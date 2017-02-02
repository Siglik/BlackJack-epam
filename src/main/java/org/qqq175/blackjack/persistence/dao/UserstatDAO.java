package org.qqq175.blackjack.persistence.dao;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Interface that contains user statstic DAO operations
 * 
 * @author qqq175
 *
 */
public interface UserstatDAO extends EntityDAO<Userstat, UserId> {
	/**
	 * updates user wins. increment by 1.
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean incrementWin(UserId userId) throws DAOException;

	/**
	 * updates user blackjack. increment by 1.
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean incrementBlackjack(UserId userId) throws DAOException;

	/**
	 * updates user ties. increment by 1.
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean incrementTie(UserId userId) throws DAOException;

	/**
	 * updates user losses. increment by 1.
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean incrementLoss(UserId userId) throws DAOException;
}
