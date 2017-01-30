package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.GameLogic;
import org.qqq175.blackjack.game.GameStage;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.GamePool;

public class BlackJackGame {
	private static Logger log = LogManager.getLogger(BlackJackGame.class);
	private static final int MAX_PLAYERS = 3;
	private GameId id;
	private Deck deck;
	private int maxPlayers;
	private List<Player> players;
	private List<Player> leavingPlayers;
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
		this.maxPlayers = maxPlayers;

		players = new CopyOnWriteArrayList<>();
		leavingPlayers = new CopyOnWriteArrayList<>();
		Player player = new Player(creator.getId(), true);
		players.add(player);
		activePlayer = null;
		playersCount = new AtomicInteger(1);

		gameStage = GameStage.UNACTIVE;
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
		// pulling:do nothing
		// websocket: do callback
	}

	/**
	 * 
	 * @param stage
	 * @return
	 */
	private boolean nextHand(GameStage stage) {
		boolean result = false;
		if (activePlayer != null && activePlayer.nextHand(stage)) {
			log.debug("Next hand: same player");
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
		log.debug("Next hand found: " + result);
		return result;
	}

	/**
	 * 
	 * @param stage
	 * @return
	 */
	private boolean nextPlayer(GameStage stage) {
		int nextPlayerIndex = activePlayer != null ? players.indexOf(activePlayer) + 1 : 0;
		boolean foundNext = false;

		while (!foundNext && nextPlayerIndex < players.size()) {
			Player player = players.get(nextPlayerIndex);
			if (player != null && player.getStage() == stage) {
				if (activePlayer != null) {
					activePlayer.setActive(false);
					if (activePlayer.getStage() == stage) {
						activePlayer.setStage(stage.nextState());
					}
				}
				player.setActive(true);
				activePlayer = player;
				foundNext = true;
			} else {
				nextPlayerIndex++;
			}
		}

		if (!foundNext) {
			if (activePlayer != null) {
				activePlayer.setActive(false);
				activePlayer = null;
			}
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
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryHit(activePlayer, deck)) {
					if (!GameLogic.canHit(activePlayer)) {
						if (!nextHand(gameStage)) {
							parallelNextSage();
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
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryDouble(activePlayer, deck)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
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
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.trySplit(activePlayer, deck)) {
					activePlayer.resetActiveHand();
					if (!nextHand(gameStage)) {
						parallelNextSage();
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
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY || gameStage == GameStage.DEAL) {
				if (GameLogic.trySurrender(activePlayer)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
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
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.DEAL) {
				if (GameLogic.tryDeal(activePlayer, bid, deck)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
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
	public void insurance(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryInsurance(dealer, activePlayer)) {
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

	public void stay(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryStay(activePlayer)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
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
	 * @return
	 */
	public Player join(User user) {
		if (gameStage != GameStage.UNACTIVE) {
			int nextPlayersCount = playersCount.incrementAndGet();
			if (nextPlayersCount <= maxPlayers) {
				Player player = new Player(user.getId(), false);
				players.add(player);
				modify();
				return player;
			} else {
				playersCount.decrementAndGet();
				return null;
			}
		} else {
			return null;
		}
	}

	public void leave(User user) {
		Player player = players.get(indexOfPlayer(user.getId()));
		leavingPlayers.add(player);
		switch (player.getStage()) {
		case UNACTIVE:
		case DEAL:
			while (player == activePlayer) {
				try {
					this.surrender(user);
				} catch (GameActionDeniedException e) {
					log.error("Game exception at user " + user.getId().getValue() + " leaving.", e);
				}
			}
			player.setStage(GameStage.DONE);
			break;
		case PLAY:
			while (player == activePlayer) {
				try {
					this.stay(user);
				} catch (GameActionDeniedException e) {
					log.error("Game exception at user " + user.getId().getValue() + " leaving.", e);
				}
			}
			player.setStage(GameStage.RESULT);
			break;
		case RESULT:
			break;
		case DONE:
			break;
		}
		modify();
	}

	private void parallelNextSage() {
		BlackJackGame thisGame = this;
		Thread thread = new Thread() {
			private BlackJackGame game = thisGame;

			@Override
			public void run() {
				thisGame.lock.lock();
				thisGame.nextStage();
				thisGame.lock.unlock();
			}
		};
		thread.start();
	}

	private void nextStage() {
		StringBuilder debugStr;

		if (activePlayer != null) {
			activePlayer.resetActiveHand();
			activePlayer.setActive(false);
			activePlayer = null;
		}

		GameStage newStage = gameStage = gameStage.nextState();
		for (Player player : players) {
			GameStage plStage = player.getStage();
			if ((plStage != GameStage.UNACTIVE && plStage.compareTo(gameStage) < 0) || gameStage == GameStage.DEAL) {
				player.setStage(gameStage);
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
				GameLogic.dealerHit(dealer, deck);
				// just lets play
			}
			break;
		case RESULT:
			if (!nextHand(gameStage)) {
				nextStage();
			} else {
				// dealer plays and calcResults
				GameLogic.dealerPlay(dealer, deck);
				calcResults();
				nextStage();
			}
			break;
		case DONE:
			if (!nextHand(gameStage)) {
				if (players.size() == 0) {
					log.debug("Game id: " + this.id.getValue() + " " + newStage + " empty -> finish");
					this.finishEmptyGame();
				}
			} else {
				payOut();
				modify();
				try {
					// sleep for user to look results
					Thread.sleep(4000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (Player player : leavingPlayers) {
				while (players.remove(player)) {
					playersCount.decrementAndGet();
				}
				GamePool.getInstance().remove(player.getUserId(), this);
			}
			leavingPlayers.clear();
			if (players.size() > 0) {
				nextStage();
			}
			break;
		case UNACTIVE:
			log.warn("wrong game state after nextStage()");
		}
	}

	private void finishEmptyGame() {
		gameStage = GameStage.UNACTIVE;
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
				GameLogic.payOut(id, activePlayer, playerHand);
			} while (nextHand(GameStage.DONE));
		}

	}

	public GameId getId() {
		return id;
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
		return maxPlayers - playersCount.get();
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

	/**
	 * @return the gameStage
	 */
	public GameStage getGameStage() {
		return gameStage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

}