package org.qqq175.blackjack.game;

import java.math.BigDecimal;
import java.util.function.Supplier;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.impl.Card;
import org.qqq175.blackjack.game.impl.Player;

public interface GameAction {
	void hit(Player player, Supplier<Card> cardTaker) throws GameActionDeniedException;

	void doubleBet(Player player, Supplier<Card> cardTaker) throws GameActionDeniedException;

	void split(Player player, Supplier<Card> cardTaker) throws GameActionDeniedException;

	void surrender(Player player, Supplier<Card> cardTaker) throws GameActionDeniedException;

	void deal(Player player, Supplier<Card> cardTaker, BigDecimal betSize) throws GameActionDeniedException;

	void insurance(Player player, Supplier<Card> cardTaker) throws GameActionDeniedException;
}
