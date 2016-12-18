package org.qqq175.blackjack.game;

import java.math.BigDecimal;

import org.qqq175.blackjack.exception.GameActionDeniedException;

public interface GameActions {
	void hit() throws GameActionDeniedException;

	void doubleBet() throws GameActionDeniedException;

	void split() throws GameActionDeniedException;

	void surrender() throws GameActionDeniedException;

	void deal(BigDecimal betSize) throws GameActionDeniedException;

	void insurance() throws GameActionDeniedException;
}
