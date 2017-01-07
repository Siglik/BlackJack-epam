package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.dao.AccountOperationDAO;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;

public class AccountOperationDAOSQL extends EntityDAOSQL<AccountOperation, AccountOperationId> implements AccountOperationDAO {
	private static final String TABLE_NAME = "account_operation";
	private static final int COLUMN_COUNT = 6;

	public AccountOperationDAOSQL() {
		super(TABLE_NAME, COLUMN_COUNT);
	}

	@Override
	protected void prepareWithEntity(PreparedStatement prepStatment, AccountOperation entity) throws SQLException, UnsupportedOperation {
		// TODO Auto-generated method stub

	}

	@Override
	protected AccountOperation fillEntity(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
