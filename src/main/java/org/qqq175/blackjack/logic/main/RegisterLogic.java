package org.qqq175.blackjack.logic.main;

import static org.qqq175.blackjack.StringConstant.PARAMETER_DISPLAY_NAME;
import static org.qqq175.blackjack.StringConstant.PARAMETER_EMAIL;
import static org.qqq175.blackjack.StringConstant.PARAMETER_FIRST_NAME;
import static org.qqq175.blackjack.StringConstant.PARAMETER_LAST_NAME;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PASSWORD;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PASSWORD_REPEAT;
import static org.qqq175.blackjack.StringConstant.PATTERN_DISPLAY_NAME;
import static org.qqq175.blackjack.StringConstant.PATTERN_EMAIL;
import static org.qqq175.blackjack.StringConstant.PATTERN_NAME;
import static org.qqq175.blackjack.StringConstant.PATTERN_PASSWORD;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class RegisterLogic {
	public enum Result {
		OK("Ok"), ALREADY_EXIST("This email is already used!"), REGISTRATION_ERROR("Unkown registration error."), WRONG_DATA(
				"Please enter valid data.");
		private String message;

		private Result(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public Result registerUser(Map<String, String[]> params, Part part) {
		Result result;
		UserId userId = null;
		logParams(params);
		if (isValid(params)) {
			try {
				if (isEmailAvaliable(params.get(PARAMETER_EMAIL)[0])) {
					User user = createNewUser(params);
					try {
						userId = saveUserInDatabase(user);
						// registration is successful
						result = Result.OK;
					} catch (DAOException e) {
						result = Result.REGISTRATION_ERROR;
						// TODO log
						e.printStackTrace();
					}
					// try to save photo, if unable - register anyway
					if (userId != null && part != null) {
						FileUploader uploader = new FileUploader();
						uploader.uploadPhoto(part, userId);
					}
				} else {
					result = Result.ALREADY_EXIST;
				}
			} catch (DAOException e) {
				result = Result.REGISTRATION_ERROR;
				// TODO log
				e.printStackTrace();
			}
		} else {
			result = Result.WRONG_DATA;
		}

		return result;
	};

	private boolean isEmailAvaliable(String email) throws DAOException {
		User user = Settings.getInstance().getDaoFactory().getUserDAO().findUser(email);
		return user == null;
	}

	private UserId saveUserInDatabase(User user) throws UnsupportedOperationException, DAOException {

		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		UserId userId;

		userId = daoFactory.getUserDAO().create(user);

		Userstat userstat = new Userstat();
		userstat.setId(userId);
		daoFactory.getUserstatDAO().create(userstat);

		return userId;
	}

	private User createNewUser(Map<String, String[]> params) {
		User user = new User();

		user.setEmail(params.get(PARAMETER_EMAIL)[0]);

		SecurityLogic sLogic = new SecurityLogic();
		String password = params.get(PARAMETER_PASSWORD)[0];
		String securedPassword = sLogic.pepperPassword(sLogic.saltPassword(password), user.getEmail());
		user.setPassword(securedPassword);

		user.setFirstName(params.get(PARAMETER_FIRST_NAME)[0]);
		user.setLastName(params.get(PARAMETER_LAST_NAME)[0]);

		String displayName;
		if (params.containsKey(PARAMETER_DISPLAY_NAME) && !params.get(PARAMETER_DISPLAY_NAME)[0].isEmpty()) {
			displayName = params.get(PARAMETER_DISPLAY_NAME)[0];
		} else {
			displayName = params.get(PARAMETER_FIRST_NAME)[0] + "." + params.get(PARAMETER_LAST_NAME)[0].charAt(0);
		}
		user.setDisplayName(displayName);

		user.setType(User.Type.PLAYER);
		user.setActive(true);

		return user;
	}

	private boolean isValid(Map<String, String[]> params) {
		return validatePasswords(params) && validateParameter(params, PARAMETER_EMAIL, PATTERN_EMAIL, true)
				&& validateParameter(params, PARAMETER_FIRST_NAME, PATTERN_NAME, true)
				&& validateParameter(params, PARAMETER_LAST_NAME, PATTERN_NAME, true)
				&& validateParameter(params, PARAMETER_DISPLAY_NAME, PATTERN_DISPLAY_NAME, false);
	}

	private void logParams(Map<String, String[]> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n{");
		for (String key : params.keySet()) {
			sb.append(key + "=");
			sb.append(Arrays.toString(params.get(key)));
			sb.append(", ");
		}
		sb.append("}\n");
	}

	private boolean validateParameter(Map<String, String[]> params, String parameter, String pattern, boolean isRequired) {
		boolean isValid = false;
		if (params.containsKey(parameter)) {
			String[] values = params.get(parameter);
			if (values.length == 1) {
				if (!values[0].isEmpty()) {
					Pattern ptn = Pattern.compile(pattern);
					Matcher matcher = ptn.matcher(values[0]);
					isValid = matcher.matches();
				} else {
					isValid = !isRequired;
				}
			} else {
				isValid = !isRequired;
			}
		} else {
			isValid = !isRequired;
		}

		if (!isValid) {
			System.out.print(parameter + " is not valid");
		}
		return isValid;
	}

	private boolean validatePasswords(Map<String, String[]> params) {
		boolean isPassValid = false, isValid = false;
		String password = null;

		if (params.containsKey(PARAMETER_PASSWORD)) {
			String[] values = params.get(PARAMETER_PASSWORD);

			if (values.length == 1) {
				Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
				password = values[0];
				Matcher matcher = pattern.matcher(password);
				isPassValid = matcher.matches();
			}
		}

		if (isPassValid) {
			if (params.containsKey(PARAMETER_PASSWORD_REPEAT)) {
				String[] values = params.get(PARAMETER_PASSWORD_REPEAT);
				if (values.length == 1) {
					String passRepeat = values[0];
					isValid = passRepeat.equals(password);
				}
			}
		}

		return isValid;
	}
}
