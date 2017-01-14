package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the user database table.
 * 
 */
public class User extends Entity<UserId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal accountBalance;

	private String displayName;

	private String email;

	private String firstName;

	private String lastName;

	private BigDecimal lockedBalance;

	private String password;

	private double rating;

	private Date registred;

	private Type type;

	private boolean isActive;

	public User() {
	}

	public enum Type {
		ADMIN, PLAYER;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

	}

	public BigDecimal getAccountBalance() {
		return this.accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Date getRegistred() {
		return this.registred;
	}

	public void setRegistred(Date registred) {
		this.registred = registred;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the lockedBalance
	 */
	public BigDecimal getLockedBalance() {
		return lockedBalance;
	}

	/**
	 * @param lockedBalance
	 *            the lockedBalance to set
	 */
	public void setLockedBalance(BigDecimal lockedBalance) {
		this.lockedBalance = lockedBalance;
	}

}