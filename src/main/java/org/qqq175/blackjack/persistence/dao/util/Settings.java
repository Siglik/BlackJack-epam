/**
 * 
 */
package org.qqq175.blackjack.persistence.dao.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.impl.DAOFactoryImpl;

/**
 * @author qqq175
 *
 */
public class Settings {
	private final DAOFactory DAO_FACTORY;
	private final Database DATABASE;
	private String realPath = null;
	private String contextPath = null;
	private final String SALT;
	private final String PHOTO_FOLDER;
	private final List<String> PHOTO_EXTENSIONS;

	private static AtomicReference<Settings> instance = new AtomicReference<>();;
	private static Semaphore semaphore = new Semaphore(1);

	/** Constructor */
	private Settings() {
		this.DATABASE = new Database();
		this.DAO_FACTORY = new DAOFactoryImpl();
		Properties props = new Properties();
		try (FileInputStream in = new FileInputStream("conf/app.properties")) {
			props.load(in);
		} catch (IOException e) {
		}
		SALT = "Unds&4s>dfuPMdqmx84Yfagt=274bfa#fdsa64q1";
		PHOTO_FOLDER = "/img/user";

		PHOTO_EXTENSIONS = new ArrayList<>(2);
		PHOTO_EXTENSIONS.add(".png");
		PHOTO_EXTENSIONS.add(".jpg");
	}

	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return Settings instance
	 */
	public static Settings getInstance() {
		if (instance.get() == null) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO log
			}
			instance.compareAndSet(null, new Settings());
			semaphore.release();
		}

		return instance.get();
	}

	/**
	 * contain database connection settings
	 * 
	 * @author qqq175
	 */
	public final class Database {
		private final static String DBPROPS_PATH = "/conf/db.properties";
		private final String DB_URL;
		private final String DRIVER;
		private final String USER;
		private final String PASSWORD;
		private final int MIN_POOL_SIZE;
		private final int MAX_POOL_SIZE;
		private final String MIN_POOL_SIZE_DEFAULT = "5";
		private final String MAX_POOL_SIZE_DEFAULT = "25";

		private Database() {
			Properties props = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			try (InputStream in = classLoader.getResourceAsStream(DBPROPS_PATH);) {
				props.load(in);
			} catch (IOException e) {
			}

			DB_URL = props.getProperty("dburl");
			DRIVER = props.getProperty("driver");
			USER = props.getProperty("user");
			PASSWORD = props.getProperty("password");
			MIN_POOL_SIZE = Integer.parseInt(props.getProperty("minPoolSize", MIN_POOL_SIZE_DEFAULT));
			MAX_POOL_SIZE = Integer.parseInt(props.getProperty("maxPoolSize", MAX_POOL_SIZE_DEFAULT));
		}

		/**
		 * @return the user
		 */
		public String getUser() {
			return USER;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return PASSWORD;
		}

		/**
		 * @return the minPoolSize
		 */
		public int getMinPoolSize() {
			return MIN_POOL_SIZE;
		}

		/**
		 * @return the maxPoolSize
		 */
		public int getMaxPoolSize() {
			return MAX_POOL_SIZE;
		}

		/**
		 * @return the url
		 */
		public String getDBUrl() {
			return DB_URL;
		}

		/**
		 * @return the driver
		 */
		public String getDriver() {
			return DRIVER;
		}
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return SALT;
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
		return DATABASE;
	}

	/**
	 * @return the daoFactory
	 */
	public DAOFactory getDaoFactory() {
		return DAO_FACTORY;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath
	 *            the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * @return the pHOTO_FOLDER
	 */
	public String getPhotoFolder() {
		return PHOTO_FOLDER;
	}

	/**
	 * @return the pHOTO_EXTENSIONS
	 */
	public List<String> getPhotoExtensions() {
		return PHOTO_EXTENSIONS;
	}

}