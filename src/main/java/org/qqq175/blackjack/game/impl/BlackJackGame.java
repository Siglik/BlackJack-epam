package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.GameLogic;
import org.qqq175.blackjack.game.GameStage;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class BlackJackGame {
	private static final int MAX_PLAYERS = 3;
	private GameId id;
	private AtomicInteger modifyCount;
	private Deck deck;
	private List<Player> players;
	private AtomicInteger playersCount;
	private Player activePlayer;
	private Dealer dealer;

	private GameStage gameStage;
	private ReentrantLock lock;

	private BlackJackGame(GameId gameId, User creator) {
		this(gameId, 6, MAX_PLAYERS, creator);
	}

	private BlackJackGame(GameId gameId, User creator, int playerCount) {
		this(gameId, 6, playerCount <= MAX_PLAYERS ? playerCount : MAX_PLAYERS, creator);
	}

	private BlackJackGame(GameId gameId, int decksCount, int maxPlayers, User creator) {
		id = gameId;
		deck = new Deck(decksCount);

		players = new CopyOnWriteArrayList<>();
		Player player = new Player(creator.getId(), true);
		players.add(player);
		player.setActive(true);
		activePlayer = player;
		playersCount = new AtomicInteger(1);

		for (int i = 1; i < maxPlayers; i++) {
			players.add(null);
		}

		gameStage = GameStage.UNACTIVE;
		modifyCount = new AtomicInteger(1);
		lock = new ReentrantLock();
	}

	public static BlackJackGame createGame(GameId gameId, User creator, int playerCount) {
		BlackJackGame instance = new BlackJackGame(gameId, creator, playerCount);
		instance.nextStage();
		return instance;
	}

	public static BlackJackGame createGame(GameId gameId, User creator) {
		BlackJackGame instance = new BlackJackGame(gameId, creator);
		instance.nextStage();
		return instance;
	}

	private void modify() {
		int modCount = modifyCount.incrementAndGet();
		// WebSocket.send(this, List<UserId>);
	}

	/**
	 * 
	 * @param stage
	 * @return
	 */
	private boolean nextHand(GameStage stage) {
		boolean result = false;
		if (activePlayer != null && activePlayer.nextHand(stage)) {
			result = true;
		} else {
			boolean hasNext;
			do {
				hasNext = nextPlayer(stage);
				if (activePlayer != null) {
					result = activePlayer.nextHand(stage);
				}
			} while (result != true && hasNext == true);
		}
		return result;
	}

	/**
	 * 
	 * @param stage
	 * @return
	 */
	private boolean nextPlayer(GameStage stage) {
		int index = activePlayer != null ? players.indexOf(activePlayer) : 0;
		boolean foundNext = false;

		while (!foundNext && index < players.size()) {
			Player player = players.get(index);
			if (player != null && player.getStage() == stage) {
				if (activePlayer != null) {
					activePlayer.setActive(false);
					if (activePlayer.getStage() == stage) {
						activePlayer.nextStage();
					}
				}
				player.setActive(true);
				activePlayer = player;
				foundNext = true;
			} else {
				index++;
			}
		}

		if (!foundNext) {
			activePlayer = null;
		}

		return foundNext;
	}

	/**
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 */
	public void hit(User user) throws GameActionDeniedException {
		/*
		 * Hit one hand while possible, then then go to next, if its no next
		 * hand of any player - go to the next game stage
		 */
		if (user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryHit(activePlayer, deck)) {
					if (!GameLogic.canHit(activePlayer)) {
						if (!nextHand(gameStage)) {
							nextStage();
						}
					}
					modify();
				} else {
					throw new GameActionDeniedException("error.game.error");
				}
			} else {
				throw new GameActionDeniedException("error.game.wrongstage");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	/**
	 * Double hand and stay
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 */
	public void doubleBet(User user) throws GameActionDeniedException {
		if (user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryDouble(activePlayer, deck)) {
					if (!nextHand(gameStage)) {
						nextStage();
					}
					modify();
				} else {
					throw new GameActionDeniedException("error.game.error");
				}
			} else {
				throw new GameActionDeniedException("error.game.wrongstage");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	/**
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 */
	public void split(User user) throws GameActionDeniedException {
		if (user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.trySplit(activePlayer, deck)) {
					activePlayer.resetActiveHand();
					if (!nextHand(gameStage)) {
						nextStage();
					}
					modify();
				} else {
					throw new GameActionDeniedException("error.game.error");
				}
			} else {
				throw new GameActionDeniedException("error.game.wrongstage");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	/**
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 */
	public void surrender(User user) throws GameActionDeniedException {
		if (user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY || gameStage == GameStage.DEAL) {
				if (GameLogic.trySurrender(activePlayer)) {
					if (!nextHand(gameStage)) {
						nextStage();
					}
					modify();
				} else {
					throw new GameActionDeniedException("error.game.error");
				}
			} else {
				throw new GameActionDeniedException("error.game.wrongstage");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	/**
	 * 
	 * @param player
	 * @param bid
	 * @throws GameActionDeniedException
	 */
	public void deal(User user, BigDecimal bid) throws GameActionDeniedException {
		if (user.getId().equals(activePlayer.getUserId())) {
			if (GameLogic.tryDeal(activePlayer, bid, deck)) {
				if (!nextHand(gameStage)) {
					nextStage();
				}
				modify();
			} else {
				throw new GameActionDeniedException("error.game.error");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}

	}

	/**
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 */
	public void insurance(User user) throws GameActionDeniedException {
		if (user.getId().equals(activePlayer.getUserId())) {
			if (GameLogic.tryDouble(activePlayer, deck)) {
				modify();
			} else {
				throw new GameActionDeniedException("error.game.error");
			}
		} else {
			throw new GameActionDeniedException("error.game.wrongplayer");
		}
	}

	/**
	 * 
	 * @param user
	 * @return
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

	public void leave(User user) {
		modify();
	}

	private void nextStage() {
		if (activePlayer != null) {
			activePlayer.resetActiveHand();
			activePlayer.setActive(false);
			activePlayer = null;
		}
		GameStage newStage = gameStage = gameStage.nextState();
		for (Player player : players) {
			GameStage plStage = player.getStage();
			if (plStage != GameStage.UNACTIVE && plStage.compareTo(gameStage) < 0) {
				player.nextStage();
			}
		}

		switch (newStage) {
		case DEAL:
			if (!nextHand(gameStage)) {
				nextStage();
			} else {
				dealer = new Dealer();
				// just lets deal
			}
			break;
		case PLAY:
			if (!nextHand(gameStage)) {
				nextStage();
			} else {
				GameLogic.dealDealer(dealer, deck);
				// just lets play
			}
			break;
		case RESULT:
			if (!nextHand(gameStage)) {
				nextStage();
			} else {
				GameLogic.playDealer(dealer, deck);
				calcResults();
				nextStage();
			}
			break;
		case DONE:
			if (!nextHand(gameStage)) {
				if (players.size() > 0) {
					nextStage();
				} else {
					this.finishEmptyGame();
				}
			} else {
				payOut();
				modify();
				try {
					// sleep for user to look results
					Thread.currentThread().sleep(5000l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nextStage();
			}
			break;
		case UNACTIVE:

			break;
		}

	}

	private void finishEmptyGame() {
		// TODO Auto-generated method stub

	}

	private void calcResults() {
		if (gameStage.equals(GameStage.RESULT)) {
			Hand dealerHand = dealer.getHand();
			do {
				Hand playerHand = activePlayer.getActiveHand();
				GameLogic.calcHandResult(playerHand, dealerHand);
			} while (nextHand(GameStage.RESULT));
		}
	}

	private void payOut() {
		if (gameStage.equals(GameStage.DONE)) {
			do {
				Hand playerHand = activePlayer.getActiveHand();
				GameLogic.payOut(activePlayer, playerHand);
			} while (nextHand(GameStage.RESULT));
		}

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

	public List<Player> getPlayersList() {
		List<Player> copyOfPlayers = new ArrayList<>(players);
		return copyOfPlayers;
	}

	public int getPlayersCount() {
		return playersCount.get();
	}

	public int getFreeSlots() {
		return MAX_PLAYERS - playersCount.get();
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public Dealer getDealer() {
		return dealer;
	}

	private int indexOfPlayer(UserId userId) {
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player != null && player.getUserId().equals(userId)) {
				return i;
			}
		}

		return -1;
	}
}