package org.qqq175.blackjack.game.impl;

public class Score {
	private int value;
	private boolean isBlackJack;

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the isBlackJack
	 */
	public boolean isBlackJack() {
		return isBlackJack;
	}

	/**
	 * @param isBlackJack
	 *            the isBlackJack to set
	 */
	public void setBlackJack(boolean isBlackJack) {
		this.isBlackJack = isBlackJack;
	}
}
