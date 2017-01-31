package org.qqq175.blackjack.service.implemented.game;

import static org.qqq175.blackjack.service.ActionResult.ActionType.REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * Leave current user's game
 * @author qqq175
 *
 */
public class LeaveGameAction implements Action {

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
