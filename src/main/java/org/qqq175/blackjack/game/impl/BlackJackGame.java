package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.BJGame;
import org.qqq175.blackjack.game.GameState;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;

public class BlackJackGame implements BJGame {
	private static final int MAX_PLAYERS = 3;
	private GameId id;
	private AtomicInteger modifyCount;
	private Deck deck;
	private List<Player> players;
	private AtomicInteger playersCount;
	private Player activePlayer;
	private Dealer dealer;

	private GameState gameState;
	private State state;
	private BlackJackGame game;
	private ReentrantLock lock;

	public BlackJackGame(GameId gameId, User creator) {
		this(gameId, 6, true, MAX_PLAYERS, creator);
	}

	public BlackJackGame(GameId gameId, User creator, int playerCount) {
		this(gameId, 6, true, playerCount <= MAX_PLAYERS ? playerCount : MAX_PLAYERS, creator);
	}

	private BlackJackGame(GameId gameId, int decksCount, boolean isCasinoPlays, int maxPlayers, User creator) {
		id = gameId;
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
		lock = new ReentrantLock();
	}

	private void modify() {
		int modCount = modifyCount.incrementAndGet();
		// WebSocket.send(this, List<UserId>);
	}

	private boolean nextPlayer() {
		int index = activePlayer != null ? players.indexOf(activePlayer) : 0;
		boolean foundNext = false;

		while (!foundNext && index < players.size()) {
			Player player = players.get(index);
			if (player != null && player.getState() != GameState.UNACTIVE) {
				activePlayer.setActive(false);
				player.setActive(true);
				activePlayer = player;
				foundNext = true;
			} else {
				index++;
			}
		}

		if (!foundNext) {
			activePlayer = null;
			state.nextState();
		}
		return foundNext;
	}

	private abstract class State {
		public abstract void nextState();

		/*
		 * @Override public void insurance(User user) throws
		 * GameActionDeniedException { throw new
		 * GameActionDeniedException("error.game.actiondenied"); }
		 */

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

		public void surrender(User user) throws GameActionDeniedException {
			// player.canSurrender();
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
				// if (p != null && p.isDone()) {
				// List<Hand> hs = p.getHands();
				// if (!hs.isEmpty()) {
				// Hand h = hs.get(0);
				// if (h != null) {
				// h.addCard(deck.pullCard());
				// h.addCard(deck.pullCard());
				// }
				// }
				// }
			}
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
	public void hit(User user) throws GameActionDeniedException {
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
	public void doubleBet(User user) throws GameActionDeniedException {
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
	public void split(User user) throws GameActionDeniedException {
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
	public void surrender(User user) throws GameActionDeniedException {
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
	public void insurance(User user) throws GameActionDeniedException {
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
	public void leave(User user) {
		state.leave(player);
		modify();
	}

	public static int getMaxPlayers() {
		return MAX_PLAYERS;
	}

	public GameId getId() {
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