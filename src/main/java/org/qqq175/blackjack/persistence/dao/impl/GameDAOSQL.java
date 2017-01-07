package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.dao.GameDAO;
import org.qqq175.blackjack.persistence.entity.Game;
import org.qqq175.blackjack.persistence.entity.id.GameId;

public class GameDAOSQL extends EntityDAOSQL<Game, GameId> implements GameDAO {
	private static final String TABLE_NAME = "game";
	private static final int COLUMN_COUNT = 3;

	public GameDAOSQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Game entity) throws SQLException, UnsupportedOperation {
		// TODO Auto-generated method stub

	}

	@Override
	protected Game fillEntity(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
