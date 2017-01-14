package org.qqq175.blackjack.action.implemented.main;

import javax.servlet.http.HttpServletRequest;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;

public class SetLocaleAction implements Action {

	public SetLocaleAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request) {
		String locale = request.getParameter(StringConstant.PARAMETER_LOCALE);
		return null;
	}

}
