package org.qqq175.blackjack.persistence.dao.impl;

import org.qqq175.blackjack.persistence.dao.AccountOperationDAO;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.EntityDAO;
import org.qqq175.blackjack.persistence.dao.GameDAO;
import org.qqq175.blackjack.persistence.dao.MessageDAO;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;

public class DAOFactoryImpl implements DAOFactory {
	enum DAOType {
		USER(new UserDAOImpl()), USERSTAT(new UserstatDAOImpl()), GAME(new GameDAOImpl()), MESSAGE(
				new MessageDAOImpl()), ACCOUNT_OPERATION(new AccountOperationDAOImpl());

		private EntityDAO<?, ?> dao;

		private DAOType(EntityDAO<?, ?> dao) {
			this.dao = dao;
		}

		public EntityDAO<?, ?> getDAO() {
			return dao;
		}
	}

	@Override
	public AccountOperationDAO getAccountOperationDAO() {
		return (AccountOperationDAO) DAOType.ACCOUNT_OPERATION.getDAO();
	}

	@Override
	public GameDAO getGameDAO() {
		return (GameDAO) DAOType.GAME.getDAO();
	}

	@Override
	public MessageDAO getMesssageDAO() {
		return (MessageDAO) DAOType.MESSAGE.getDAO();
	}

	@Override
	public UserDAO getUserDAO() {
		return (UserDAO) DAOType.USER.getDAO();
	}

	@Override
	public UserstatDAO getUserstatDAO() {
		return (UserstatDAO) DAOType.USERSTAT.getDAO();
	}

}
