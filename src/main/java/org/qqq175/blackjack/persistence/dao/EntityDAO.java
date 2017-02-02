package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Entity;
import org.qqq175.blackjack.persistence.entity.id.EntityId;

/**
 * Interface that contains basic DAO operations
 * 
 * @author qqq175
 *
 * @param <T>
 *            - EntityType
 * @param <I>
 *            - Id type
 */
public interface EntityDAO<T extends Entity<I>, I extends EntityId> {
	/**
	 * insert entity to database
	 * 
	 * @param entity
	 * @return
	 * @throws DAOException
	 */
	I create(T entity) throws DAOException;

	/**
	 * find and get all entities from database
	 * 
	 * @return
	 * @throws DAOException
	 */
	List<T> findAll() throws DAOException;

	/**
	 * find entity in database by its ID
	 * 
	 * @param id
	 * @return
	 * @throws DAOException
	 * @throws DAOException
	 */
	T findEntityById(I id) throws DAOException, DAOException;

	/**
	 * update entity in database
	 * 
	 * @param entity
	 * @return
	 * @throws DAOException
	 */
	boolean update(T entity) throws DAOException;

	/**
	 * delete entity from id
	 * 
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	boolean delete(I id) throws DAOException;

	/**
	 * delete entity from id
	 * 
	 * @param entity
	 * @return
	 * @throws DAOException
	 */
	boolean delete(T entity) throws DAOException;
}
