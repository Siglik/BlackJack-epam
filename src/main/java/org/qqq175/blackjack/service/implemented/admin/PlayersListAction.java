package org.qqq175.blackjack.service.implemented.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.admin.PlayersListLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * shows players list
 * 
 * @author qqq175
 *
 */
public class PlayersListAction implements Action {
	private static Logger log = LogManager.getLogger(PlayerInfoAction.class);
	private static final String DATA_ERROR = "Unable to prepare necessay data";
	private static final int PAGINATION = 15;

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;

		PlayersListLogic plLogic = new PlayersListLogic();
		/* parse page number. if no such parameter or wrong format set to 1 */
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

		/* prepare user list data */
		List<User> users = null;
		Long userCount = null;
		try {
			users = plLogic.findUsers(pageNumber, PAGINATION);
			userCount = plLogic.countUsers();
		} catch (LogicException e) {
			result = new ActionResult(ActionResult.ActionType.SENDERROR, e.getMessage());
			log.warn(DATA_ERROR, e);
		}

		if (result == null) {
			request.setAttribute(StringConstant.ATTRIBUTE_USERS, users);
			Long pages = userCount / PAGINATION + (userCount % PAGINATION == 0 ? 0 : 1);
			request.setAttribute(StringConstant.ATTRIBUTE_PAGE_COUNT, pages);
			request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.playerslist"));
			result = new ActionResult(ActionResult.ActionType.FORWARD, JSPPathManager.getProperty("page.admin"));
		}

		return result;
	}

}
