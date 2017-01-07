package org.qqq175.blackjack.persistence.dao;

public interface DAOFactory {
	AccountOperationDAO getAccountOperationDAO();

	GameDAO getGameDAO();

	MessageDAO getMesssageDAO();

	UserDAO getUserDAO();

	UserstatDAO getUserstatDAO();
}
