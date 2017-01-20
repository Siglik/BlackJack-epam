package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

public class BalanceAction implements Action {

	public BalanceAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		AccountOperationLogic aoLogic = new AccountOperationLogic();

		try {
			Map<String, BigDecimal> totals = aoLogic.calcTotals(user.getId());
			request.setAttribute(StringConstant.ATTRIBUTE_TOTAL, totals);
		} catch (LogicException e) {
			// TODO LOG
			e.printStackTrace();
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Error " + e.getMessage());
		}

		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.balance"));
		IndexLogic logic = new IndexLogic();

		return new ActionResult(FORWARD, logic.definePathByUser(user));
	}

}
