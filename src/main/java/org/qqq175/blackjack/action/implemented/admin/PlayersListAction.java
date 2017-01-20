package org.qqq175.blackjack.action.implemented.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.admin.PlayersListLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.entity.User;

public class PlayersListAction implements Action {
	private static final int PAGINATION = 7;

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;

		PlayersListLogic plLogic = new PlayersListLogic();
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

		List<User> users = null;
		Long userCount = null;
		try {
			users = plLogic.findUsers(pageNumber, PAGINATION);
			userCount = plLogic.conntUsers();
		} catch (LogicException e) {
			result = new ActionResult(ActionResult.ActionType.SENDERROR, e.getMessage());
			// LOG
			e.printStackTrace();
		}

		if (result == null) {
			request.setAttribute(StringConstant.ATTRIBUTE_USERS, users);
			Long pages = userCount / PAGINATION + 1;
			request.setAttribute(StringConstant.ATTRIBUTE_PAGE_COUNT, pages);
			request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.playerslist"));
			result = new ActionResult(ActionResult.ActionType.FORWARD, JSPPathManager.getProperty("page.admin"));
		}

		return result;
	}

}
