package org.qqq175.blackjack.logic.blackjack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.logic.blackjack.entity.Dealer;
import org.qqq175.blackjack.logic.blackjack.entity.Deck;
import org.qqq175.blackjack.logic.blackjack.entity.Hand;
import org.qqq175.blackjack.logic.blackjack.entity.Player;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.GamePool;

/**
 * Contains game state and main game logic
 * 
 * @author qqq175
 */
public class BlackJackGame {
	private static final String UNEXPECTED_INTERRUPT = "Unexpected interrupt";
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

	/**
	 * create standard 6 deck multiplayer game
	 * 
	 * @param gameId
	 * @param creator
	 *            - id of user that created game
	 */
	private BlackJackGame(GameId gameId, User creator) {
		this(gameId, 6, MAX_PLAYERS, creator);
	}

	/**
	 * create generic 6 deck game
	 * 
	 * @param gameId
	 * @param creator
	 *            - user that created game
	 * @param maxPlayers
	 *            - max game players
	 */
	private BlackJackGame(GameId gameId, User creator, int maxPlayers) {
		// max players can't be greater than MAX_PLAYERS constant
		this(gameId, 6, maxPlayers <= MAX_PLAYERS ? maxPlayers : MAX_PLAYERS, creator);
	}

	/**
	 * create generic game
	 * 
	 * @param gameId
	 * @param decksCount
	 *            - deck count
	 * @param maxPlayers
	 *            - max game players
	 * @param creator
	 *            - id of user that created game
	 */
	private BlackJackGame(GameId gameId, int decksCount, int maxPlayers, User creator) {
		this.id = gameId;
		this.deck = new Deck(decksCount);
		this.maxPlayers = maxPlayers;

		this.players = new CopyOnWriteArrayList<>();
		this.leavingPlayers = new CopyOnWriteArrayList<>();

		Player player = new Player(creator.getId(), true);
		this.players.add(player);
		this.activePlayer = null;
		this.playersCount = new AtomicInteger(1);

		this.gameStage = GameStage.UNACTIVE;
		this.lock = new ReentrantLock();
	}

	/**
	 * create generic 6 deck game
	 * 
	 * @param gameId
	 * @param creator
	 *            - user that created game
	 * @param maxPlayers
	 *            - max game players
	 * @return
	 */
	public static BlackJackGame createGame(GameId gameId, User creator, int maxPlayers) {
		BlackJackGame instance = new BlackJackGame(gameId, creator, maxPlayers);
		instance.nextStage();
		return instance;
	}

	/**
	 * create standard 6 deck multiplayer game
	 * 
	 * @param gameId
	 * @param creator
	 *            - id of user that created game
	 */
	public static BlackJackGame createGame(GameId gameId, User creator) {
		BlackJackGame instance = new BlackJackGame(gameId, creator);
		instance.nextStage();
		return instance;
	}

