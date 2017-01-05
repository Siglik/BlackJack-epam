package org.qqq175.blackjack.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User extends Entity<UserId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "account_balance")
	private BigDecimal accountBalance;

	@Column(name = "display_name")
	private String displayName;

	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String password;

	private double rating;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registred;

	private String type;

	// bi-directional many-to-one association to AccountOperation
	@OneToMany(mappedBy = "user")
	private List<AccountOperation> accountOperations;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "user")
	private List<Game> games;

	// bi-directional many-to-one association to Message
	@OneToMany(mappedBy = "user")
	private List<Message> messages;

	// bi-directional one-to-one association to Userstat
	@OneToOne(mappedBy = "user")
	private Userstat userstat;

	public User() {
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AccountOperation> getAccountOperations() {
		return this.accountOperations;
	}

	public void setAccountOperations(List<AccountOperation> accountOperations) {
		this.accountOperations = accountOperations;
	}

	public AccountOperation addAccountOperation(AccountOperation accountOperation) {
		getAccountOperations().add(accountOperation);
		accountOperation.setUser(this);

		return accountOperation;
	}

	public AccountOperation removeAccountOperation(AccountOperation accountOperation) {
		getAccountOperations().remove(accountOperation);
		accountOperation.setUser(null);

		return accountOperation;
	}

	public List<Game> getGames() {
		return this.games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public Game addGame(Game game) {
		getGames().add(game);
		game.setUser(this);

		return game;
	}

	public Game removeGame(Game game) {
		getGames().remove(game);
		game.setUser(null);

		return game;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setUser(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setUser(null);

		return message;
	}

	public Userstat getUserstat() {
		return this.userstat;
	}

	public void setUserstat(Userstat userstat) {
		this.userstat = userstat;
	}

}