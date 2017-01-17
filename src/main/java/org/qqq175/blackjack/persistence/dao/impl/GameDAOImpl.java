package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.GameDAO;
import org.qqq175.blackjack.persistence.entity.Game;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class GameDAOImpl extends EntityDAOImpl<Game, GameId> implements GameDAO {
	private static final String TABLE_NAME = "game";
	private static final int COLUMN_COUNT = 3;

	private static final String EXCEPTION_MESSAGE_PREPARE = "Exception in " + GameDAOImpl.class.getName() + " at preparing query.";
	private static final String EXCEPTION_MESSAGE_FILL = "Exception in " + GameDAOImpl.class.getName() + " at reading query result.";

	public GameDAOImpl() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Game entity) throws DAOException {
		// INSERT INTO game (user_id) VALUES (?)
		try {
			prepStatment.setLong(1, entity.getId().getValue());
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_PREPARE, e);
		}
	}

	@Override
	protected Game fillEntity(ResultSet resultSet) throws DAOException {
		Game game = new Game();
		try {
			game.setId(new GameId(resultSet.getLong(1)));
			game.setCreationTime(this.stringToDate(resultSet.getString(2)));
			game.setUserId(new UserId(resultSet.getLong(3)));
		} catch (SQLException e) {
			throw new DAOException(EXCEPTION_MESSAGE_FILL, e);
		}
		return game;
	}

	@Override
	public List<Game> findGamesByUser(UserId userId) throws DAOException {
		String query = sqlQuery.getQuery("sql.game.find.byUser");
		return this.findMany(query, (ps) -> {
			ps.setLong(1, userId.getValue());
		});
	}

	@Override
	protected GameId makeId(long id) {
		return new GameId(id);
	}

}
