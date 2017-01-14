package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

import java.util.Date;


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

}