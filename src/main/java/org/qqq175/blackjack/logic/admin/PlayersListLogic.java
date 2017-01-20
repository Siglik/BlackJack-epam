package org.qqq175.blackjack.logic.admin;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;

public class PlayersListLogic {

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
