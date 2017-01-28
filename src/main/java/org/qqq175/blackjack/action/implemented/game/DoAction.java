package org.qqq175.blackjack.action.implemented.game;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

public class DoAction implements Action {
	ActionType actionType;

	public DoAction(ActionType actionType) {
		this.actionType = actionType;
	}

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
			if (bjGame != null) {
				try {
					switch (actionType) {
					case DEAL:
						BigDecimal bid = new BigDecimal(request.getParameter(StringConstant.PARAMETER_BID));
						bjGame.deal(user, bid);
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
					resultJson.put("result", "OK");
				} catch (GameActionDeniedException e) {
					resultJson.put("result", "ERROR");
					e.printStackTrace();
				} catch (NumberFormatException e) {
					resultJson.put("result", "ERROR");
					e.printStackTrace();
				}
			} else {
				resultJson.put("result", "ERROR");
				resultJson.put("message", "NO GAME");
			}
		} else {
			resultJson.put("result", "ERROR");
		}

		request.setAttribute(StringConstant.ATTRIBUTE_JSON, resultJson);
		result = new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);

		return result;
	}

}
