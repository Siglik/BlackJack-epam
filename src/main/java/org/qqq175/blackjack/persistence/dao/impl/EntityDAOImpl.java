package org.qqq175.blackjack.persistence.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionPool;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.dao.EntityDAO;
import org.qqq175.blackjack.persistence.dao.util.SQLQueryManager;
import org.qqq175.blackjack.persistence.entity.Entity;
import org.qqq175.blackjack.persistence.entity.id.EntityId;

public abstract class EntityDAOImpl<T extends Entity<I>, I extends EntityId> implements EntityDAO<T, I> {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private String tableName;
	private int colCount;
	protected ConnectionPool connPool;
	protected SQLQueryManager sqlQuery;

	public EntityDAOImpl(String tableName, int colCount) {
		this.tableName = tableName;
		this.colCount = colCount;
		this.sqlQuery = SQLQueryManager.getInstance();
		this.connPool = ConnectionPool.getInstance();
	}

	@Override
	public I create(T entity) throws DAOException {
		String query = sqlQuery.getQuery("sql." + tableName + ".insert");
		System.out.println(query);
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				prepareWithEntity(prepStatment, entity);
				int affectedRows = prepStatment.executeUpdate();
				if (affectedRows > 0) {
					ResultSet indexKeys = prepStatment.getGeneratedKeys();
					if (indexKeys.next()) {
						long id = indexKeys.getLong(1);
						return this.makeId(id);
					} else {
						return null;
					}
				} else {
					throw new DAOException("Unable to insert: 0 rows are affected.");
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<T> findAll() throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.findAll");
		query = prepareQueryString(query);
		return findMany(query, (s) -> {
		});
	}

	@Override
	public T findEntityById(I id) throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.findBy.id");
		query = prepareQueryString(query);
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithId(prepStatment, id);
				try (ResultSet resultSet = prepStatment.executeQuery()) {
					return toDTO(resultSet);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public boolean update(T entity) throws DAOException {
		String query = sqlQuery.getQuery("sql." + tableName + ".update");
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithEntity(prepStatment, entity);
				prepStatment.setLong(colCount, entity.getId().getValue());
				int affectedRows = prepStatment.executeUpdate();
				if (affectedRows > 0)
					return true;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return false;
	}

	@Override
	public boolean delete(I id) throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.deleteBy.id");
		query = prepareQueryString(query);
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithId(prepStatment, id);

				return prepStatment.executeUpdate() == 1;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public boolean delete(T entity) throws DAOException {
		return delete(entity.getId());
	}

	private void prepareWithId(PreparedStatement prepStatment, I id) {
		try {
			prepStatment.setLong(1, id.getValue());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String prepareQueryString(String query) {
		String id = sqlQuery.getQuery("sql." + tableName + ".id");
		String columns = sqlQuery.getQuery("sql." + tableName + ".columns");
		String result = query.replaceAll("\\{tableName\\}", tableName);
		result = result.replaceAll("\\{id\\}", id);
		result = result.replaceAll("\\{columns\\}", columns);
		return result;
	}

	protected abstract void prepareWithEntity(PreparedStatement prepStatment, T entity) throws DAOException;

	protected List<T> findMany(String query, Preparator preparator) throws DAOException {
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				preparator.prepare(prepStatment);
				try (ResultSet resultSet = prepStatment.executeQuery()) {
					return toDTOList(resultSet);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	protected T toDTO(ResultSet resultSet) throws DAOException, SQLException {
		if (resultSet.next()) {
			return fillEntity(resultSet);
		} else {
			return null;
		}
	}

	protected List<T> toDTOList(ResultSet resultSet) throws DAOException {
		List<T> resultList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				resultList.add(fillEntity(resultSet));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	protected abstract I makeId(long id);

	protected abstract T fillEntity(ResultSet resultSet) throws DAOException, SQLException;

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

	protected String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	protected Date stringToDate(String string) throws DAOException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			throw new DAOException(e);
		}
	}

	@FunctionalInterface
	interface Preparator {
		void prepare(PreparedStatement ps) throws SQLException;
	}
}