package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;

import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the userstat database table.
 * 
 */
public class Userstat extends Entity<UserId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private int blackjack;

	private int loss;

	private int tie;

	private int win;

	public Userstat() {
	}

	public int getBlackjack() {
		return this.blackjack;
	}

	public void setBlackjack(int blackjack) {
		this.blackjack = blackjack;
	}

	public int getLoss() {
		return this.loss;
	}

	public void setLoss(int loss) {
		this.loss = loss;
	}

	public int getTie() {
		return this.tie;
	}

	public void setTie(int tie) {
		this.tie = tie;
	}

	public int getWin() {
		return this.win;
	}

	public void setWin(int win) {
		this.win = win;
	}
}