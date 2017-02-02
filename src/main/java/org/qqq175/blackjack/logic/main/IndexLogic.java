package org.qqq175.blackjack.logic.main;

import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Index action logic methods
 * 
 * @author qqq175
 *
 */
public class IndexLogic {

	/**
	 * return index page path according to users type
	 * 
	 * @param user
	 * @return
	 */
	public String definePathByUser(User user) {
		String result = null;
		if (user != null) {
			User.Type type = user.getType();
			switch (type) {
			case ADMIN:
				result = JSPPathManager.getProperty("page.admin");
				break;
			case PLAYER:
				result = JSPPathManager.getProperty("page.player");
				break;
			default:
				throw new IllegalArgumentException("Unknown enum '" + type.getClass().getName() + "' value '" + type.name() + "'");
			}

		} else {
			result = JSPPathManager.getProperty("page.main");
		}

		return result;
	}
}
