package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Interface that contains messages DAO operations
 * 
 * @author qqq175
 *
 */
public interface MessageDAO extends EntityDAO<Message, MessageId> {
	/**
	 * find all game messages
	 * 
	 * @param gameId
	 * @return
	 * @throws DAOException
	 */
	List<Message> findGameMessages(GameId gameId) throws DAOException;

	/**
	 * find all users messages
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	List<Message> findUserMessages(UserId userId) throws DAOException;
}