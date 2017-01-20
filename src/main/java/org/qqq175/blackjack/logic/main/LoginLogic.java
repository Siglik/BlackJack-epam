/**
 * 
 */
package org.qqq175.blackjack.logic.main;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

/**
 * @author qqq175
 *
 */
public class LoginLogic {
	private static Logger log = LogManager.getLogger(LoginLogic.class);

	public enum Result {
		OK, NOTFOUND, UNACTIVE, INUSE;
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
				log.debug(
						UserPool.getInstance().containsKey(user.getId()) + " (" + UserPool.getInstance().size() + ") id: " + user.getId().getValue());
				if (!UserPool.getInstance().containsKey(user.getId())) {
					session.setAttribute(StringConstant.ATTRIBUTE_USER, user);
					UserPool.getInstance().put(user);
					return Result.OK;
				} else {
					return Result.INUSE;
				}
			} else {
				return Result.UNACTIVE;
			}
		} else {
			return Result.NOTFOUND;
		}
	}
}
