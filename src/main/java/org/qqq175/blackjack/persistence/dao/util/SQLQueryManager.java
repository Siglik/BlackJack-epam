/**
 * 
 */
package org.qqq175.blackjack.persistence.dao.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qqq175
 *
 */
public class SQLQueryManager {
	private final ResourceBundle RESOURCE_BUNDLE;
	private static AtomicReference<SQLQueryManager> instance = new AtomicReference<>();
    private	static Semaphore semaphore = new Semaphore(1);
	
	private SQLQueryManager() { 
		RESOURCE_BUNDLE = ResourceBundle.getBundle("conf/sqlquery", Locale.ROOT);
	}
	
	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return Settings instance
	 */
	public static SQLQueryManager getInstance() {
		SQLQueryManager localInstance = instance.get();
		if (localInstance == null) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				//TODO log
			}
				localInstance = instance.get();
				if (localInstance == null) {
					localInstance = new SQLQueryManager();
					instance.set(localInstance);
				}
			semaphore.release();
		}

		return localInstance;
	}
	public String getQuery(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
}
