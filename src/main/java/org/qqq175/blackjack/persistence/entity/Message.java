package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the message database table.
 * 
 */
public class Message extends Entity<MessageId> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String text;

	private Date time;

	private GameId gameId;

	private UserId userId;

	public Message() {
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public GameId getGameId() {
		return this.gameId;
	}

	public void setGameId(GameId gameId) {
		this.gameId = gameId;
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
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Message other = (Message) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}