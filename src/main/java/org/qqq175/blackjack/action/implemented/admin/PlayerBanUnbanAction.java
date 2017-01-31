package org.qqq175.blackjack.action.implemented.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.admin.BanLogic;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Ban or unban player
 * @author qqq175
 */
public class PlayerBanUnbanAction implements Action {

	/**
	 * avaliable subactions
	 * @author Maksim.Mikhalkou
	 *
	 */
	public enum Mode {
		BAN(true), UNBAN(false);

		private boolean isBan;

		private Mode(boolean isBan) {
			this.isBan = isBan;
		}

		/**
		 * @return the isBan
		 */
		public boolean isBan() {
			return isBan;
		}
	}

	private final Mode MODE;
	
	/**
	 * Counstructor
	 * @param mode - specify command subaction BAN or UNBAN
	 */
	public PlayerBanUnbanAction(Mode mode) {
		MODE = mode;
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter(StringConstant.PARAMETER_ID);
		BanLogic banLogic = new BanLogic();
		ModifyUserLogic muLogic = new ModifyUserLogic();
		muLogic.updateSessionUser(request.getSession());

		User user = (User) request.getSession(false).getAttribute(StringConstant.ATTRIBUTE_USER);
		JSONObject result = banLogic.changeUserBanState(MODE.isBan(), id, user);

		request.setAttribute(StringConstant.ATTRIBUTE_JSON, result);
		return new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);
	}

}
