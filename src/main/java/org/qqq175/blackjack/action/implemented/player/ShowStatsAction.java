package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.Userstat;

public class ShowStatsAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession(false).getAttribute(StringConstant.ATTRIBUTE_USER);
		Userstat userstat;
		System.out.println(user.getId() + user.getDisplayName() + user.getRegistred());
		try {
			userstat = Settings.getInstance().getDaoFactory().getUserstatDAO().findEntityById(user.getId());
		} catch (DAOException e) {
			// TODO LOG
			e.printStackTrace();
			userstat = new Userstat();
		}
		request.setAttribute(StringConstant.ATTRIBUTE_USER, user);
		request.setAttribute(StringConstant.ATTRIBUTE_USERSTAT, userstat);
		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.stats"));
		IndexLogic logic = new IndexLogic();

		return new ActionResult(FORWARD, logic.definePathByUser(user));
	}

}
