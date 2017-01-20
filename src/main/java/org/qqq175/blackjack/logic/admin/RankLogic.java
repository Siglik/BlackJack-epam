package org.qqq175.blackjack.logic.admin;

import org.json.simple.JSONObject;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class RankLogic {
	private final static String RESULT_KEY = "result";
	private final static String RESULT_RANK = "rank";
	private final static String RESULT_MESSAGE = "message";

	private enum ResultState {
		OK, WRONG_NUMBER_FORMAT, DAO_ERROR, NOT_FOUND, MISSING_PARAMETER, SELF_FORBIDDEN, NOT_ALLOWED;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toogleRank(String id, User curUser) {
		JSONObject result = new JSONObject();

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

					if (!curUser.getId().equals(userId)) {
						if (curUser.isActive() && curUser.getType().equals(User.Type.ADMIN)) {
							boolean updateResult;
							User.Type type = userDAO.findEntityById(userId).getType();
							User.Type newType;
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
							result.put(RESULT_MESSAGE, "Insufficient permissions: banned or isn't ADMIN");
						}
					} else {
						result.put(RESULT_KEY, ResultState.SELF_FORBIDDEN.name());
						result.put(RESULT_MESSAGE, "Self rank change is forbidden!");
					}
				} catch (DAOException e) {
					result.put(RESULT_KEY, ResultState.DAO_ERROR.name());
					result.put(RESULT_MESSAGE, e.getMessage());
					// TODO log
					e.printStackTrace();
				}
			}
		} else {
			result.put(RESULT_KEY, ResultState.MISSING_PARAMETER.name());
			result.put(RESULT_MESSAGE, "Missing: id.");
		}
		return result;
	}

}