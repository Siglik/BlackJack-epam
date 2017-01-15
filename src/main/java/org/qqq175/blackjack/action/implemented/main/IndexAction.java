package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.persistence.entity.User;

public class IndexAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession(true).getAttribute(StringConstant.ATTRIBUTE_USER);
		IndexLogic logic = new IndexLogic();

		return new ActionResult(FORWARD, logic.definePathByUser(user));
	}

}
