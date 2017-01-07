package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class UserstatDAOSQL extends EntityDAOSQL<Userstat, UserId> implements UserstatDAO {
	private static final String TABLE_NAME = "userstat";
	private static final int COLUMN_COUNT = 6;

	public UserstatDAOSQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, Userstat entity) throws SQLException, UnsupportedOperation {
		// TODO Auto-generated method stub

	}

	@Override
	protected Userstat fillEntity(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
