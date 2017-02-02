package org.qqq175.blackjack.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Interface that contains basic DAO operations
 * 
 * @author qqq175
 *
 */
public interface UserDAO extends EntityDAO<User, UserId> {
	/**
	 * find user by Email and password
	 * 
	 * @param login
	 * @param password
	 * @return
	 * @throws DAOException
	 */
	User findUser(String login, String password) throws DAOException;

	/**
	 * find user by Email and password hash
	 * 
	 * @param login
	 * @param passwordHash
	 * @return
	 * @throws DAOException
	 */
	User findUserWithHash(String login, String passwordHash) throws DAOException;

	/**
	 * find user by Email
	 * 
	 * @param email
	 * @return
	 * @throws DAOException
	 */
	User findUser(String email) throws DAOException;

	/**
	 * find all users with pagination
	 * 
	 * @param from
	 * @param count
	 * @return
	 * @throws DAOException
	 */
	List<User> findAllPaginated(long from, int count) throws DAOException;

	/**
	 * count users in database
	 * 
	 * @return
	 * @throws DAOException
	 */
	long countUsers() throws DAOException;

	/**
	 * set user banned
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean disableUser(UserId userId) throws DAOException;

	/**
	 * unban user
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	boolean enableUser(UserId userId) throws DAOException;

	/**
	 * update user balance
	 * 
	 * @param userId
	 * @param change
	 *            - ammont of change +-sum
	 * @param connection
	 * @return
	 * @throws DAOException
	 */
	boolean updateBalance(UserId userId, BigDecimal change, ConnectionWrapper connection) throws DAOException;

	/**
	 * lock user balance (transfer sum from account balance to locked balance)
	 * 
	 * @param userId
	 * @param change
	 * @return
	 * @throws DAOException
	 */
	boolean lockBalance(UserId userId, BigDecimal change) throws DAOException;

	/**
	 * unlock user balance (transfer sum from locked balance to account balance)
	 * 
	 * @param userId
	 * @param change
	 * @return
	 * @throws DAOException
	 */
	boolean unlockBalance(UserId userId, BigDecimal change) throws DAOException;

	/**
	 * unlock user balance (transfer sum from locked balance to account balance)
	 * TRANSACTION POSSIBILITY VERSION
	 * 
	 * @param userId
	 * @param change
	 * @param connection
	 * @return
	 * @throws DAOException
	 */
	boolean unlockBalance(UserId userId, BigDecimal change, ConnectionWrapper connection) throws DAOException;

	/**
	 * decrease users locked balance
	 * 
	 * @param userId
	 * @param change
	 * @return
	 * @throws DAOException
	 */
	boolean decreaceLockedBalance(UserId userId, BigDecimal change) throws DAOException;

	/**
	 * decrease users locked balance. TRANSACTION POSSIBILITY VERSION
	 * 
	 * @param userId
	 * @param change
	 * @param connection
	 * @return
	 * @throws DAOException
	 */
	boolean decreaceLockedBalance(UserId userId, BigDecimal change, ConnectionWrapper connection) throws DAOException;

	/**
	 * Update user type
	 * 
	 * @param userId
	 * @param type
	 * @return
	 * @throws DAOException
	 */
	boolean updateType(UserId userId, User.Type type) throws DAOException;

	/**
	 * update users password
	 * 
	 * @param userId
	 * @param oldPass
	 * @param newPass
	 * @return
	 * @throws DAOException
	 */
	boolean updatePassword(UserId userId, String oldPass, String newPass) throws DAOException;

	/**
	 * update users personal info
	 * 
	 * @param userId
	 * @param firstName
	 * @param lastName
	 * @param displayName
	 * @return
	 * @throws DAOException
	 */
	boolean updatePersonal(UserId userId, String firstName, String lastName, String displayName) throws DAOException;
}