package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the account_operation database table.
 * 
 */
@Entity
@Table(name="account_operation")
@NamedQuery(name="AccountOperation.findAll", query="SELECT a FROM AccountOperation a")
public class AccountOperation extends Entity<AccountOperationId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal ammount;

	private String comment;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	private String type;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public AccountOperation() {
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}