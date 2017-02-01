package org.qqq175.blackjack.logic.player;

import static org.qqq175.blackjack.StringConstant.PATTERN_DISPLAY_NAME;
import static org.qqq175.blackjack.StringConstant.PATTERN_NAME;
import static org.qqq175.blackjack.StringConstant.PATTERN_PASSWORD;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.logic.main.SecurityLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Containt methos that modify or update user in database, session, pool, etc
 * 
 * @author qqq175
 *
 */
public class ModifyUserLogic {
	private static final String UNABLE_GET_DATA = "Unable to get users data";
	private static final String WRONG_PASSWORD = "Wrong password ";
	private static final String UNABLE_UPDATE_DATA = "Unable to update users data";
	private static final String UNABLE_HASH = "Unable to calc password hash";
	private static Logger log = LogManager.getLogger(ModifyUserLogic.class);

	/**
	 * possible modify user operation results
	 * 
	 * @author qqq175
	 *
	 */
	public enum Result {
		OK("Ok"), WRONG_PASSWORD("Passowrd is incorect!"), UNKNOWN_ERROR("Unkown user modifycation error."), WRONG_DATA("Icorrect data."), NOT_EQUAL(
				"Password and password confirm isn't match.");
		private String message;

		private Result(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	/**
	 * update users personal data in database
	 * 
	 * @param params
	 *            - request params map
	 * @param user
	 * @return
	 */
	public Result changePersonal(Map<String, String[]> params, User user) {
		Result result = null;
		String firstName = extractParameter(StringConstant.PARAMETER_FIRST_NAME, params);
		String lastName = extractParameter(StringConstant.PARAMETER_LAST_NAME, params);
		String displayName = extractParameter(StringConstant.PARAMETER_DISPLAY_NAME, params);
		if (firstName != null && lastName != null) {
			if (displayName == null || displayName.isEmpty()) {
				displayName = firstName + "." + lastName;
			}
			result = setNewPerlsonal(user, firstName, lastName, displayName);
		} else {
			result = Result.WRONG_DATA;
		}

		return result;
	}

	/**
	 * update users password in database
	 * 
	 * @param params
	 *            - request params map
	 * @param user
	 * @return
	 */
	public Result changePassowrd(Map<String, String[]> params, User user) {
		Result result = null;
		String oldPassword = extractParameter(StringConstant.PARAMETER_PASSWORD_OLD, params);
		String newPassword = extractParameter(StringConstant.PARAMETER_PASSWORD_NEW, params);
		String repeatPassword = extractParameter(StringConstant.PARAMETER_PASSWORD_REPEAT, params);
		if (oldPassword != null && newPassword != null && repeatPassword != null) {
			result = updateNewPassword(user, newPassword, repeatPassword, oldPassword);
		} else {
			result = Result.WRONG_DATA;
		}

		return result;
	}

	/**
	 * update current session user from DB
	 * 
	 * @param session
	 * @return
	 */
	public boolean updateSessionUser(HttpSession session) {
		boolean result = false;
		User user = (User) session.getAttribute(StringConstant.ATTRIBUTE_USER);
		if (user != null) {
			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			UserDAO userDAO = daoFactory.getUserDAO();
			try {
				User updatedUser = userDAO.findEntityById(user.getId());
				session.setAttribute(StringConstant.ATTRIBUTE_USER, updatedUser);
				result = true;
			} catch (DAOException e) {
				result = false;
				log.error(UNABLE_GET_DATA, e);
			}
		}

		return result;
	}

	/**
	 * update user in ool from DB if users pool contains user, else do nothing
	 * 
	 * @param userId
	 * @return
	 */
	public boolean updateUserInPool(UserId userId) {
		boolean result = false;
		if (UserPool.getInstance().containsKey(userId)) {
			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			UserDAO userDAO = daoFactory.getUserDAO();
			try {
				User updatedUser = userDAO.findEntityById(userId);
				User returned = UserPool.getInstance().replace(updatedUser);
				log.debug(
						"new active? " + updatedUser.isActive() + " pool active? " + returned.isActive() + " equal? " + updatedUser.equals(returned));
				result = true;
			} catch (DAOException e) {
				result = false;
				log.error(UNABLE_GET_DATA, e);
			}
		}

		return result;
	}

	/**
	 * update users password in database
	 * 
	 * @param user
	 * @param newPassword
	 * @param repeatPassword
	 * @param oldPassword
	 * @return
	 */
	public Result updateNewPassword(User user, String newPassword, String repeatPassword, String oldPassword) {
		Result result = null;
		UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
		SecurityLogic sLogic = new SecurityLogic();
		String oldPassHash = null;

		try {
			oldPassHash = sLogic.calcSHA(sLogic.pepperPassword(sLogic.saltPassword(oldPassword), user.getEmail()));
		} catch (LogicException e1) {
			result = Result.UNKNOWN_ERROR;
			log.error(UNABLE_HASH, e1);
		}

		if (result == null) {
			try {
				User updatedUser = userDAO.findEntityById(user.getId());
				if (updatedUser.getPassword().equals(oldPassHash)) {
					result = this.validatePasswords(newPassword, repeatPassword);
					if (result == Result.OK) {
						String oldPassSalted = sLogic.pepperPassword(sLogic.saltPassword(oldPassword), updatedUser.getEmail());
						String newPassSalted = sLogic.pepperPassword(sLogic.saltPassword(newPassword), updatedUser.getEmail());
						userDAO.updatePassword(updatedUser.getId(), oldPassSalted, newPassSalted);
					}
				} else {
					result = Result.WRONG_PASSWORD;
					log.warn(WRONG_PASSWORD + user.getId().getValue());
				}
			} catch (DAOException e) {
				log.error(UNABLE_UPDATE_DATA, e);
				result = Result.UNKNOWN_ERROR;
			}
		}

		return result;
	}

	/**
	 * update users personal data in database
	 * 
	 * @param user
	 * @param firstName
	 * @param lastName
	 * @param displayName
	 * @return
	 */
	public Result setNewPerlsonal(User user, String firstName, String lastName, String displayName) {
		Result result = null;
		UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();

		result = validatePersonal(firstName, lastName, displayName);

		if (result == Result.OK) {
			try {
				userDAO.updatePersonal(user.getId(), firstName, lastName, displayName);
			} catch (DAOException e) {
				result = Result.UNKNOWN_ERROR;
				log.error(UNABLE_UPDATE_DATA, e);
			}
		}

		return result;
	}

	/**
	 * extracts parameter from request params map
	 * 
	 * @param parameter
	 * @param params
	 * @return parameter value as string or null if not found
	 */
	private String extractParameter(String parameter, Map<String, String[]> params) {
		String value = null;
		if (params.containsKey(parameter)) {
			String[] values = params.get(parameter);
			if (values.length == 1) {
				if (!values[0].isEmpty()) {
					value = values[0];
				}
			}
		}

		return value;
	}

	/**
	 * validate personal data
	 * 
	 * @param firstName
	 * @param lastName
	 * @param displayName
	 * @return
	 */
	private Result validatePersonal(String firstName, String lastName, String displayName) {
		Result result = null;

		if (validateParameter(firstName, PATTERN_NAME) && validateParameter(lastName, PATTERN_NAME)
				&& validateParameter(displayName, PATTERN_DISPLAY_NAME)) {
			result = Result.OK;
		} else {
			result = Result.WRONG_DATA;
		}

		return result;
	}

	/**
	 * validate passwords using
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	private Result validatePasswords(String password, String repeatPassword) {
		Result result = Result.OK;

		Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			result = Result.WRONG_DATA;
		}
		if (result == Result.OK) {
			if (!repeatPassword.equals(password)) {
				result = Result.NOT_EQUAL;
			}
		}

		return result;
	}

	/**
	 * validate parameter using regex
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	private boolean validateParameter(String value, String pattern) {
		Pattern ptn = Pattern.compile(pattern);
		Matcher matcher = ptn.matcher(value);

		return matcher.matches();
	}
}
