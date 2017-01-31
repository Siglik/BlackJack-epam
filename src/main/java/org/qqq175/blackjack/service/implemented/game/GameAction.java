package org.qqq175.blackjack.service.implemented.game;

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
 * Main game actions - shows game application
 * @author qqq175
 */
public class GameAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		ActionResult result;

		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.blackjack"));
		request.setAttribute(StringConstant.ATTRIBUTE_IN_GAME, true);
		IndexLogic logic = new IndexLogic();
		result = new ActionResult(FORWARD, logic.definePathByUser(user));

		return result;
	}

}
