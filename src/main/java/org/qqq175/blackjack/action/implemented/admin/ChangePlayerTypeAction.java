package org.qqq175.blackjack.action.implemented.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.admin.RankLogic;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Toogles user's rank ADMIN<-PLAYER
 * request param - "id" 
 * @author qqq175
 */
public class ChangePlayerTypeAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter(StringConstant.PARAMETER_ID);
		ModifyUserLogic muLogic = new ModifyUserLogic();
		RankLogic rankLogic = new RankLogic();
		muLogic.updateSessionUser(request.getSession());

		User user = (User) request.getSession(false).getAttribute(StringConstant.ATTRIBUTE_USER);
		JSONObject result = rankLogic.toogleRank(id, user);

		request.setAttribute(StringConstant.ATTRIBUTE_JSON, result);
		return new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);
	}

}
