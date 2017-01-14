package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public interface MessageDAO extends EntityDAO<Message, MessageId> {
	List<Message> findGameMessages(GameId gameId) throws DAOException;

	List<Message> findUserMessages(UserId userId) throws DAOException;
}