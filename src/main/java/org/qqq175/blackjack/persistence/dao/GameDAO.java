package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Game;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Interface that contains game DAO operations
 * 
 * @author qqq175
 *
 */
public interface GameDAO extends EntityDAO<Game, GameId> {
	/**
	 * find all games created by user
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	List<Game> findGamesByUser(UserId userId) throws DAOException;
}
