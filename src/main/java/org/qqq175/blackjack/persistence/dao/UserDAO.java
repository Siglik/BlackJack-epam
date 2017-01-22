package org.qqq175.blackjack.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public interface UserDAO extends EntityDAO<User, UserId> {
	User findUser(String login, String password) throws DAOException;

	User findUserWithHash(String login, String passwordHash) throws DAOException;

	User findUser(String email) throws DAOException;

	List<User> findAllLimit(long from, int count) throws DAOException;

	long countUsers() throws DAOException;

	boolean disableUser(UserId userId) throws DAOException;

	boolean enableUser(UserId userId) throws DAOException;

	boolean updateBalance(UserId userId, BigDecimal change, ConnectionWrapper connection) throws DAOException;

	boolean lockBalance(UserId userId, BigDecimal change) throws DAOException;

	boolean unlockBalance(UserId userId, BigDecimal change) throws DAOException;

	boolean decreaceLockedBalance(UserId userId, BigDecimal change) throws DAOException;

	boolean updateType(UserId userId, User.Type type) throws DAOException;

	boolean updatePassword(UserId userId, String oldPass, String newPass) throws DAOException;

	boolean updatePersonal(UserId userId, String firstName, String lastName, String displayName) throws DAOException;
}