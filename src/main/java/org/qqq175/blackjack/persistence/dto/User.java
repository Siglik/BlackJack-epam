package org.qqq175.blackjack.persistence.dto;

import java.math.BigDecimal;

public class User extends Entity {
	private BigDecimal balance;

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void changeBalance(BigDecimal change) {
		this.balance.add(change);
	}
}
