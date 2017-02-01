package org.qqq175.blackjack.logic.blackjack.entity;

public class Dealer {
	private Hand hand;

	private boolean isShowAllCards;

	public Dealer() {
		this.hand = new Hand();
		isShowAllCards = false;
	}

	/**
	 * @return the hand
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * @return the isShowAllCards
	 */
	public boolean isShowAllCards() {
		return isShowAllCards;
	}

	/**
	 * @param isShowAllCards
	 *            the isShowAllCards to set
	 */
	public void setShowAllCards(boolean isShowAllCards) {
		this.isShowAllCards = isShowAllCards;
	}
}
