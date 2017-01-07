package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.dao.MessageDAO;
import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;

public class MessageDAOSQL extends EntityDAOSQL<Message, MessageId> implements MessageDAO {
	private static final String TABLE_NAME = "message";
	private static final int COLUMN_COUNT = 6;

	public MessageDAOSQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	public List<Message> getGameMessages(GameId gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Message entity) throws SQLException, UnsupportedOperation {
		// TODO Auto-generated method stub

	}

	@Override
	protected Message fillEntity(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
