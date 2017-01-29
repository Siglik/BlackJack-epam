package org.qqq175.blackjack.logic.game;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.game.GameStage;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.GameDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.Game;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.pool.GamePool;

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

	public Set<BlackJackGame> getJoinableGameList() {
		Collection<BlackJackGame> allGames = GamePool.getInstance().getGamesList();
		return allGames.stream()
				.filter(
						(game) -> 
						game.getFreeSlots() > 0 && game.getGameStage() != GameStage.UNACTIVE)
				.collect(Collectors.toSet());
		
	}

}
