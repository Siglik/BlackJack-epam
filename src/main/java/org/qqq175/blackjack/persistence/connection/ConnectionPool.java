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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.persistence.dao.util.Settings;

/**
 * Connection pool manages database connections.
 * 
 * @author qqq175
 */
public class ConnectionPool {
	private static final String POOL_IS_CLOSING = "Illegal state - connection pool is closing";
	private static final String CLOSE_CONNECTION_ERROR = "Unable to close connection";
	private static final String UNEXPECTED_INTERRUPT = "Unexpected interrupt";
	private static final String ESPABLISH_CONNECTION_ERROR = "Unable to espablish database connection.";
	private static final String UNABLE_TO_REGISTER_DRIVER = "Unable to register DB driver";
	private static Logger log = LogManager.getLogger(ConnectionPool.class);
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
			log.fatal(UNABLE_TO_REGISTER_DRIVER, e);
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
				log.warn(UNEXPECTED_INTERRUPT, e);
			} finally {
				semaphore.release();
			}
		}
		return instance.get();
	}

	/**
	 * Estasblish and return new db connection
	 * 
	 * @return
	 */
	private Connection createConnection() {
		Connection conn = null;
		Settings.Database dbSettings = Settings.getInstance().getDatabase();
		try {
			conn = DriverManager.getConnection(dbSettings.getDBUrl(), dbSettings.getUser(), dbSettings.getPassword());
			connectionsCount.incrementAndGet();
		} catch (SQLException ex) {
			log.fatal(ESPABLISH_CONNECTION_ERROR, ex);
			throw new RuntimeException(ESPABLISH_CONNECTION_ERROR + ex.getMessage(), ex);
		}
		return conn;
	}

	/**
	 * close pool and all connection.
	 */
	public void close() {
		while (connectionsCount.get() > 0) {
			try {
				availableConnections.take().close();
				int left = connectionsCount.decrementAndGet();
			} catch (SQLException e) {
				log.error(CLOSE_CONNECTION_ERROR, e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				log.warn(UNEXPECTED_INTERRUPT, e);
			}
		}
	}

	/**
	 * Retrive connection from pool. Its creates wrapper and put connection to
	 * wrapper before retrieve.
	 * 
	 * @return connection wrapper
	 */
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
				log.error(ESPABLISH_CONNECTION_ERROR, e);
			}
		} catch (InterruptedException e) {
			log.warn(UNEXPECTED_INTERRUPT, e);
		}

		return new ConnectionWrapper(conn);
	}

	/**
	 * Available only in package scope. Put connection back to pool. It's
	 * usually calls from wrapper
	 * 
	 * @param conn
	 */
	void putbackConnection(Connection conn) {
		if (availableConnections.size() < MIN_POOL_SIZE) {
			try {
				availableConnections.put(conn);
			} catch (InterruptedException e) {
				log.warn(UNEXPECTED_INTERRUPT, e);
			}
		} else {
			try {
				conn.close();
				connectionsCount.decrementAndGet();
			} catch (SQLException e) {
				log.error(CLOSE_CONNECTION_ERROR, e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return connections count avaliable in pool at moment
	 */
	public int getAvailableConnectionsCount() {
		return availableConnections.size();
	}

	/**
	 * throws exception if perform action on is clossing pool
	 */
	private void assertNotClosing() {
		if (isClosing.get()) {
			log.fatal(POOL_IS_CLOSING);
			throw new RuntimeException("Cannot perform operation: pool is closing.");
		}
	}
}
