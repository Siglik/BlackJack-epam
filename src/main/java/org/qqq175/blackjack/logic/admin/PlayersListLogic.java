package org.qqq175.blackjack.logic.admin;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
/**
 * Contains methods to get user's list data
 * @author qqq175
 *
 */
public class PlayersListLogic {

	/**
	 * Find in persistence list of user using pagination
	 * @param page
	 * @param usersPerPage
	 * @return
	 * @throws LogicException
	 */
	public List<User> findUsers(int page, int usersPerPage) throws LogicException {
		List<User> result;
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		UserDAO userDAO = daoFactory.getUserDAO();
		int from = (page - 1) * usersPerPage;
		try {
			result = userDAO.findAllLimit(from, usersPerPage);
		} catch (DAOException e) {
			throw new LogicException("Unable to get users list.", e);
		}

		return result;
	}

	/**
	 * return count of users in DB
	 * @return
	 * @throws LogicException
	 */
	public long countUsers() throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		UserDAO userDAO = daoFactory.getUserDAO();

		try {
			return userDAO.countUsers();
		} catch (DAOException e) {
			throw new LogicException("Unable to get users count.", e);
		}
	}

}
