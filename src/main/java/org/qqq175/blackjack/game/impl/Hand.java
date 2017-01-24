package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.game.GameState;
import org.qqq175.blackjack.game.GameLogic;

public class Hand {
	

	private List<Card> cards;
	private BigDecimal bid;
	private Score score;
	private boolean isSurrendered;
	private GameState state;
	/**
	 * 
	 */
	private boolean isActive;
	private boolean isFirstAction;
	private boolean isFinished;

	public Hand() {
		this.cards = new ArrayList<>();
		this.score = new Score();
		this.bid = new BigDecimal(0.0);
		this.isFirstAction = true;
		setState(GameState.DEAL);
	}

	public Hand split() {
		return null;
	}

	private void updateScore() {
		score = GameLogic.calcScore(this);
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

	/**
	 * @return the isFinished
	 */
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished the isFinished to set
	 */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public int size() {
		return cards.size();
	}

	/**
	 * @return the state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GameState state) {
		this.state = state;
	}
}