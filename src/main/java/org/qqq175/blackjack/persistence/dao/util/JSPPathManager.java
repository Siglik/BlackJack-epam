/**
 * 
 */
package org.qqq175.blackjack.persistence.dao.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provide access to page path resource bundle
 * 
 * @author qqq175
 *
 */
public class JSPPathManager {
	private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("conf/pagepath", Locale.ROOT);

	private JSPPathManager() {
	}

	public static String getProperty(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
}
