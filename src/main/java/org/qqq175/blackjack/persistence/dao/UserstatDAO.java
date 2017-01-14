package org.qqq175.blackjack.persistence.dao;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.entity.Userstat;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public interface UserstatDAO extends EntityDAO<Userstat, UserId> {
	boolean incrementWin(UserId userId)  throws DAOException;

	boolean incrementBlackjack(UserId userId) throws DAOException;

	boolean incrementTie(UserId userId) throws DAOException;

	boolean incrementLoss(UserId userId) throws DAOException;
}
