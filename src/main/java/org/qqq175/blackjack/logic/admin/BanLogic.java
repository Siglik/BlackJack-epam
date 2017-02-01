package org.qqq175.blackjack.logic.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Containts methods that operate user ban status
 * 
 * @author qqq175
 *
 */
public class BanLogic {
	private static final String MISSING_ID = "Missing: id.";
	private static final String ERROR_ON_CHANGE_STATE = "Error on  change user ban/unban state.";
	private static final String SELF_BAN_UNBAN = "Self ban/unban is forbidden!";
	private static final String NOT_ALLOWED_BAN_MESSAGE = "Insufficient permissions: banned or isn't ADMIN";
	private static Logger log = LogManager.getLogger(BanLogic.class);
	private final static String RESULT_KEY = "result";
	private final static String RESULT_MESSAGE = "message";

	/**
	 * possible operation result states
	 * 
	 * @author qqq175
	 */
	private enum ResultState {
		OK, WRONG_NUMBER_FORMAT, DAO_ERROR, NOT_FOUND, MISSING_PARAMETER, SELF_FORBIDDEN, NOT_ALLOWED
	}

	/**
	 * Ban/unban user and return json object with operation result
	 * 
	 * @param ban
	 * @param id
	 * @param curUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject changeUserBanState(boolean ban, String id, User curUser) {
		JSONObject result = new JSONObject();

		/* check parameter */
		if (id != null && !id.isEmpty()) {
			UserId userId = null;
			try {
				userId = new UserId(Long.parseLong(id));
			} catch (NumberFormatException e) {
				result.put(RESULT_KEY, ResultState.WRONG_NUMBER_FORMAT.name());
				result.put(RESULT_MESSAGE, "id = " + id);
			}
			if (userId != null) {
				DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
				UserDAO userDAO = daoFactory.getUserDAO();
				try {
					/* check self ban */
					if (!curUser.getId().equals(userId)) {
						/* check permission */
						if (curUser.isActive() && curUser.getType().equals(User.Type.ADMIN)) {
							boolean updateResult;

							/* change user state */
							if (ban) {
								updateResult = userDAO.disableUser(userId);
							} else {
								updateResult = userDAO.enableUser(userId);
							}

							if (updateResult) {
								/* update pool */
								ModifyUserLogic muLogic = new ModifyUserLogic();
								muLogic.updateUserInPool(userId);
								result.put(RESULT_KEY, ResultState.OK.name());
							} else {
								result.put(RESULT_KEY, ResultState.NOT_FOUND.name());
								result.put(RESULT_MESSAGE, "User with id = " + id + " isn't found.");
							}
						} else {
							result.put(RESULT_KEY, ResultState.NOT_ALLOWED.name());
							result.put(RESULT_MESSAGE, NOT_ALLOWED_BAN_MESSAGE);
						}
					} else {
						result.put(RESULT_KEY, ResultState.SELF_FORBIDDEN.name());
						result.put(RESULT_MESSAGE, SELF_BAN_UNBAN);
					}
				} catch (DAOException e) {
					result.put(RESULT_KEY, ResultState.DAO_ERROR.name());
					result.put(RESULT_MESSAGE, e.getMessage());
					log.error(ERROR_ON_CHANGE_STATE, e);
					e.printStackTrace();
				}
			}
		} else {
			result.put(RESULT_KEY, ResultState.MISSING_PARAMETER.name());
			result.put(RESULT_MESSAGE, MISSING_ID);
		}
		return result;
	}
}
