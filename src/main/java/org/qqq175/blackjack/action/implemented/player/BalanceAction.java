package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * shows user balance information
 * 
 * @author qqq175
 */
public class BalanceAction implements Action {
	private static final String BALANCE_DATA_ERROR = "Can't get balance data";
	private static Logger log = LogManager.getLogger(BalanceAction.class);

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		AccountOperationLogic aoLogic = new AccountOperationLogic();

		try {
			Map<String, BigDecimal> totals = aoLogic.calcTotals(user.getId());
			request.setAttribute(StringConstant.ATTRIBUTE_TOTAL, totals);
		} catch (LogicException e) {
			log.error(BALANCE_DATA_ERROR, e);
			e.printStackTrace();
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Error: " + e.getMessage());
		}

		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.balance"));
		IndexLogic logic = new IndexLogic();

		return new ActionResult(FORWARD, logic.definePathByUser(user));
	}

}
