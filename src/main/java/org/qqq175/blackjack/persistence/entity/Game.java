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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (creationTime == null) {
			if (other.creationTime != null)
				return false;
		} else if (!creationTime.equals(other.creationTime))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}