/**
 * 
 */
package org.qqq175.blackjack.persistence.dao.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provide access to sql query resource bundle
 * 
 * @author qqq175
 *
 */
public class SQLQueryManager {
	private final ResourceBundle RESOURCE_BUNDLE;
	private final static SQLQueryManager INSTANCE = new SQLQueryManager();

	private SQLQueryManager() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle("conf/sqlquery", Locale.ROOT);
	}

	/**
	 * @return SQLQueryManager instance
	 */
	public static SQLQueryManager getInstance() {
		return INSTANCE;
	}

	public String getQuery(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
}
