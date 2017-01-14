package org.qqq175.blackjack.persistence.dao;

import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Entity;
import org.qqq175.blackjack.persistence.entity.id.EntityId;

public interface EntityDAO<T extends Entity<I>, I extends EntityId> {
	I create(T entity) throws DAOException, UnsupportedOperationException;

	List<T> findAll() throws DAOException;

	T findEntityById(I id) throws DAOException, DAOException;

	boolean update(T entity) throws DAOException, UnsupportedOperationException;

	boolean delete(I id) throws DAOException;

	boolean delete(T entity) throws DAOException;
}
