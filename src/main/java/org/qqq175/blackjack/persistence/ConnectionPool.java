package org.qqq175.blackjack.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.qqq175.blackjack.util.Settings;

class ConnectionPool {
	private static volatile ConnectionPool instance;
	private BlockingQueue<ConnectionWrapper> availableConns;
	private String url;

	private ConnectionPool() {
		Settings settings = Settings.getInstance();
		availableConns = new ArrayBlockingQueue<>(settings.getDatabase().getPool().getMaxPoolSize());

		try {
			Class.forName(settings.getDatabase().getDriver());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.url = settings.getDatabase().getUrl();
		for (int i = 0; i < settings.getDatabase().getPool().getMinPoolSize(); i++) {
			availableConns.add(getConnection());
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

	private ConnectionWrapper getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			// TODO: catch
		}
		return new ConnectionWrapper(conn, this);
	}

	public ConnectionWrapper retrieve() throws SQLException, InterruptedException {
		return availableConns.take();
	}

	void putback(ConnectionWrapper cw) throws NullPointerException, InterruptedException {
		availableConns.put(cw);
	}

	public int getAvailableConnsCnt() {
		return availableConns.size();
	}
}
