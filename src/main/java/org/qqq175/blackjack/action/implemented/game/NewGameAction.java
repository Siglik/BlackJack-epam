package org.qqq175.blackjack.action.implemented.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.game.BJGame;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

public class NewGameAction implements Action {
	private Mode mode;

	public enum Mode {
		SINGLEPLAYER(1), MULTIPLAYER(3);

		private int maxPlayers;

		private Mode(int maxPlayers) {
			this.maxPlayers = maxPlayers;
		}

		/**
		 * @return the maxPlayers
		 */
		public int getMaxPlayers() {
			return maxPlayers;
		}

	}

	public NewGameAction(Mode mode) {
		this.mode = mode;
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		GamePool gamePool = GamePool.getInstance();
		ActionResult result;
		BJGame game;

		if (gamePool.containsKey(user.getId())) {
			game = gamePool.get(user.getId());
		} else {
			game = new BlackJackGame(user, mode.getMaxPlayers());
			gamePool.put(user.getId(), game);
		}

		request.getSession().setAttribute(StringConstant.ATTRIBUTE_GAME, game);
		result = new ActionResult(ActionResult.ActionType.REDIRECT,
				Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.game"));

		return result;
	}

}
