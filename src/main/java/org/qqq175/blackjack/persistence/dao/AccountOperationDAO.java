package org.qqq175.blackjack.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Interface that contains account operation DAO operations
 * 
 * @author qqq175
 *
 */
public interface AccountOperationDAO extends EntityDAO<AccountOperation, AccountOperationId> {
	/**
	 * Insert new account operation. Can be used for transaction.
	 * 
	 * @param entity
	 * @param conn
	 * @return
	 * @throws DAOException
	 */
	AccountOperationId create(AccountOperation entity, ConnectionWrapper conn) throws DAOException;

	/**
	 * Find users account operations with pagination
	 * 
	 * @param userId
	 * @param from
	 * @param count
	 * @return
	 * @throws DAOException
	 */
	List<AccountOperation> findUserOperationsPaginated(UserId userId, long from, int count) throws DAOException;

	/**
	 * calc total operations of specified type
	 * 
	 * @param type
	 * @return
	 * @throws DAOException
	 */
	BigDecimal calcTotal(AccountOperation.Type type) throws DAOException;

	/**
	 * calc user's total operations of specified type
	 * 
	 * @param type
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	BigDecimal calcTotal(AccountOperation.Type type, UserId userId) throws DAOException;

	/**
	 * Calculate user's operation count
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	long countOperations(UserId userId) throws DAOException;
}
