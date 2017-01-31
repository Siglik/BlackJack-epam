package org.qqq175.blackjack.service.implemented.player;

import static org.qqq175.blackjack.service.ActionResult.ActionType.FORWARD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * shows user settings page
 * @author qqq175
 */
public class SettingsAction implements Action {

	public SettingsAction() {
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.settings"));
		IndexLogic indexLogic = new IndexLogic();
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		request.setAttribute(StringConstant.ATTRIBUTE_USER, user);

		return new ActionResult(FORWARD, indexLogic.definePathByUser(user));
	}

}
