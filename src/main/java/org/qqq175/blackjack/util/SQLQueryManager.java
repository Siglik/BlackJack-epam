/**
 * 
 */
package org.qqq175.blackjack.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author qqq175
 *
 */
public class SQLQueryManager {
	private final ResourceBundle resourceBundle;
	private volatile static SQLQueryManager instance;
	
	private SQLQueryManager() { 
		resourceBundle = ResourceBundle.getBundle("conf/sqlquery", Locale.ROOT);
	}
	
	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return Settings instance
	 */
	public static SQLQueryManager getInstance() {
		SQLQueryManager localInstance = instance;
		if (localInstance == null) {
			synchronized (SQLQueryManager.class) {
				localInstance = instance;
				if (instance == null) {
					instance = localInstance = new SQLQueryManager();
				}
			}
		}

		return localInstance;
	}
	public String getQuery(String key) {
		return resourceBundle.getString(key);
	}
}
