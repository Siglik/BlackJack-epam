package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class UserstatDAOSQL extends EntityDAOImpl<Userstat, UserId> implements UserstatDAO {
	private static final String TABLE_NAME = "userstat";
	private static final int COLUMN_COUNT = 5;

	private static final String EXCEPTION_MESSAGE_PREPARE = "Exception in " + UserstatDAOSQL.class.getName() + " at preparing query.";
	private static final String EXCEPTION_MESSAGE_FILL = "Exception in " + UserstatDAOSQL.class.getName() + " at reading query result.";

	public UserstatDAOSQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	public UserId create(Userstat entity) throws DAOException, UnsupportedOperationException {
		String query = sqlQuery.getQuery("sql.userstat.insert");
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithEntity(prepStatment, entity);
				int affectedRows = prepStatment.executeUpdate();
				if (affectedRows > 0) {
					return entity.getId();
				} else {
					throw new DAOException("Unable to insert: 0 rows are affected.");
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Userstat entity) throws UnsupportedOperationException, DAOException {
		try {
			prepStatment.setLong(1, entity.getId().getValue());
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_PREPARE, e);
		}
	}

	@Override
	public boolean incrementWin(UserId userId) throws DAOException {
		return this.doIncrement("sql.userstat.increment.win", userId);
	}

	@Override
	public boolean incrementBlackjack(UserId userId) throws DAOException {
		return this.doIncrement("sql.userstat.increment.blackjack", userId);
	}

	@Override
	public boolean incrementTie(UserId userId) throws DAOException {
		return this.doIncrement("sql.userstat.increment.tie", userId);
	}

	@Override
	public boolean incrementLoss(UserId userId) throws DAOException {
		return this.doIncrement("sql.userstat.increment.loss", userId);
	}

	@Override
	public boolean update(Userstat entity) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("For update userstat use increment methods of UserstatDAO");
	}

	private boolean doIncrement(String query, UserId userId) throws DAOException {
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(getSqlQuery().getQuery(query))) {
				prepStatment.setLong(1, userId.getValue());
				return prepStatment.executeUpdate() == 1;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	protected Userstat fillEntity(ResultSet resultSet) throws SQLException, DAOException {
		Userstat userstat = new Userstat();
		try {
			userstat.setId(new UserId(resultSet.getLong(1)));
			userstat.setWin(resultSet.getInt(2));
			userstat.setBlackjack(resultSet.getInt(3));
			userstat.setLoss(resultSet.getInt(4));
			userstat.setTie(resultSet.getInt(5));
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_FILL, e);
		}
		return userstat;
	}

	@Override
	protected UserId makeId(long id) {
		return new UserId(id);
	}
}
