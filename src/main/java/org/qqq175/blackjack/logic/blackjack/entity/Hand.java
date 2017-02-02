package org.qqq175.blackjack.logic.blackjack.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.logic.blackjack.GameLogic;
import org.qqq175.blackjack.logic.blackjack.GameResult;
import org.qqq175.blackjack.logic.blackjack.GameStage;

/**
 * Blackjack player's (dealer's) hand data class
 * 
 * @author qqq175
 *
 */
public class Hand {

	private List<Card> cards;
	private BigDecimal bid;
	private BigDecimal insurance;
	private Score score;
	private GameStage stage;
	private GameResult result;

	private boolean isActive;
	private boolean isFirstAction;

	/**
	 * Construct empty hand. Initial game state is DEAL, initial GameResult is
	 * NONE
	 */
	public Hand() {
		this.cards = new ArrayList<>();
		this.score = new Score();
		this.bid = BigDecimal.ZERO;
		this.insurance = BigDecimal.ZERO;
		this.isFirstAction = true;
		this.stage = GameStage.DEAL;
		this.result = GameResult.NONE;
	}

	private void updateScore() {
		score = GameLogic.calcScore(this);
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
	 * Remove card from hand
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
	 * Take and remove last hand card
	 * 
	 * @return card
	 */
	public Card takeLastCard() {
		Card card = null;
		if (cards.size() > 0) {
			card = cards.get(cards.size() - 1);
			cards.remove(cards.size() - 1);
			updateScore();
		}
		return card;
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
	public List<Card> getCardsListCopy() {
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

	public int size() {
		return cards.size();
	}

	/**
	 * @return the stage
	 */
	public GameStage getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the state to set
	 */
	public void setStage(GameStage stage) {
		this.stage = stage;
	}

	public void nextStage() {
		stage = stage.nextState();
	}

	/**
	 * @return the insurance
	 */
	public BigDecimal getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance
	 *            the insurance to set
	 */
	public void setInsurance(BigDecimal insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return the result
	 */
	public GameResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(GameResult result) {
		this.result = result;
	}

}