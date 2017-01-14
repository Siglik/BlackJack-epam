package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.persistence.dao.util.Settings;

public class LogoutAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}
}