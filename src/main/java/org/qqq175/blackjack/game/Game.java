package org.qqq175.blackjack.game;

import java.math.BigDecimal;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.persistence.dto.User;

public interface Game {
	void hit(Player player) throws GameActionDeniedException;

	void doubleBet(Player player) throws GameActionDeniedException;

	void split(Player player) throws GameActionDeniedException;

	void surrender(Player player) throws GameActionDeniedException;

	void deal(Player player, BigDecimal betSize) throws GameActionDeniedException;

	void insurance(Player player) throws GameActionDeniedException;

	void join(User user);

	void leave(User user);
}
