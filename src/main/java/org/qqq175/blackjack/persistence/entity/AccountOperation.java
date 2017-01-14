package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the account_operation database table.
 * 
 */
public class AccountOperation extends Entity<AccountOperationId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal ammount;

	private String comment;

	private Date time;

	private Type type;

	private UserId userId;

	public AccountOperation() {
	}

	public enum Type {
		PAYMENT, WITHDRAWAL, WIN, LOSS;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public BigDecimal getAmmount() {
		return this.ammount;
	}

	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public UserId getUserId() {
		return this.userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

}