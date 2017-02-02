package org.qqq175.blackjack.persistence.dao;

/**
 * DAOFactory interface
 * 
 * @author qqq175
 *
 */
public interface DAOFactory {
	/**
	 * @return account operation DAO
	 */
	AccountOperationDAO getAccountOperationDAO();

	/**
	 * @return game DAO
	 */
	GameDAO getGameDAO();

	/**
	 * @return messages DAO
	 */
	MessageDAO getMesssageDAO();

	/**
	 * @return user DAO
	 */
	UserDAO getUserDAO();

	/**
	 * @return user statistic DAO
	 */
	UserstatDAO getUserstatDAO();
}
