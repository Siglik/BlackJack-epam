/**
 * 
 */
package org.qqq175.blackjack.logic.main;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * @author qqq175
 *
 */
public class LoginLogic {

	public enum Result {
		OK, NOTFOUND, UNACTIVE;
	}

	/**
	 * If user and pass is right return true, else return false
	 * 
	 * @param login
	 * @param passwordHash
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public Result loginUser(String email, String password, HttpSession session) {
		SecurityLogic sLogic = new SecurityLogic();
		String saltedPassword = sLogic.pepperPassword(sLogic.saltPassword(password), email);
		UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
		User user = null;
		try {
			user = userDAO.findUser(email, saltedPassword);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (user != null) {
			if (user.isActive()) {
				session.setAttribute(StringConstant.ATTRIBUTE_USER, user);
				return Result.OK;
			} else {
				return Result.UNACTIVE;
			}
		} else {
			return Result.NOTFOUND;
		}
	}
}
