package org.qqq175.blackjack.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.qqq175.blackjack.persistence.dao.util.Settings;

public class ConnectionPool {
	private static ConnectionPool instance;
	private BlockingQueue<Connection> availableConnections;

	private ConnectionPool() {
		Settings settings = Settings.getInstance();
		availableConnections = new ArrayBlockingQueue<>(settings.getDatabase().getMaxPoolSize());
		System.out.println();

		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ConnectionPool localInstance = instance;
		if (localInstance == null) {
			synchronized (Settings.class) {
				localInstance = instance;
				if (instance == null) {
					instance = localInstance = new ConnectionPool();
				}
			}
		}

		return localInstance;
	}

	private Connection createConnection() {
		Connection conn = null;
		Settings.Database dbSettings = Settings.getInstance().getDatabase();
		try {
			conn = DriverManager.getConnection(dbSettings.getDBUrl(), dbSettings.getUser(), dbSettings.getPassword());
		} catch (SQLException ex) {
			// TODO: catch
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}

	public ConnectionWrapper retrieveConnection() {
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
}
