package org.qqq175.blackjack.service.implemented.game;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.blackjack.BlackJackGame;
import org.qqq175.blackjack.logic.blackjack.entity.Player;
import org.qqq175.blackjack.logic.game.GameUtilLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.pool.GamePool;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * Create new game, or if user already has active game - return it
 * @author qqq175
 */
public class NewGameAction implements Action {
	private static Logger log = LogManager.getLogger(NewGameAction.class);
	private Mode mode;

	/**
	 * available game types
	 * @author qqq175
	 */
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
	
	/**
	 * construct action to create game of type specified by mode
	 * @param mode
	 */
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
				/* try to find multiplayer game to join */
				if (mode != Mode.SINGLEPLAYER) {
					Iterator<BlackJackGame> games = guLogic.getJoinableGameList().iterator();
					boolean found = false;
					while (!found && games.hasNext()) {
						BlackJackGame localGame = games.next();
						/* prevent double join to same game after just leaving it and round isn't finished*/
						if (!localGame.isInGame(user)) {
							Player player = localGame.join(user);
							if (player != null) {
								game = localGame;
								found = true;
							}
						}
					}
					request.getSession().setAttribute(StringConstant.ATTRIBUTE_SHOWCHAT, true);
				} else {
					request.getSession().setAttribute(StringConstant.ATTRIBUTE_SHOWCHAT, false);
				}
				/*if game isn't found or SOLO - create new*/
				if (game == null) {
					game = BlackJackGame.createGame(gameId, user, mode.getMaxPlayers());
				}
				gamePool.put(user.getId(), game);
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
