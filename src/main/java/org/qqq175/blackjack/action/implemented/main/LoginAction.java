package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.LoginLogic;
import org.qqq175.blackjack.persistence.dao.util.Settings;

public class LoginAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		LoginLogic logic = new LoginLogic();
		String email = request.getParameter(StringConstant.PARAMETER_EMAIL);
		String password = request.getParameter(StringConstant.PARAMETER_PASSWORD);
		LoginLogic.Result result = null;
		String errorMessage = "";
		result = logic.loginUser(email, password, request.getSession());

		switch (result) {
		case OK:
			break;
		case NOTFOUND:
			errorMessage = "Wrong login or password.";
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_LOGIN, errorMessage);
			break;
		case UNACTIVE:
			errorMessage = "This accout is deactivated.";
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_LOGIN, errorMessage);
			break;
		case INUSE:
			errorMessage = "This accout is already in use.";
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_LOGIN, errorMessage);
			break;
		}

		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}
}
