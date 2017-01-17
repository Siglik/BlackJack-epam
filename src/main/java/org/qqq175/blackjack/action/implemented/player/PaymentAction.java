package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

public class PaymentAction implements Action {

	public PaymentAction() {

	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		Map<String, String[]> params = request.getParameterMap();

		AccountOperationLogic aoLogic = new AccountOperationLogic();
		AccountOperationLogic.Result result = aoLogic.doPayment(params, user.getId());
		if (result == AccountOperationLogic.Result.OK) {
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			try {
				User userUpdated = userDAO.findEntityById(user.getId());
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_USER, userUpdated);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_ERROR_PAYMENT, "Error: " + result.getMessage());
		}

		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.balance"));
	}

}
