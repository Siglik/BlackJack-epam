package org.qqq175.blackjack.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.qqq175.blackjack.persistence.dao.util.Settings;

public class ConnectionPool {
	private static AtomicReference<ConnectionPool> instance = new AtomicReference<>();
	private AtomicInteger connectionsCount;
	private BlockingQueue<Connection> availableConnections;
	private boolean isClosing;
	private static Semaphore semaphore = new Semaphore(1);
	private static boolean isEmpty = true;

	private ConnectionPool() {
		isClosing = false;
		connectionsCount = new AtomicInteger();
		Settings settings = Settings.getInstance();
		availableConnections = new ArrayBlockingQueue<>(settings.getDatabase().getMaxPoolSize());

		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e) {
			// TODO LOG fatal
			throw new RuntimeException("Unable to load db driver.\n" + e.getMessage(), e);
		}
		for (int i = 0; i < settings.getDatabase().getMinPoolSize(); i++) {
			availableConnections.add(createConnection());
		}
	}

	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return ConnectionPool instance
	 */
	public static ConnectionPool getInstance() {
		if (isEmpty) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO log
			}
			if (instance.get() == null) {
				instance.set(new ConnectionPool());
				isEmpty = false;
			}
			semaphore.release();
		}
		return instance.get();
	}

	private Connection createConnection() {
		Connection conn = null;
		Settings.Database dbSettings = Settings.getInstance().getDatabase();
		try {
			conn = DriverManager.getConnection(dbSettings.getDBUrl(), dbSettings.getUser(), dbSettings.getPassword());
			connectionsCount.incrementAndGet();
		} catch (SQLException ex) {
			throw new RuntimeException("Unable to espablish database connection.\n" + ex.getMessage(), ex);
			// TODO log fatal
		}
		return conn;
	}

	public void close() {
		while (connectionsCount.get() > 0) {
			try {
				availableConnections.take().close();
				int left = connectionsCount.decrementAndGet();
				System.out.println("Closing pool. Connection left: " + left);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ConnectionWrapper retrieveConnection() {
		assertNotClosing();
		Connection conn = null;
		try {
			conn = availableConnections.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ConnectionWrapper(conn);
	}

	void putbackConnection(Connection cw) {
		try {
			availableConnections.put(cw);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAvailableConnectionsCount() {
		return availableConnections.size();
	}

	private void assertNotClosing() {
		if (isClosing) {
			throw new RuntimeException("Cannot perform operation: pool is closing.");
		}
	}
}
