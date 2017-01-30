package org.qqq175.blackjack.action.implemented.game;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.logic.game.GameUtilLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.pool.GamePool;

public class NewGameAction implements Action {
	private static Logger log = LogManager.getLogger(NewGameAction.class);
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
		BlackJackGame game = null;

		if (gamePool.containsKey(user.getId())) {
			game = gamePool.get(user.getId());
		} else {
			GameId gameId;
			try {
				GameUtilLogic guLogic = new GameUtilLogic();
				gameId = guLogic.newGameEntity(user);
				if (mode != Mode.SINGLEPLAYER) {
					Iterator<BlackJackGame> games = guLogic.getJoinableGameList().iterator();
					boolean found = false;
					while (!found && games.hasNext()) {
						BlackJackGame localGame = games.next();
						Player player = localGame.join(user);
						if (player != null) {
							game = localGame;
							found = true;
						}
					}
				}
				if (game != null) {
					log.debug("Game found " + game.getId().getValue());
				}
				if (game == null) {
					game = BlackJackGame.createGame(gameId, user, mode.getMaxPlayers());
					log.debug("Creating new game " + game.getId().getValue());
				}
				gamePool.put(user.getId(), game);
				log.debug("Put to pool game " + gamePool.get(user.getId()).getId().getValue());
			} catch (LogicException e) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Sorry, some error occurred! " + e.getMessage());
			}
		}

		if (game != null) {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_GAME, game);
			result = new ActionResult(ActionResult.ActionType.REDIRECT,
					Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.game"));
		} else {
			result = new ActionResult(ActionResult.ActionType.REDIRECT,
					Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.index"));
		}

		return result;
	}

}
