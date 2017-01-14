package org.qqq175.blackjack.logic.main;

import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

public class IndexLogic {

	public IndexLogic() {
		// TODO Auto-generated constructor stub
	}

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
