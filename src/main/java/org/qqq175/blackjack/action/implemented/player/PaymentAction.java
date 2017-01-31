package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Fill balance or withdrawal money 
 * @author qqq175
 */
public class PaymentAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		Map<String, String[]> params = request.getParameterMap();

		AccountOperationLogic aoLogic = new AccountOperationLogic();
		AccountOperationLogic.Result result = aoLogic.doPayment(params, user.getId());
		if (result == AccountOperationLogic.Result.OK) {
			ModifyUserLogic muLogic = new ModifyUserLogic();
			muLogic.updateSessionUser(request.getSession());
		} else {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_PAYMENT, "Error: " + result.getMessage());
		}

		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.balance"));
	}

}
