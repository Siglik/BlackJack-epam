package org.qqq175.blackjack.logic.blackjack.entity;

/**
 * Hand score data class
 * 
 * @author qqq175
 *
 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isBlackJack ? 1231 : 1237);
		result = prime * result + value;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		if (isBlackJack != other.isBlackJack)
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}
