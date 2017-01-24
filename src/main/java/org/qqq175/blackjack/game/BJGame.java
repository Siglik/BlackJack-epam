package org.qqq175.blackjack.game;

import java.math.BigDecimal;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.persistence.entity.User;

public interface BJGame {
	void hit(User user) throws GameActionDeniedException;

	void doubleBet(User user) throws GameActionDeniedException;

	void split(User user) throws GameActionDeniedException;

	void surrender(User user) throws GameActionDeniedException;

	void deal(User user, BigDecimal betSize) throws GameActionDeniedException;

	void insurance(User user) throws GameActionDeniedException;

	Player join(User user);

	void leave(User user);
}
