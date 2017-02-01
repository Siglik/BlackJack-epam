package org.qqq175.blackjack.logic.admin;

import org.json.simple.JSONObject;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

**
* COntaints methods that operate user rank
* @author qqq175
*
*/
public class RankLogic {
	private static final String SELF_RANK_CHANGE = "Self rank change is forbidden!";
	private static final String NOT_ALLOWED_MESSAGE = "Insufficient permissions: banned or isn't ADMIN";
	private static final String ERROR_ON_CHANGE_RANK = "Error on  change user rank.";
	private static Logger log = LogManager.getLogger(BanLogic.class);
	private static final String MISSING_ID = "Missing: id.";
	private final static String RESULT_KEY = "result";
	private final static String RESULT_RANK = "rank";
	private final static String RESULT_MESSAGE = "message";

	/**
	 * possible operation result states
	 * @author qqq175
	 */
	private enum ResultState {
		OK, WRONG_NUMBER_FORMAT, DAO_ERROR, NOT_FOUND, MISSING_PARAMETER, SELF_FORBIDDEN, NOT_ALLOWED;
	}

	/**
	 * change user state and return json object with operation result
	 * @param ban
	 * @param id
	 * @param curUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toogleRank(String id, User curUser) {
		JSONObject result = new JSONObject();
		
		/*check parameter*/
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
					
					/* check self modification */
					if (!curUser.getId().equals(userId)) {
						/* check permission */
						if (curUser.isActive() && curUser.getType().equals(User.Type.ADMIN)) {
							boolean updateResult;
							
							/* get user rank */
							User.Type type = userDAO.findEntityById(userId).getType();
							User.Type newType;
							
							/* change user rank */
							if (type == User.Type.ADMIN) {
								newType = User.Type.PLAYER;
							} else {
								newType = User.Type.ADMIN;
							}
							updateResult = userDAO.updateType(userId, newType);
							if (updateResult) {
								ModifyUserLogic muLogic = new ModifyUserLogic();
								muLogic.updateUserInPool(userId);
								result.put(RESULT_KEY, ResultState.OK.name());
								result.put(RESULT_RANK, newType.name());
							} else {
								result.put(RESULT_KEY, ResultState.NOT_FOUND.name());
								result.put(RESULT_MESSAGE, "User with id = " + id + " isn't found.");
							}
						} else {
							result.put(RESULT_KEY, ResultState.NOT_ALLOWED.name());
							result.put(RESULT_MESSAGE, NOT_ALLOWED_MESSAGE);
						}
					} else {
						result.put(RESULT_KEY, ResultState.SELF_FORBIDDEN.name());
						result.put(RESULT_MESSAGE, SELF_RANK_CHANGE);
					}
				} catch (DAOException e) {
					result.put(RESULT_KEY, ResultState.DAO_ERROR.name());
					result.put(RESULT_MESSAGE, e.getMessage());
					log.error(ERROR_ON_CHANGE_RANK, e);
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
