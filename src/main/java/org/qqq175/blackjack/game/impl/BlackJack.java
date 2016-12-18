package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.Game;
import org.qqq175.blackjack.persistence.dto.User;

public class BlackJack implements Game {
	private static final int MAX_PLAYERS = 3;
	private final int id;
	private int modifyCount;
	private Deck deck;
	private List<Player> players;
	private Player activePlayer;
	private Dealer dealer;
	private State state;
	private BlackJack game;

	public BlackJack(User creator) {
		this(6, true, MAX_PLAYERS, creator);
	}

	private BlackJack(int decksCount, boolean isCasinoPlays, int maxPlayers, User creator) {
		id = 1;
		game = this;
		deck = new Deck(decksCount);
		if (isCasinoPlays) {
			dealer = new Dealer();
		}
		state = new InitState();
		players = new ArrayList(maxPlayers);
		Player player = new Player(creator, true);
		players.add(player);

		activePlayer = player;

		for (int i = 1; i < maxPlayers; i++) {
			players.add(null);
		}
		modifyCount = 1;
	}

	private void modify() {
		modifyCount++;
	}

	private void nextHand() {

	}

	private void nextPlayer() {
		int userId = activePlayer != null ? players.indexOf(activePlayer) : 0;

		boolean foundNext = false;
		while (!foundNext || userId < players.size()) {
			Player player = players.get(userId);
			if (player != null && player.isActive()) {
				activePlayer = player;
				foundNext = true;
			} else {
				userId++;
			}
		}

		if (!foundNext) {
			activePlayer = null;
			state.nextState();
		}
	}

	private abstract class State implements Game {
		public abstract void nextState();
	};

	private class InitState extends State {

		@Override
		public void hit(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied.init");
		}

		@Override
		public void doubleBet(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied.init");
		}

		@Override
		public void split(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied.init");
		}

		@Override
		public void surrender(Player player) throws GameActionDeniedException {
			player.surrender();
			game.nextPlayer();
		}

		@Override
		public void deal(Player player, BigDecimal betSize) throws GameActionDeniedException {
			player.deal(betSize);
			game.nextPlayer();
		}

		@Override
		public void nextState() {
			// TODO Auto-generated method stub

		}

		@Override
		public void join(User user) {
			// TODO Auto-generated method stub

		}

		@Override
		public void leave(User user) {
			// TODO Auto-generated method stub

		}

		@Override
		public void insurance(Player player) throws GameActionDeniedException {
			// TODO Auto-generated method stub
		}

	}

	private class PlayState extends State {

		@Override
		public void hit(Player player) throws GameActionDeniedException {
			if (player == activePlayer) {
				player.hit();
			} else {
				throw null;
			}
		}

		@Override
		public void doubleBet(Hand hand, Player player) {
			// TODO Auto-generated method stub
		}

		@Override
		public void split(Hand hand, Player player) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surrender(Hand hand, Player player) {
			// TODO Auto-generated method stub
		}

		@Override
		public void deal(Player player, BigDecimal bid) throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to deal twice");
		}

		@Override
		public void nextState() {
			// TODO Auto-generated method stub

		}

		@Override
		public void join(User user) {
			// TODO Auto-generated method stub

		}

		@Override
		public void leave(User user) {
			// TODO Auto-generated method stub

		}
	}

	private class FinishedState extends State {

		@Override
		public boolean hit(Hand hand, Player player) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean doubleBet(Hand hand, Player player) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Hand split(Hand hand, Player player) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean surrender(Hand hand, Player player) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean deal(Player player, BigDecimal bid) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void nextState() {
			// TODO Auto-generated method stub

		}

		@Override
		public void join(User user) {
			// TODO Auto-generated method stub

		}

		@Override
		public void leave(User user) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#hit(org.qqq175.blackjack.game.impl.Hand,
	 * org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public boolean hit(Hand hand, Player player) throws GameActionDeniedException {
		return state.hit(hand, player);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#doubleBet(org.qqq175.blackjack.game.impl.
	 * Hand, org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public boolean doubleBet(Hand hand, Player player) throws GameActionDeniedException {
		return state.doubleBet(hand, player);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#split(org.qqq175.blackjack.game.impl.Hand,
	 * org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public Hand split(Hand hand, Player player) throws GameActionDeniedException {
		return state.split(hand, player);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#surrender(org.qqq175.blackjack.game.impl.
	 * Hand, org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public boolean surrender(Hand hand, Player player) throws GameActionDeniedException {
		return state.surrender(hand, player);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#deal(org.qqq175.blackjack.game.impl.Hand,
	 * org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public boolean deal(Player player, BigDecimal bid) throws GameActionDeniedException {
		return state.deal(player, bid);
	}

	@Override
	public void join(User user) {
		state.join(user);
	}

	@Override
	public void leave(User user) {
		state.leave(user);
	}
}
