package org.qqq175.blackjack.persistence.dao;

import java.sql.SQLException;

import org.qqq175.blackjack.exception.EntityNotFoundException;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public interface UserDAO extends EntityDAO<User, UserId> {
	User findUser(String login, String password) throws SQLException, EntityNotFoundException;
}