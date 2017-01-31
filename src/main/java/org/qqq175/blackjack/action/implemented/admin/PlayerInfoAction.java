package org.qqq175.blackjack.action.implemented.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * show user statistics, balance and operations history
 * 
 * @author qqq175
 */
public class PlayerInfoAction implements Action {
	private static Logger log = LogManager.getLogger(PlayerInfoAction.class);
	private static final String DATA_ERROR = "Unable to prepare necessary data";
	private static final String WRONG_PAGE_FORMAT = "Wrong page number format.";
	private static final String MISSING_PARAMETER_ID = "Missing parameter ID.";
	private static final String WRONG_ID_FORMAT = "Wrong id format.";
	private final static String ERROR = "Unable to get required data at PlayerInfoAction::execute.";
	private static final int PAGINATION = 15;

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;

		/* parse user id param. Send popup error if not found */
		String id = request.getParameter(StringConstant.PARAMETER_ID);
		UserId userId = null;
		if (id != null && !id.isEmpty()) {
			try {
				userId = new UserId(Long.parseLong(id));
			} catch (NumberFormatException e) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, WRONG_ID_FORMAT);
			}
		} else {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, MISSING_PARAMETER_ID);
		}

		/* parse page number. if no such parameter or wrong format set to 1 */
		String page = request.getParameter(StringConstant.PARAMETER_PAGE);
		int pageNumber;
		if (page != null && !page.isEmpty()) {
			try {
				pageNumber = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, WRONG_PAGE_FORMAT);
				pageNumber = 1;
			}
		} else {
			pageNumber = 1;
		}

		/*
		 * get necessary data and bind it to request attributes. if DB error -
		 * redirect error page (send error)
		 */
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
				log.warn(DATA_ERROR, e);
			}
		} else {
			result = new ActionResult(ActionResult.ActionType.REDIRECT,
					Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.playerslist"));
		}

		return result;
	}

}