	/**
	 * iterate hands to next. (with hand state equals game state)
	 * 
	 * @param stage
	 * @return next hand, or null if not found
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
	 * iterate players to next. (with player state equals game state)
	 * 
	 * @param stage
	 * @return next player, or null if not found
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
	 * game action - hit.
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
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
	 * Game action - double. Double hand and stay
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
	 */
	public void doubleBet(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryDouble(activePlayer, deck)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
					}

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
	 * Game action - split.
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
	 */
	public void split(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.trySplit(activePlayer, deck)) {
					activePlayer.resetActiveHand();
					if (!nextHand(gameStage)) {
						parallelNextSage();
					}

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
	 * Game action - surrender. after surrender hand wait for game end to get
	 * money back
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
	 */
	public void surrender(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY || gameStage == GameStage.DEAL) {
				if (GameLogic.trySurrender(activePlayer)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
					}

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
	 * Game action - deal.
	 * 
	 * @param user
	 * @param bid
	 *            - bid size
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
	 */
	public void deal(User user, BigDecimal bid) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.DEAL) {
				if (GameLogic.tryDeal(activePlayer, bid, deck)) {
					if (!nextHand(gameStage)) {
						parallelNextSage();
					}

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
	 * Game action - insurance.
	 * 
	 * @param user
	 * @throws GameActionDeniedException
	 *             - thrown if user not allowed perform this action because of
	 *             some reason
	 */
	public void insurance(User user) throws GameActionDeniedException {
		if (activePlayer != null && user.getId().equals(activePlayer.getUserId())) {
			if (gameStage == GameStage.PLAY) {
				if (GameLogic.tryInsurance(dealer, activePlayer)) {

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
	 * Try to join user into a game
	 * 
	 * @param user
	 * @return
	 */
	public Player join(User user) {
		lock.lock();
		if (gameStage != GameStage.UNACTIVE) {
			int nextPlayersCount = playersCount.incrementAndGet();
			if (nextPlayersCount <= maxPlayers) {
				Player player = new Player(user.getId(), false);
				players.add(player);
				lock.unlock();
				return player;
			} else {
				playersCount.decrementAndGet();
				lock.unlock();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Leave user from game.
	 * 
	 * @param user
	 */
	public void leave(User user) {
		Player player = players.get(indexOfPlayer(user.getId()));
		leavingPlayers.add(player);
		switch (player.getStage()) {
		case UNACTIVE:
		case DEAL:
			GameLogic.trySurrender(player, true);
			while (player == activePlayer) {
				if (!nextHand(gameStage)) {
					parallelNextSage();
				}
			}
			player.setStage(GameStage.DONE);
			break;
		case PLAY:
			GameLogic.tryStay(player, true);
			while (player == activePlayer) {
				if (!nextHand(gameStage)) {
					parallelNextSage();
				}
			}
			player.setStage(GameStage.RESULT);
			break;
		case RESULT:
			break;
		case DONE:
			break;
		}

	}

	/**
	 * Perform next stage in another thread (to decrease user's responce time).
	 */
	private void parallelNextSage() {
		BlackJackGame thisGame = this;
		Thread thread = new Thread() {
			@Override
			public void run() {
				thisGame.lock.lock();
				thisGame.nextStage();
				thisGame.lock.unlock();
			}
		};
		thread.start();
	}

	/**
	 * Change game state to next, and perform all necessary operations (leave,
	 * join, pay out, calculate result, dealer play, etc.)
	 */
	private void nextStage() {

		/*
		 * if somehow user state is not nul - reset
		 */
		if (activePlayer != null) {
			log.warn("Active user is'nt null!");
			activePlayer.resetActiveHand();
			activePlayer.setActive(false);
			activePlayer = null;
		}

		GameStage newStage = gameStage = gameStage.nextState();

		/*
		 * next stage active Players. On game stage DEAL activete UNACTIVE
		 * players
		 */
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
				deck.newRound();
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
				try {
					// sleep for user to look results
					Thread.sleep(4000L);
				} catch (InterruptedException e) {
					log.warn(UNEXPECTED_INTERRUPT, e);
				}
				payOut();
			}
			/* leave players */
			for (Player player : leavingPlayers) {
				while (players.remove(player)) {
					playersCount.decrementAndGet();
				}
				GamePool.getInstance().remove(player.getUserId(), this);
			}
			leavingPlayers.clear();
			/* next stage if is not empty */
			lock.lock();
			if (players.size() > 0) {
				lock.unlock();
				nextStage();
			} else {
				finishEmptyGame();
				lock.unlock();
			}
			break;
		case UNACTIVE:
			log.warn("wrong game state after nextStage()");
		}
	}

	/**
	 * set game to UNACTIVE state
	 */
	private void finishEmptyGame() {
		gameStage = GameStage.UNACTIVE;
	}

	/**
	 * calculate game results
	 */
	private void calcResults() {
		if (gameStage.equals(GameStage.RESULT)) {
			Hand dealerHand = dealer.getHand();
			do {
				Hand playerHand = activePlayer.getActiveHand();
				GameLogic.calcHandResult(playerHand, dealerHand);
			} while (nextHand(GameStage.RESULT));
		}
	}

	/**
	 * do pay out
	 */
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

	/**
	 * @return copy of players list
	 */
	public List<Player> getPlayersList() {
		List<Player> copyOfPlayers = new ArrayList<>(players);
		return copyOfPlayers;
	}

	public int getPlayersCount() {
		return playersCount.get();
	}

	/**
	 * return count of available to enter palyers slots
	 * 
	 * @return
	 */
	public int getFreeSlots() {
		return maxPlayers - playersCount.get();
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * Check if user already in game
	 * 
	 * @param user
	 * @return
	 */
	public boolean isInGame(User user) {
		return indexOfPlayer(user.getId()) != -1;
	}

	/**
	 * find user in players list and return id
	 * 
	 * @param userId
	 * @return
	 */
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

	/**
	 * @return the maxPlayers
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

}