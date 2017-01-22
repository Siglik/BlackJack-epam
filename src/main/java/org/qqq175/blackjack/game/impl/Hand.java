package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.game.GameUtil;

public class Hand {
	private List<Card> cards;
	private BigDecimal bid;
	private Score score;
	private boolean isSurrendered;
	private boolean isActive;
	private boolean isFirstAction;

	public Hand() {
		this.score = new Score();
		this.bid = new BigDecimal(0.0);
	}

	public Hand split() {
		return null;
	}

	private void updateScore() {
		score = GameUtil.calcScore(this);
	}

	public boolean canHit() {
		if (!score.isBlackJack()) {
			if (score.getValue() < 21) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public boolean canDouble() {
		return (9 <= score.getValue() && score.getValue() <= 11);
	}

	public boolean canSplit() {
		return cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
	}

	public boolean canSurrender() {
		return isFirstAction;
	}

	/**
	 * @return the isSurrendered
	 */
	public boolean isSurrendered() {
		return isSurrendered;
	}

	/**
	 * @param isSurrendered
	 *            the isSurrendered to set
	 */
	public void setSurrendered(boolean isSurrendered) {
		this.isSurrendered = isSurrendered;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @param card
	 */
	public boolean addCard(Card card) {
		boolean result = cards.add(card);
		if (result) {
			updateScore();
		}
		return result;
	}

	/**
	 * 
	 * @param card
	 * @return
	 */
	public boolean removeCard(Card card) {
		boolean result = cards.remove(card);
		if (result) {
			updateScore();
		}
		return result;
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
	public List<Card> getCardsList() {
		List<Card> cardsList = new ArrayList<>(cards);
		return cardsList;
	}

	/**
	 * @return the score
	 */
	public Score getScore() {
		return score;
	}

	/**
	 * @return the isFirstAction
	 */
	public boolean isFirstAction() {
		return isFirstAction;
	}

	/**
	 * @param isFirstAction
	 *            the isFirstAction to set
	 */
	public void setFirstAction(boolean isFirstAction) {
		this.isFirstAction = isFirstAction;
	}
}