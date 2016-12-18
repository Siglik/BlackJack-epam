package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.GameActions;

public class Hand {
	private List<Card> cards;
	private BigDecimal bid;
	private Score score;
	private State state;

	public Hand() {
		this.state = new EmptyState();
		this.score = new Score();
	}

	private abstract class State implements GameActions {

	};

	public void addCard(Card card) {
		cards.add(card);
	}

	public Hand split() {
		return null;
	}

	public boolean deleteCard(Card card) {
		return cards.remove(card);
	}

	/**
	 * @return the bid
	 */
	public BigDecimal getBid() {
		return bid;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	/**
	 * @return the cards
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * @param cards
	 *            the cards to set
	 */
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * @return the score
	 */
	public Score getScore() {
		return score;
	}
}