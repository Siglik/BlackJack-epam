package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.LoginLogic;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

public class LoginAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request) {
		LoginLogic logic = new LoginLogic();
		String email = request.getParameter(StringConstant.PARAMETER_EMAIL);
		String password = request.getParameter(StringConstant.PARAMETER_PASSWORD);
		System.out.println(email + " ^ " + password);
		LoginLogic.Result result = null;
		String errorMessage = "";
		User user = null;
		result = logic.loginUser(email, password, request.getSession());

		switch (result) {
		case OK:
			user = (User) request.getSession(true).getAttribute(StringConstant.ATTRIBUTE_USER);
			break;
		case NOTFOUND:
			errorMessage = "Wrong login or password.";
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_LOGIN, errorMessage);
			break;
		case UNACTIVE:
			errorMessage = "This accout is deactivated.";
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_LOGIN, errorMessage);
			break;
		}

		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}
}
