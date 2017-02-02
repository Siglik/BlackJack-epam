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

/**
 * Contains EntityDAO implementation for SQL(MySQL) database
 * 
 * @author qqq175
 *
 * @param <T>
 * @param <I>
 */
public abstract class EntityDAOImpl<T extends Entity<I>, I extends EntityId> implements EntityDAO<T, I> {
	private static final String UNABLE_OPERATION = "Unable to perform operation.";
	private static final String UNABLE_EXTRACT_RESULT = "Unable to extract query result.";
	private static final String UNABLE_PARSE_DATE = "Unable to parse date";
	private static final String ZERO_ROWS_AFFECTED = "Unable to insert: 0 rows are affected.";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private String tableName;
	private int colCount;
	protected ConnectionPool connPool;
	protected SQLQueryManager sqlQuery;

	/**
	 * Construct entity with defined table name
	 * 
	 * @param tableName
	 * @param colCount
	 */
	public EntityDAOImpl(String tableName, int colCount) {
		this.tableName = tableName;
		this.colCount = colCount;
		this.sqlQuery = SQLQueryManager.getInstance();
		this.connPool = ConnectionPool.getInstance();
	}

	@Override
	public I create(T entity) throws DAOException {
		/* find sql query for specific entity type */
		String query = sqlQuery.getQuery("sql." + tableName + ".insert");
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				/* prepare entity with specific entity type */
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
					throw new DAOException(ZERO_ROWS_AFFECTED);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(UNABLE_OPERATION, e);
		}
	}

	@Override
	public List<T> findAll() throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.findAll");
		/* prepare query string with specific entity type */
		query = prepareQueryString(query);
		return findMany(query, (s) -> {
		});
	}

	@Override
	public T findEntityById(I id) throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.findBy.id");
		/* prepare query string with specific entity type */
		query = prepareQueryString(query);
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithId(prepStatment, id);
				try (ResultSet resultSet = prepStatment.executeQuery()) {
					return toEntity(resultSet);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(UNABLE_OPERATION, e);
		}
	}

	@Override
	public boolean update(T entity) throws DAOException {
		/* find sql query for specific entity type */
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
			throw new DAOException(UNABLE_OPERATION, e);
		}
		return false;
	}

	@Override
	public boolean delete(I id) throws DAOException {
		String query = sqlQuery.getQuery("sql.entity.deleteBy.id");
		/* prepare query string with specific entity type */
		query = prepareQueryString(query);
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				prepareWithId(prepStatment, id);

				return prepStatment.executeUpdate() == 1;
			}
		} catch (SQLException e) {
			throw new DAOException(UNABLE_OPERATION, e);
		}
	}

	@Override
	public boolean delete(T entity) throws DAOException {
		return delete(entity.getId());
	}

	private void prepareWithId(PreparedStatement prepStatment, I id) throws SQLException {
		prepStatment.setLong(1, id.getValue());
	}

	/**
	 * Prepare query string(inser tabble and column names to query template)
	 * 
	 * @param queryTemplate
	 *            - query template
	 * @return
	 */
	String prepareQueryString(String queryTemplate) {
		String id = sqlQuery.getQuery("sql." + tableName + ".id");
		String columns = sqlQuery.getQuery("sql." + tableName + ".columns");
		String result = queryTemplate.replaceAll("\\{tableName\\}", tableName);
		result = result.replaceAll("\\{id\\}", id);
		result = result.replaceAll("\\{columns\\}", columns);
		return result;
	}

	/**
	 * Find list of entities of type T with some query. Its used Preparator
	 * object (functional interface) to prepare statement for specific query.
	 * 
	 * @param query
	 * @param preparator
	 * @return
	 * @throws DAOException
	 */
	List<T> findMany(String query, Preparator preparator) throws DAOException {
		try (ConnectionWrapper connection = connPool.retrieveConnection()) {
			try (PreparedStatement prepStatment = connection.prepareStatement(query)) {
				preparator.prepare(prepStatment);
				try (ResultSet resultSet = prepStatment.executeQuery()) {
					return toEntityList(resultSet);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(UNABLE_OPERATION, e);
		}
	}

	/**
	 * Extract one entity from Result set
	 * 
	 * @param resultSet
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	T toEntity(ResultSet resultSet) throws DAOException, SQLException {
		if (resultSet.next()) {
			return fillEntity(resultSet);
		} else {
			return null;
		}
	}

	/**
	 * Extract entities list from result set
	 * 
	 * @param resultSet
	 * @return
	 * @throws DAOException
	 */
	List<T> toEntityList(ResultSet resultSet) throws DAOException {
		List<T> resultList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				resultList.add(fillEntity(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(UNABLE_EXTRACT_RESULT, e);
		}
		return resultList;
	}

	/**
	 * prepare insert or update query. Must be implemented in sublcasses
	 * 
	 * @param prepStatment
	 * @param entity
	 * @throws DAOException
	 */
	protected abstract void prepareWithEntity(PreparedStatement prepStatment, T entity) throws DAOException;

	/**
	 * make specific id from long. Must be implemented in sublcasses
	 * 
	 * @param id
	 * @return
	 */
	protected abstract I makeId(long id);

	/**
	 * Fill entity from result set. Must be implemented in sublcasses
	 * 
	 * @param resultSet
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 */
	protected abstract T fillEntity(ResultSet resultSet) throws DAOException, SQLException;

	/**
	 * @return the tableName
	 */
	String getTableName() {
		return tableName;
	}

	/**
	 * @return the sqlQuery
	 */
	SQLQueryManager getSqlQuery() {
		return sqlQuery;
	}

	/**
	 * covert java.util.Date to MySQL data string format
	 * 
	 * @param date
	 * @return
	 */
	String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	/**
	 * covert MySQL data string format to java.util.Date
	 * 
	 * @param string
	 * @return
	 * @throws DAOException
	 */
	Date stringToDate(String string) throws DAOException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			throw new DAOException(UNABLE_PARSE_DATE, e);
		}
	}

	/**
	 * Prepare prepared statement
	 * 
	 * @author qqq175
	 */
	@FunctionalInterface
	interface Preparator {
		void prepare(PreparedStatement ps) throws SQLException;
	}
}