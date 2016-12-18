/**
 * 
 */
package org.qqq175.blackjack.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author qqq175
 *
 */
public class Settings {
	private final Database database;
	private String realPath = null;
	private final String salt;
	private static volatile Settings instance;

	/** Constructor */
	private Settings() {
		database = new Database();
		Properties props = new Properties();
		try (FileInputStream in = new FileInputStream("conf/app.properties")) {
			props.load(in);
		} catch (IOException e) {
		}
		salt = "Unds&4s>dfuPMdqmx84Yfagt=274bfa#fdsa64q1";
	}

	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return Settings instance
	 */
	public static Settings getInstance() {
		Settings localInstance = instance;
		if (localInstance == null) {
			synchronized (Settings.class) {
				localInstance = instance;
				if (instance == null) {
					instance = localInstance = new Settings();
				}
			}
		}

		return localInstance;
	}

	/**
	 * contain database connection settings
	 * 
	 * @author qqq175
	 */
	public final class Database {
		private final String url;
		private final String driver;
		private final String user;
		private final String password;
		private final int minPoolSize;
		private final int maxPoolSize;

		private Database() {
			Properties props = new Properties();
			try (FileInputStream in = new FileInputStream("conf/db.properties")) {
				props.load(in);
			} catch (IOException e) {

			}

			url = props.getProperty("url");
			driver = props.getProperty("driver");
			user = props.getProperty("user");
			password = props.getProperty("password");
			minPoolSize = Integer.parseInt(props.getProperty("minPoolSize", "5"));
			maxPoolSize = Integer.parseInt(props.getProperty("maxPoolSize", "25"));
		}

		/**
		 * @return the user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @return the minPoolSize
		 */
		public int getMinPoolSize() {
			return minPoolSize;
		}

		/**
		 * @return the maxPoolSize
		 */
		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @return the driver
		 */
		public String getDriver() {
			return driver;
		}
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @return the realPath
	 */
	public String getRealPath() {
		return realPath;
	}

	/**
	 * @param realPath
	 *            the realPath to set
	 */
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

}