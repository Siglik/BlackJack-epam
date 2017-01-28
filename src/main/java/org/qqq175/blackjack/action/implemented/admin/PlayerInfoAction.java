package org.qqq175.blackjack.action.implemented.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class PlayerInfoAction implements Action {
	private final static String ERROR = "Unable to get required data at PlayerInfoAction::execute.";
	private static final int PAGINATION = 15;

	public PlayerInfoAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;

		String id = request.getParameter(StringConstant.PARAMETER_ID);
		UserId userId = null;
		if (id != null && !id.isEmpty()) {
			try {
				userId = new UserId(Long.parseLong(id));
			} catch (NumberFormatException e) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Wrong id format.");
			}
		} else {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Missing parameter ID.");
		}

		String page = request.getParameter(StringConstant.PARAMETER_PAGE);
		int pageNumber;
		if (page != null && !page.isEmpty()) {
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "Wrong page number format.");
				pageNumber = 1;
			}
		} else {
			pageNumber = 1;
		}

		if (userId != null) {
			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			UserDAO userDAO = daoFactory.getUserDAO();
			UserstatDAO userstatDAO = daoFactory.getUserstatDAO();
			AccountOperationLogic aoLogic = new AccountOperationLogic();

			try {
				User user = userDAO.findEntityById(userId);
				Userstat userstat = userstatDAO.findEntityById(userId);
				Map<String, BigDecimal> totals = aoLogic.calcTotals(userId);

				List<AccountOperation> users = aoLogic.findUserOperations(userId, pageNumber, PAGINATION);
				Long operCount = aoLogic.countOpers(userId);

				Long pages = operCount / PAGINATION + (operCount % PAGINATION == 0 ? 0 : 1);

				request.setAttribute(StringConstant.ATTRIBUTE_PAGE_COUNT, pages);
				request.setAttribute(StringConstant.ATTRIBUTE_OPERATIONS, users);
				request.setAttribute(StringConstant.ATTRIBUTE_USER, user);
				request.setAttribute(StringConstant.ATTRIBUTE_USERSTAT, userstat);
				request.setAttribute(StringConstant.ATTRIBUTE_TOTAL, totals);
				request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.playerinfo"));
				result = new ActionResult(ActionResult.ActionType.FORWARD, JSPPathManager.getProperty("page.admin"));
			} catch (DAOException | LogicException e) {
				result = new ActionResult(ActionResult.ActionType.SENDERROR, ERROR + "\n" + e.getMessage());
			}
		} else {
			result = new ActionResult(ActionResult.ActionType.REDIRECT,
					Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.playerslist"));
		}

		return result;
	}

}
