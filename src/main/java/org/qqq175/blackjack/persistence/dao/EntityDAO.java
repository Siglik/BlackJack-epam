package org.qqq175.blackjack.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import org.qqq175.blackjack.exception.EntityNotFoundException;
import org.qqq175.blackjack.exception.UnsupportedOperation;
import org.qqq175.blackjack.persistence.entity.Entity;
import org.qqq175.blackjack.persistence.entity.id.EntityId;

public interface EntityDAO<T extends Entity<I>, I extends EntityId> {
	boolean create(T entity) throws SQLException, UnsupportedOperation;

	List<T> findAll() throws SQLException;

	T findEntityById(I id) throws SQLException, EntityNotFoundException;

	boolean update(T entity) throws SQLException, UnsupportedOperation;

	boolean delete(I id) throws SQLException;

	boolean delete(T entity) throws SQLException;
}
