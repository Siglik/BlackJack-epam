package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the game database table.
 * 
 */
public class Game extends Entity<GameId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date creationTime;

	private UserId userId;

	public Game() {
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public UserId getUserId() {
		return this.userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}
}