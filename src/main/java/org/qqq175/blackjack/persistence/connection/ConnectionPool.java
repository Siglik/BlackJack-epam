package org.qqq175.blackjack.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.qqq175.blackjack.persistence.dao.util.Settings;

public class ConnectionPool {
	private static AtomicReference<ConnectionPool> instance = new AtomicReference<>();
	private static int VALID_TIMEOUT = 3; // seconds
	private static int RETRIEVE_TIMEOUT = 300; // milliseconds
	private static int MAX_POOL_SIZE = Settings.getInstance().getDatabase().getMaxPoolSize();
	private static int MIN_POOL_SIZE = Settings.getInstance().getDatabase().getMinPoolSize();
	private AtomicInteger connectionsCount;
	private BlockingQueue<Connection> availableConnections;
	private AtomicBoolean isClosing;
	private static Semaphore semaphore = new Semaphore(1);
	private static AtomicBoolean isEmpty = new AtomicBoolean(true);

	private ConnectionPool() {
		isClosing = new AtomicBoolean(false);
		connectionsCount = new AtomicInteger();
		Settings settings = Settings.getInstance();
		availableConnections = new ArrayBlockingQueue<>(MAX_POOL_SIZE);

		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e) {
			// TODO LOG fatal
			throw new RuntimeException("Unable to load db driver.\n" + e.getMessage(), e);
		}
		for (int i = 0; i < MIN_POOL_SIZE; i++) {
			availableConnections.add(createConnection());
		}
	}

	/**
	 * @return ConnectionPool instance
	 */
	public static ConnectionPool getInstance() {
		if (isEmpty.get()) {
			try {
				semaphore.acquire();
				if (instance.get() == null) {
					instance.set(new ConnectionPool());
					isEmpty.set(false);
				}
			} catch (InterruptedException e) {
				// TODO log
			} finally {
				semaphore.release();
			}
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
			conn = availableConnections.poll(RETRIEVE_TIMEOUT, TimeUnit.MILLISECONDS);
			if (conn == null) {
				if (connectionsCount.get() < MAX_POOL_SIZE) {
					conn = this.createConnection();
				} else {
					conn = availableConnections.take();
				}
			}
			try {
				if (!conn.isValid(VALID_TIMEOUT)) {
					conn.close();
					connectionsCount.decrementAndGet();
					conn = this.createConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ConnectionWrapper(conn);
	}

	void putbackConnection(Connection conn) {
		if (availableConnections.size() < MIN_POOL_SIZE) {
			try {
				availableConnections.put(conn);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				conn.close();
				connectionsCount.decrementAndGet();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getAvailableConnectionsCount() {
		return availableConnections.size();
	}

	private void assertNotClosing() {
		if (isClosing.get()) {
			throw new RuntimeException("Cannot perform operation: pool is closing.");
		}
	}
}
