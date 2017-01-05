package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.PlayerAction;

public class Hand {
	private List<Card> cards;
	private BigDecimal bid;
	private Score score;
	private State state;

	public Hand() {
		this.state = new DealState();
		this.score = new Score();
		this.bid = new BigDecimal(0.0);
	}

	private abstract class State implements PlayerAction {

		@Override
		public boolean canHit() {
			return false;

		}

		@Override
		public boolean canDouble() {
			return false;
		}

		@Override
		public boolean canSplit() {
			return false;
		}

		@Override
		public boolean canSurrender() {
			return false;
		}

		@Override
		public boolean canDeal(BigDecimal betSize) {
			return false;
		}

		@Override
		public boolean canInsurance() {
			return false;
		}

	};

	private class DealState extends State {

		@Override
		public boolean canSurrender() {
			return true;
		}

		@Override
		public boolean canDeal(BigDecimal betSize) {
			return true;
		}
	}

	private class PlayState extends State {
	}

	private class DoneState extends State {
	}

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

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return !(state instanceof DoneState);
	}

	private void updateScore() {
		int aces = 0;
		int total = 0;
		boolean isBlackJack = false;
		for (Card card : cards) {
			total += card.getValue();
			if (card.getRank() == Card.Rank.ACE) {
				aces++;
			}
		}
		if (cards.size() == 2 && aces == 1 && total == 21) {
			isBlackJack = true;
		} else {
			while (total > 21 && aces > 0) {
				total -= 10;
				aces--;
			}
		}
		score.setBlackJack(isBlackJack);
		score.setValue(total);
	}
}