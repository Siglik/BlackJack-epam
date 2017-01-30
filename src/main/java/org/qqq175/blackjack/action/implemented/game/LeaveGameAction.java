package org.qqq175.blackjack.action.implemented.game;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

public class LeaveGameAction implements Action {

	public LeaveGameAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		if (user != null) {
			BlackJackGame game = GamePool.getInstance().get(user.getId());
			if (game != null) {
				GamePool.getInstance().remove(user.getId());
				game.leave(user);
			}
		}
		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}

}
