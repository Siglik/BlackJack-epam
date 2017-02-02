/**
 * 
 */
package org.qqq175.blackjack.persistence.dao.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.impl.DAOFactoryImpl;

/**
 * Contains main application settings
 * 
 * @author qqq175
 *
 */
public class Settings {
	private static final String UNEXPECTED_INTERRUPT = "Unexpected interrupt";

	private static final String INITIALIZE_SETTINGS_FATAL = "Can't initialize application settings";

	public static final boolean IS_DEBUG = true;

	private static final String PROPS_DELIMETER = ";";
	private static final String APP_PROPS_PATH = "conf/app.properties";
	private final DAOFactory DAO_FACTORY;
	private final Database DATABASE;
	private String realPath = null;
	private String contextPath = null;
	private final String SALT = "Unds&4s>dfuPMdqmx84Yfagt=274bfa#fdsa64q1";
	private final String PHOTO_FOLDER;
	private final String CARD_FOLDER;
	private final String DEFAULT_PHOTO;
	private final List<String> PHOTO_EXTENSIONS;

	private static AtomicReference<Settings> instance = new AtomicReference<>();;
	private static Semaphore semaphore = new Semaphore(1);
	private static Logger log = LogManager.getLogger(Settings.class);

	/** 
	*/
	private Settings() {
		this.DATABASE = new Database();
		this.DAO_FACTORY = new DAOFactoryImpl();
		// initialize setting from props file
		Properties props = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream in = classLoader.getResourceAsStream(APP_PROPS_PATH);) {
			if (in != null) {
				props.load(in);
			}
		} catch (IOException e) {
			log.fatal(INITIALIZE_SETTINGS_FATAL, e);
			throw new RuntimeException("Unable to read " + APP_PROPS_PATH);
		}
		PHOTO_FOLDER = props.getProperty("img.dir.avatar");
		CARD_FOLDER = props.getProperty("img.dir.card");
		DEFAULT_PHOTO = props.getProperty("img.default.avatar");

		String[] extensions = props.getProperty("img.extensions").split(PROPS_DELIMETER);
		PHOTO_EXTENSIONS = new ArrayList<>(Arrays.asList(extensions));
	}

	/**
	 * Double Checked singleton get instance method
	 * 
	 * @return Settings instance
	 */
	public static Settings getInstance() {
		if (instance.get() == null) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				log.warn(UNEXPECTED_INTERRUPT, e);
			}
			if (instance.get() == null) {
				instance.set(new Settings());
			}
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
				if (in != null) {
					props.load(in);
				}
			} catch (IOException e) {
				log.fatal(INITIALIZE_SETTINGS_FATAL, e);
				throw new RuntimeException("Unable to read " + DBPROPS_PATH);
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

	public String getDefaultPhoto() {
		return DEFAULT_PHOTO;
	}

	/**
	 * @return the cARD_FOLDER
	 */
	public String getCardFolder() {
		return CARD_FOLDER;
	}

}