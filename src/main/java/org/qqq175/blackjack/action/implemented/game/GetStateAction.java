package org.qqq175.blackjack.action.implemented.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.game.GameJSONizer;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

public class GetStateAction implements Action {
	private static Logger log = LogManager.getLogger(GetStateAction.class);

	public GetStateAction() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		JSONObject resultJson;
		if (user != null) {
			BlackJackGame bjGame = GamePool.getInstance().get(user.getId());
			resultJson = GameJSONizer.toJSON(bjGame, user);
		} else {
			log.debug("---------------------------- user is NULL ----------------------");
			resultJson = new JSONObject();
			resultJson.put("result", "ERROR");
		}

		request.setAttribute(StringConstant.ATTRIBUTE_JSON, resultJson);
		result = new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);

		return result;
	}

}
