package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.qqq175.blackjack.exception.EntityNotFoundException;
import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.connection.ConnectionPool;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.dao.EntityDAO;
import org.qqq175.blackjack.persistence.entity.Entity;
import org.qqq175.blackjack.persistence.entity.id.EntityId;
import org.qqq175.blackjack.util.SQLQueryManager;

public abstract class EntityDAOMySQL<T extends Entity<I>, I extends EntityId> implements EntityDAO<T, I> {
	private String tableName;
	private int colCount;
	protected ConnectionPool connPool;
	protected SQLQueryManager sqlQuery;

	public EntityDAOMySQL(String tableName, int colCount) {
		this.tableName = tableName;
		this.colCount = colCount;
		this.sqlQuery = SQLQueryManager.getInstance();
		this.connPool = ConnectionPool.getInstance();
	}

	@Override
	public boolean create(T entity) throws SQLException, UnsupportedOperation {
		String query = sqlQuery.getQuery("sql." + tableName + ".insert");
		try (ConnectionWrapper connection = connPool.retrieve()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithEntity(prepStatment, entity);
				int affectedRows = prepStatment.executeUpdate();
				if (affectedRows > 0)
					return true;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<T> findAll() throws SQLException {
		String query = sqlQuery.getQuery("sql.entity.findAll");
		query = insertTableName(query);
		try (ConnectionWrapper connection = connPool.retrieve()) {
		try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
			try (ResultSet resultSet = prepStatment.executeQuery()) {
				return toDTOList(resultSet);
			}
		}} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new SQLException();
		}
	}

	@Override
	public T findEntityById(I id) throws SQLException, EntityNotFoundException {
		String query = sqlQuery.getQuery("sql.entity.findBy.id");
		query = insertTableName(query);
		try (ConnectionWrapper connection = connPool.retrieve()) {
		try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
			prepareWithId(prepStatment, id);
			try (ResultSet resultSet = prepStatment.executeQuery()) {
				return toDTO(resultSet);
			}
		}} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new SQLException();
		}
	}

	@Override
	public boolean update(T entity) throws SQLException, UnsupportedOperation {
		String query = sqlQuery.getQuery("sql." + tableName + ".update");
		try (ConnectionWrapper connection = connPool.retrieve()) {
		try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
			prepareWithEntity(prepStatment, entity);
			prepStatment.setLong(colCount, entity.getId().getValue());
			int affectedRows = prepStatment.executeUpdate();
			if (affectedRows > 0)
				return true;
		}} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new SQLException();
		}
		return false;
	}

	@Override
	public boolean delete(I id) throws SQLException {
		String query = sqlQuery.getQuery("sql.entity.deleteBy.id");
		query = insertTableName(query);
		try (ConnectionWrapper connection = connPool.retrieve()) {
		try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
			prepareWithId(prepStatment, id);

			return prepStatment.executeUpdate() == 1;
		}} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new SQLException();
		}
	}

	@Override
	public boolean delete(T entity) throws SQLException {
		return delete(entity.getId());
	}

	private void prepareWithId(PreparedStatement prepStatment, I id) throws SQLException {
		prepStatment.setLong(1, id.getValue());
	}

	private String insertTableName(String query) {
		return query.replaceFirst("\\{tableName\\}", tableName);
	}

	protected abstract void prepareWithEntity(PreparedStatement prepStatment, T entity)
			throws SQLException, UnsupportedOperation;

	protected T toDTO(ResultSet resultSet) throws EntityNotFoundException, SQLException {
		if (resultSet.next()) {
			return fillEntity(resultSet);
		} else {
			throw new EntityNotFoundException("MESSAGE");
		}
	}

	protected List<T> toDTOList(ResultSet resultSet) throws SQLException {
		List<T> resultList = new ArrayList<>();
		while (resultSet.next()) {
			resultList.add(fillEntity(resultSet));
		}
		return resultList;
	}

	protected abstract T fillEntity(ResultSet resultSet) throws SQLException;

	/**
	 * @return the tableName
	 */
	protected String getTableName() {
		return tableName;
	}

	/**
	 * @return the sqlQuery
	 */
	protected SQLQueryManager getSqlQuery() {
		return sqlQuery;
	}

	// protected <R> R doQuerry(Function<String, R> func, String query){
	// try (ConnectionWrapper connection = connPool.retrieve()){
	// try (PreparedStatement prepStatment = connection.prepareStatement(query))
	// {
	// prepareWithEntity(prepStatment, entity);
	// int affectedRows = prepStatment.executeUpdate();
	// if (affectedRows > 0)
	// return true;
	// }} catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return false;
	// return func.apply(query);
	// }
}