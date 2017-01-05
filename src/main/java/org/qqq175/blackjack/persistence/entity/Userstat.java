package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.qqq175.blackjack.persistence.entity.id.UserId;


/**
 * The persistent class for the userstat database table.
 * 
 */
@Entity
@Table(name="userstat")
@NamedQuery(name="Userstat.findAll", query="SELECT u FROM Userstat u")
public class Userstat extends Entity<UserId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private int blackjack;

	private int loss;

	private int tie;

	private int win;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;

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

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}