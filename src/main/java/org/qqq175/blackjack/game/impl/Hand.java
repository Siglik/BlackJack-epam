package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.GameUtil;

public class Hand {
	private List<Card> cards;
	private BigDecimal bid;
	private Score score;
	private boolean isSurrendered;
	private boolean isActive;

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
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canDouble() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canSplit() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canSurrender() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canDeal(BigDecimal betSize) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canInsurance() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canStand() {
		// TODO Auto-generated method stub
		return false;
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
		return cards.add(card);
	}

	/**
	 * 
	 * @param card
	 * @return
	 */
	public boolean removeCard(Card card) {
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
	 * @return the score
	 */
	public Score getScore() {
		return score;
	}
}