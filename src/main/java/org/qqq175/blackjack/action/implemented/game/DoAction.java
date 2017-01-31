package org.qqq175.blackjack.action.implemented.game;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

/**
 * Handles game actions such as DEAL, DOUBLE, HIT, INSURANCE, SPLIT, STAY, SURRENDER
 * @author qqq175
 */
public class DoAction implements Action {
	private static Logger log = LogManager.getLogger(DoAction.class);
	private ActionType actionType;

	/**
	 * construct DoAction class with specified action
	 * @param actionType
	 */
	public DoAction(ActionType actionType) {
		this.actionType = actionType;
	}

	/**
	 * Avaliable game actions
	 * @author qqq175
	 */
	public enum ActionType {
		DEAL, DOUBLE, HIT, INSURANCE, SPLIT, STAY, SURRENDER;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		JSONObject resultJson = new JSONObject();
		if (user != null) {
			BlackJackGame bjGame = GamePool.getInstance().get(user.getId());
			boolean isOk = true;
			if (bjGame != null) {
				try {
					switch (actionType) {
					case DEAL:
						String bidString = request.getParameter(StringConstant.PARAMETER_BID);
						if (bidString != null) {
							BigDecimal bid = new BigDecimal(request.getParameter(StringConstant.PARAMETER_BID));
							bjGame.deal(user, bid);
						} else {
							isOk = false;
						}
						break;
					case DOUBLE:
						bjGame.doubleBet(user);
						break;
					case HIT:
						bjGame.hit(user);
						break;
					case INSURANCE:
						bjGame.insurance(user);
						break;
					case SPLIT:
						bjGame.split(user);
						break;
					case STAY:
						bjGame.stay(user);
						break;
					case SURRENDER:
						bjGame.surrender(user);
						break;
					default:
						break;
					}
					ModifyUserLogic muLogic = new ModifyUserLogic();
					muLogic.updateSessionUser(request.getSession());
					if (isOk) {
						resultJson.put("result", "OK");
					} else {
						resultJson.put("result", "ERROR");
						resultJson.put("message", "MISSING PARAMS");
						log.warn("Missing params. User: " + user.getId().getValue());
					}
				} catch (GameActionDeniedException e) {
					resultJson.put("result", "ERROR");
					resultJson.put("message", "ACTION DENIED");
					log.warn("Action denied for user " + user.getId().getValue(), e);
				} catch (NumberFormatException e) {
					resultJson.put("result", "ERROR");
					resultJson.put("message", "WRONG PARAMS");
					log.warn("Wrong params. User: " + user.getId().getValue(), e);
				}
			} else {
				resultJson.put("result", "ERROR");
				resultJson.put("message", "NO GAME");
				log.warn("No game. User: " + user.getId().getValue());
			}
		} else {
			resultJson.put("result", "ERROR");
			log.warn("User is null. Session: " + request.getSession().getId());
		}

		request.setAttribute(StringConstant.ATTRIBUTE_JSON, resultJson);
		result = new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);

		return result;
	}

}
