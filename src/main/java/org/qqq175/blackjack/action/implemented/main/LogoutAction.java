package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

public class LogoutAction implements Action {
	private static Logger log = LogManager.getLogger(LogoutAction.class);

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		request.getSession().invalidate();
		if (user != null) {
			UserPool.getInstance().remove(user.getId());
			log.debug(UserPool.getInstance().containsKey(user.getId()) + " (" + UserPool.getInstance().size() + ") id: " + user.getId().getValue());
		}
		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}
}