package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

public class ChangePersonalAction implements Action {

	public ChangePersonalAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;
		Map<String, String[]> params = request.getParameterMap();
		ModifyUserLogic muLogic = new ModifyUserLogic();
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		
		// TODO save modification
		
		ModifyUserLogic.Result logicResult = muLogic.changePersonal(params, user);
		if (logicResult == ModifyUserLogic.Result.OK){
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_PERSONAL_ERROR, "message.error.pass " + logicResult.getMessage());
		}
		
		muLogic.updateSessionUser(request.getSession());
		
		String context = Settings.getInstance().getContextPath();
		String path = context + JSPPathManager.getProperty("command.settings");
		return new ActionResult(REDIRECT, path);
	}

}
