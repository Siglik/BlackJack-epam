package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.MessageDAO;
import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * MessageDAO implementation for MYSQL DB
 * 
 * @author qqq175
 *
 */
public class MessageDAOImpl extends EntityDAOImpl<Message, MessageId> implements MessageDAO {
	private static final String TABLE_NAME = "message";
	private static final int COLUMN_COUNT = 5;

	private static final String EXCEPTION_MESSAGE_PREPARE = "Exception in " + MessageDAOImpl.class.getName() + " at preparing query.";
	private static final String EXCEPTION_MESSAGE_FILL = "Exception in " + MessageDAOImpl.class.getName() + " at reading query result.";

	public MessageDAOImpl() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	public List<Message> findGameMessages(GameId gameId) throws DAOException {
		String query = sqlQuery.getQuery("sql.message.find.byGame");

		return this.findMany(query, (ps) -> {
			ps.setLong(1, gameId.getValue());
		});
	}

	@Override
	public List<Message> findUserMessages(UserId userId) throws DAOException {
		String query = sqlQuery.getQuery("sql.message.find.byUser");

		return this.findMany(query, (ps) -> {
			ps.setLong(1, userId.getValue());
		});
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Message entity) throws DAOException {
		try {
			prepStatment.setLong(1, entity.getGameId().getValue());
			prepStatment.setLong(2, entity.getUserId().getValue());
			prepStatment.setString(3, entity.getText());
			prepStatment.setString(4, this.dateToString(entity.getTime()));
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_PREPARE, e);
		}
	}

	@Override
	protected Message fillEntity(ResultSet resultSet) throws DAOException {
		Message message = new Message();
		try {
			message.setId(new MessageId(resultSet.getLong(1)));
			message.setGameId(new GameId(resultSet.getLong(2)));
			message.setUserId(new UserId(resultSet.getLong(3)));
			message.setText(resultSet.getString(4));
			message.setTime(this.stringToDate(resultSet.getString(5)));
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_FILL, e);
		}
		return message;
	}

	@Override
	protected MessageId makeId(long id) {
		return new MessageId(id);
	}

}
