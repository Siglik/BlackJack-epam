package org.qqq175.blackjack.action.implemented.game;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Shows game rules
 * @author qqq175
 */
public class ShowRulesAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		ActionResult result;

		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.rules"));
		IndexLogic logic = new IndexLogic();
		result = new ActionResult(FORWARD, logic.definePathByUser(user));

		return result;
	}

}
