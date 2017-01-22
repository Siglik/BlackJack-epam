package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.BJGame;
import org.qqq175.blackjack.persistence.entity.User;

public class BlackJackGame implements BJGame {
	private static final int MAX_PLAYERS = 3;
	private int id;
	private AtomicInteger modifyCount;
	private Deck deck;
	private List<Player> players;
	private AtomicInteger playersCount;
	private Player activePlayer;
	private Dealer dealer;

	private State state;
	private BlackJackGame game;

	public BlackJackGame(User creator) {
		this(6, true, MAX_PLAYERS, creator);
	}

	public BlackJackGame(User creator, int playerCount) {
		this(6, true, playerCount <= MAX_PLAYERS ? playerCount : MAX_PLAYERS, creator);
	}

	private BlackJackGame(int decksCount, boolean isCasinoPlays, int maxPlayers, User creator) {
		id = 1;
		game = this;
		deck = new Deck(decksCount);
		if (isCasinoPlays) {
			dealer = new Dealer();
		}

		players = new CopyOnWriteArrayList<>();
		Player player = new Player(creator.getId(), true);
		players.add(player);
		player.setActive(true);
		activePlayer = player;
		playersCount = new AtomicInteger(1);

		for (int i = 1; i < maxPlayers; i++) {
			players.add(null);
		}

		state = new InitState();
		modifyCount = new AtomicInteger(1);
	}

	private void modify() {
		int modCount = modifyCount.incrementAndGet();
		// WebSocket.send(this, List<UserId>);
	}

	private boolean nextPlayer() {
		int userId = activePlayer != null ? players.indexOf(activePlayer) : 0;
		boolean foundNext = false;

		while (!foundNext && userId < players.size()) {
			Player player = players.get(userId);
			if (player != null && player.isDone()) {
				activePlayer.setActive(false);
				player.setActive(true);
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
		return foundNext;
	}

	private abstract class State implements BJGame {
		public abstract void nextState();

		@Override
		public void insurance(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public void hit(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public void doubleBet(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public void split(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public void surrender(Player player) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public void deal(Player player, BigDecimal betSize) throws GameActionDeniedException {
			throw new GameActionDeniedException("error.game.actiondenied");
		}

		@Override
		public Player join(User user) {
			int nextPlayersCount = playersCount.incrementAndGet();
			if (nextPlayersCount <= MAX_PLAYERS) {
				Player player = new Player(user.getId(), false);
				players.add(player);
				modify();
				return player;
			} else {
				playersCount.decrementAndGet();
				return null;
			}
		}
	};

	private class InitState extends State {

		@Override
		public void surrender(Player player) throws GameActionDeniedException {
			player.canSurrender();
			game.nextPlayer();
		}

		@Override
		public void deal(Player player, BigDecimal betSize) throws GameActionDeniedException {
			if (player.tryDeal(betSize)) {

			} else {

			}

			game.nextPlayer();
		}

		@Override
		public void nextState() {
			giveCards();
			activePlayer = null;
			state = new PlayState();
		}

		private void giveCards() {
			for (Player p : players) {
				if (p != null && p.isDone()) {
					List<Hand> hs = p.getHands();
					if (!hs.isEmpty()) {
						Hand h = hs.get(0);
						if (h != null) {
							h.addCard(deck.pullCard());
							h.addCard(deck.pullCard());
						}
					}
				}
			}
		}

		@Override
		public void leave(Player player) {
			// TODO Auto-generated method stub

		}

	}

	private class InsuranceState extends State {

		@Override
		public void insurance(Player player) throws GameActionDeniedException {

		}

		@Override
		public void surrender(Player player) throws GameActionDeniedException {

		}

		@Override
		public void leave(Player player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void nextState() {
			// TODO Auto-generated method stub

		}
	}

	private class PlayState extends State {

		@Override
		public void hit(Player player) throws GameActionDeniedException {
			player.canHit();
		}

		@Override
		public void doubleBet(Player player) throws GameActionDeniedException {
			player.canDouble();
		}

		@Override
		public void split(Player player) throws GameActionDeniedException {
			player.canSplit();
		}

		@Override
		public void surrender(Player player) throws GameActionDeniedException {
			player.canSurrender();
		}

		@Override
		public void nextState() {
			// TODO Auto-generated method stub
		}

		@Override
		public void leave(Player player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void insurance(Player player) throws GameActionDeniedException {
			player.canInsurance();
		}
	}

	private class FinishedState extends State {

		@Override
		public void nextState() {
			// TODO Auto-generated method stub

		}

		@Override
		public void leave(Player player) {
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
	public void hit(Player player) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.hit(player);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#doubleBet(org.qqq175.blackjack.game.impl.
	 * Hand, org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public void doubleBet(Player player) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.doubleBet(player);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#split(org.qqq175.blackjack.game.impl.Hand,
	 * org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public void split(Player player) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.split(player);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#surrender(org.qqq175.blackjack.game.impl.
	 * Hand, org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public void surrender(Player player) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.surrender(player);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.qqq175.blackjack.game.Game#deal(org.qqq175.blackjack.game.impl.Hand,
	 * org.qqq175.blackjack.game.impl.Player)
	 */
	@Override
	public void deal(Player player, BigDecimal bid) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.deal(player, bid);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	@Override
	public void insurance(Player player) throws GameActionDeniedException {
		if (player == activePlayer) {
			state.insurance(player);
			modify();
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	@Override
	public Player join(User user) {
		return state.join(user);
	}

	@Override
	public void leave(Player player) {
		state.leave(player);
		modify();
	}

	public static int getMaxPlayers() {
		return MAX_PLAYERS;
	}

	public int getId() {
		return id;
	}

	public AtomicInteger getModifyCount() {
		return modifyCount;
	}

	public Deck getDeck() {
		return deck;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public AtomicInteger getPlayersCount() {
		return playersCount;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public BlackJackGame getGame() {
		return game;
	}
}