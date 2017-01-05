package org.qqq175.blackjack.game.impl;

import java.util.List;

public abstract class AbstractPlayer {
	private List<Hand> hand;

	public AbstractPlayer() {
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addHand(Hand hand) {
		return this.hand.add(hand);
	}

	/**
	 * @return the hand
	 */
	public List<Hand> getHand() {
		return hand;
	}
}
