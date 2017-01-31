package org.qqq175.blackjack.service.implemented.player;

import static org.qqq175.blackjack.service.ActionResult.ActionType.FORWARD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.main.IndexLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * show user's statictic
 * 
 * @author qqq175
 *
 */
public class ShowStatsAction implements Action {
	private static final String CANT_GET_STATS = "Cant get user's stats";
	private static Logger log = LogManager.getLogger(ShowStatsAction.class);

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession(false).getAttribute(StringConstant.ATTRIBUTE_USER);
		Userstat userstat;

		try {
			userstat = Settings.getInstance().getDaoFactory().getUserstatDAO().findEntityById(user.getId());
		} catch (DAOException e) {
			log.error(CANT_GET_STATS, e);
			// show empty
			userstat = new Userstat();
		}
		request.setAttribute(StringConstant.ATTRIBUTE_USER, user);
		request.setAttribute(StringConstant.ATTRIBUTE_USERSTAT, userstat);
		request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.stats"));
		IndexLogic logic = new IndexLogic();

		return new ActionResult(FORWARD, logic.definePathByUser(user));
	}

}
