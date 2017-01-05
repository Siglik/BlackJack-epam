package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.EntityNotFoundException;
import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class UserDAOMySQL extends EntityDAOMySQL<User, UserId> implements UserDAO {
	private static final String TABLE_NAME = "city";
	private static final int COLUMN_COUNT = 6;

	public UserDAOMySQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, User entity)
			throws SQLException, UnsupportedOperation {
		// TODO Auto-generated method stub
	}

	@Override
	protected User fillEntity(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUser(String login, String password) throws SQLException, EntityNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
