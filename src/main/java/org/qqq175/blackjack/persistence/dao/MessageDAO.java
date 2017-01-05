package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;

public interface MessageDAO extends EntityDAO<Message, MessageId> {
	List<Message> getGameMessages(GameId gameId);
}