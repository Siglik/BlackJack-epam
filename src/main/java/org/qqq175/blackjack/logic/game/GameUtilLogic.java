package org.qqq175.blackjack.logic.game;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.GameDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.Game;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;

public class GameUtilLogic {

	public GameId newGameEntity(User user) throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		GameDAO gameDAO = daoFactory.getGameDAO();
		Game gameEntity = new Game();
		gameEntity.setUserId(user.getId());
		GameId gameId;
		try {
			gameId = gameDAO.create(gameEntity);
		} catch (DAOException e) {
			throw new LogicException("Unable to create new game: data persistance error.", e);
		}

		return gameId;
	}

}
