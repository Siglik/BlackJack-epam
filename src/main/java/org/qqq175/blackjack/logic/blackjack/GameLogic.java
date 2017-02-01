package org.qqq175.blackjack.logic.blackjack;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.logic.blackjack.entity.Card;
import org.qqq175.blackjack.logic.blackjack.entity.Dealer;
import org.qqq175.blackjack.logic.blackjack.entity.Deck;
import org.qqq175.blackjack.logic.blackjack.entity.Hand;
import org.qqq175.blackjack.logic.blackjack.entity.Player;
import org.qqq175.blackjack.logic.blackjack.entity.Score;
import org.qqq175.blackjack.logic.player.AccountOperationLogic;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Contains game logic methods
 * 
 * @author qqq175
 */
public class GameLogic {
	private static final String UNABLE_TO_UPDATE_USER_S_BALANCE_OR_STATS_ACCOUNT_OPERATION_RESULT_IS = "Unable to update user's balance or stats. Account operation result is ";

	private static final String GAME_ACTION_ERROR = "Unable to perform game action. ";

	private static final int DEALER_PLAY_LIMIT = 17;

	private static Logger log = LogManager.getLogger(GameLogic.class);

	private final static BigDecimal INSURANCE_MULTIPLER_PAY = BigDecimal.valueOf(2.00);
	private final static BigDecimal INSURANCE_MULTIPLER_BID = BigDecimal.valueOf(0.50);
	private final static BigDecimal BLACK_JACK_MULTIPLER = BigDecimal.valueOf(1.50);
	private final static BigDecimal WIN_MULTIPLER = BigDecimal.ONE;
	private final static BigDecimal SURRENDER_MULTIPLER = BigDecimal.valueOf(0.50);
	private final static BigDecimal MINIMAL_BID = BigDecimal.valueOf(0.1);

	/* DEALER OPERATIONS */

	/**
	 * hit dealer 2 cards form deck
	 * 
	 * @param dealer
	 * @param deck
	 */
	public static void dealerHit(Dealer dealer, Deck deck) {
		Hand hand = dealer.getHand();

		hand.addCard(deck.pullCard());
		hand.addCard(deck.pullCard());
	}

	/**
	 * lets dealer play
	 * 
	 * @param dealer
	 * @param deck
	 */
	public static void dealerPlay(Dealer dealer, Deck deck) {
		Hand hand = dealer.getHand();
		dealer.setShowAllCards(true);
		while (hand.getScore().getValue() < DEALER_PLAY_LIMIT) {
			hand.addCard(deck.pullCard());
		}
	}

	/* PLAYER OPERATIONS */
	/*
	 * ****** try ******
	 */
	/**
	 * Try to perform DEAL game action. If success return true, else - false.
	 * 
	 * @param player
	 * @param betSize
	 * @param deck
	 * @return
	 */
	public static boolean tryDeal(Player player, BigDecimal betSize, Deck deck) {
		boolean result = false;
		if (player.getStage() == GameStage.DEAL && betSize.compareTo(MINIMAL_BID) >= 0) {
			Hand hand = player.getActiveHand();
			if (hand != null && hand.size() == 0) {
				UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
				try {
					boolean lockResult = userDAO.lockBalance(player.getUserId(), betSize);
					if (lockResult) {
						hand.setBid(betSize);
						hand.addCard(deck.pullCard());
						hand.addCard(deck.pullCard());
						result = true;
					}
				} catch (DAOException e) {
					log.error(GAME_ACTION_ERROR + e.getMessage(),e);
				}
			}
		}

		return result;
	}

	/**
	 * Try to perform SPLIT game action. If success return true, else - false.
	 * 
	 * @param player
	 * @param deck
	 * @return
	 */
	public static boolean trySplit(Player player, Deck deck) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canSplit(activeHand)) {
			BigDecimal betSize = activeHand.getBid();
			Hand newHand = new Hand();
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			try {
				boolean lockResult = userDAO.lockBalance(player.getUserId(), betSize);
				if (lockResult) {
					player.addHand(newHand);
					Card oldCard = activeHand.takeLastCard();
					activeHand.addCard(deck.pullCard());

					newHand.addCard(oldCard);
					newHand.addCard(deck.pullCard());
					newHand.setStage(GameStage.PLAY);

					newHand.setBid(betSize);
					activeHand.setActive(false);
					activeHand.setFirstAction(true);
					newHand.setFirstAction(true);
					result = true;
				}
			} catch (DAOException e) {
				log.error(GAME_ACTION_ERROR + e.getMessage(),e);
				e.printStackTrace();

			}
		}
		return result;
	}

	/**
	 * Try to perform DOUBLE game action. If success return true, else - false.
	 * 
	 * @param player
	 * @param deck
	 * @return
	 */
	public static boolean tryDouble(Player player, Deck deck) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canDouble(player)) {
			BigDecimal betSize = activeHand.getBid();
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();

			boolean lockResult;
			try {
				lockResult = userDAO.lockBalance(player.getUserId(), betSize);
				if (lockResult) {
					activeHand.setBid(betSize.add(betSize));
					activeHand.addCard(deck.pullCard());
					activeHand.setStage(GameStage.RESULT);
					if (activeHand.isFirstAction()) {
						activeHand.setFirstAction(false);
					}
					result = true;
				}
			} catch (DAOException e) {
				log.error(GAME_ACTION_ERROR + e.getMessage(),e);
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Try to perform INSURANCE game action. If success return true, else -
	 * false.
	 * 
	 * @param player
	 * @return
	 */
	public static boolean tryInsurance(Dealer dealer, Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canInsurance(dealer, player)) {
			BigDecimal betSize = activeHand.getBid().multiply(INSURANCE_MULTIPLER_BID);
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			boolean lockResult;
			try {
				lockResult = userDAO.lockBalance(player.getUserId(), betSize);
				if (lockResult) {
					activeHand.setInsurance(betSize);
					if (activeHand.isFirstAction()) {
						activeHand.setFirstAction(false);
					}
					if (activeHand.getScore().isBlackJack()) {
						activeHand.setStage(GameStage.RESULT);
					}
					result = true;
				}
			} catch (DAOException e) {
				log.error(GAME_ACTION_ERROR + e.getMessage(),e);
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Try to perform SURRENDER game action for active Hand. If success return true, else -
	 * false. 
	 * 
	 * @param player
	 * @return
	 */
	public static boolean trySurrender(Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canSurrender(activeHand)) {
			if (activeHand.getStage() == GameStage.PLAY) {
				activeHand.setStage(GameStage.DONE);
				activeHand.setResult(GameResult.SURRENDER);
			} else if (activeHand.getStage() == GameStage.DEAL) {
				activeHand.setStage(GameStage.UNACTIVE);
				activeHand.setResult(GameResult.NONE);
			}
			if (activeHand.isFirstAction()) {
				activeHand.setFirstAction(false);
			}
			result = true;
		}
		return result;
	}

	/**
	 * Try to perform SURRENDER game action for active Hand if player not leaving, or to all players hand if leaving game. If success return true, else -
	 * false. 
	 * @param player
	 * @param isLeaving - is surrender caused by player's leaving
	 * @return
	 */
	public static boolean trySurrender(Player player, boolean isLeaving) {
		boolean result = false;
		if (isLeaving) {
			for (Hand hand : player.getHandsListCopy()) {
				if (hand != null) {
					if (hand.getScore().getValue() > 0) {
						hand.setStage(GameStage.DONE);
						hand.setResult(GameResult.SURRENDER);
					} else {
						hand.setStage(GameStage.UNACTIVE);
						hand.setResult(GameResult.NONE);
					}
					if (hand.isFirstAction()) {
						hand.setFirstAction(false);
					}
				}
			}
			result = true;

		} else {
			result = trySurrender(player);
		}
		return result;
	}

	/**
	 * Try to perform HIT game action for active Hand. If success return true, else -
	 * false. 
	 * @param player
	 * @param deck
	 * @return
	 */
	public static boolean tryHit(Player player, Deck deck) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canHit(activeHand)) {
			activeHand.addCard(deck.pullCard());
			if (activeHand.isFirstAction()) {
				activeHand.setFirstAction(false);
			}
			result = true;
		}

		return result;
	}

	/**
	 * Try to perform STAY game action for active Hand. If success return true, else -
	 * false. 
	 * @param player
	 * @return
	 */
	public static boolean tryStay(Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canStay(activeHand)) {
			activeHand.setStage(GameStage.RESULT);
			if (activeHand.isFirstAction()) {
				activeHand.setFirstAction(false);
			}
			result = true;
		}
		return result;
	}

	/**
	 * Try to perform STAY game action for active Hand if player not leaving, or to all players hand if leaving game. If success return true, else -
	 * false. 
	 * @param player
	 * @param isLeaving - is surrender caused by player's leaving	 * @return
	 */
	public static boolean tryStay(Player player, boolean isLeaving) {
		boolean result = false;
		if (isLeaving) {
			for (Hand hand : player.getHandsListCopy()) {
				if (hand != null) {
					if (hand.getScore().getValue() > 0) {
						hand.setStage(GameStage.RESULT);
						if (hand.isFirstAction()) {
							hand.setFirstAction(false);
						}
					} else {
						hand.setStage(GameStage.UNACTIVE);
					}
				}
			}
			result = true;

		} else {
			result = tryStay(player);
		}
		return result;
	}
	/*
	 * ****** can ******
	 */

	/**
	 * Check is user able to DEAL
	 * @param activePlayer
	 * @return
	 */
	public static boolean canDeal(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		if (activePlayer.getStage() == GameStage.DEAL) {
			Hand activeHand = activePlayer.getActiveHand();
			result = activeHand != null && activeHand.size() == 0;
		}

		return result;
	}

	public static boolean canDouble(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		User user = UserPool.getInstance().get(activePlayer.getUserId());
		Hand hand = activePlayer.getActiveHand();
		if (user != null && hand != null) {
			if (canDouble(hand)) {
				result = user.getAccountBalance().compareTo(hand.getBid()) >= 0;
			}
		}
		return result;
	}

	/**
	 * Check is user able to STAY
	 * @param activePlayer
	 * @return
	 */
	public static boolean canStay(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		if (activePlayer.getStage() == GameStage.PLAY) {
			Hand activeHand = activePlayer.getActiveHand();
			result = canStay(activeHand);
		}

		return result;
	}

	/**
	 * Check is user able to INSURANCE
	 * Player can insurance if dealer first card is Ace and player has enough
	 * money
	 * 
	 * @param dealer
	 * @param activePlayer
	 * @param activeHand
	 * @return
	 */
	public static boolean canInsurance(Dealer dealer, Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		Hand dealerHand = dealer.getHand();
		Hand activeHand = activePlayer.getActiveHand();
		if (dealerHand != null) {
			List<Card> dealerCards = dealerHand.getCardsListCopy();
			if (activeHand != null && activeHand.isFirstAction()
					&& activeHand.getInsurance().equals(new BigDecimal(0.0)) && dealerCards != null
					&& dealerCards.size() == 2) {
				Card firstCard = dealerCards.get(0);
				if (firstCard.getRank() == Card.Rank.ACE) {
					User user = UserPool.getInstance().get(activePlayer.getUserId());
					BigDecimal bid = activeHand.getBid().multiply(INSURANCE_MULTIPLER_BID);
					BigDecimal balance = user.getAccountBalance();
					if (bid.compareTo(balance) <= 0) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Check is user able to SURRENDER
	 * @param activePlayer
	 * @return
	 */
	public static boolean canSurrender(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		Hand activeHand = activePlayer.getActiveHand();
		if (activeHand != null) {
			result = canSurrender(activeHand);
		}

		return result;
	}

	/**
	 * Check is user able to HIT
	 * @param activePlayer
	 * @return
	 */
	public static boolean canHit(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		Hand hand = activePlayer.getActiveHand();
		if (hand != null) {
			return canHit(hand);
		} else {
			return false;
		}
	}

	/**
	 * Check is user able to SPLIT
	 * Player can split he has less than 4 hands and enough money
	 * @param activePlayer
	 * @return
	 */
	public static boolean canSplit(Player activePlayer) {
		if (activePlayer == null) {
			return false;
		}
		boolean result = false;
		User user = UserPool.getInstance().get(activePlayer.getUserId());
		Hand hand = activePlayer.getActiveHand();
		if (user != null && hand != null) {
			if (user.getAccountBalance().compareTo(hand.getBid()) >= 0) {
				if (activePlayer.handsCount() < 4) {
					result = canSplit(activePlayer.getActiveHand());
				}
			}
		}
		return result;
	}

	/* HAND OPERATIONS */
	/**
	 * Check is hand able to HIT
	 * Player can hit hand if score is less than 21
	 * 
	 * @param activeHand
	 * @return
	 */
	public static boolean canHit(Hand activeHand) {
		Score score = activeHand.getScore();
		if (activeHand.getStage() == GameStage.PLAY && !score.isBlackJack() && score.getValue() < 21) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Check is hand able to DOUBLE
	 * player can double hand if hand score beetween 9 and 11
	 * 
	 * @param activeHand
	 * @return
	 */
	public static boolean canDouble(Hand activeHand) {
		Score score = activeHand.getScore();
		return (activeHand.getStage() == GameStage.PLAY && 9 <= score.getValue() && score.getValue() <= 11);
	}

	/**
	 * Check is hand able to STAY
	 * player can STAY hand if hand score is lesser than or equal to 21 and
	 * isnt Blackjack;
	 * 
	 * @param activeHand
	 * @return
	 */
	private static boolean canStay(Hand activeHand) {
		return activeHand.getStage() == GameStage.PLAY;
	}

	/**
	 * Check is hand able to SPLIT
	 * player can split hand if it contain two cards and both cards have same
	 * rank
	 * 
	 * @param activeHand
	 * @return
	 */
	public static boolean canSplit(Hand activeHand) {
		List<Card> cards = activeHand.getCardsListCopy();
		return activeHand.getStage() == GameStage.PLAY && cards.size() == 2
				&& cards.get(0).getRank().equals(cards.get(1).getRank());
	}

	/**
	 * Check is hand able to SURRENDER
	 * player can surrender hand if only on first action and if it is not
	 * BlackJack (or if it lesser or equal 21? but at first action is impossible
	 * to have score greater than 21, so it's not necessary to check)
	 * 
	 * @param activeHand
	 * @return boolean is able to surrender
	 */
	public static boolean canSurrender(Hand activeHand) {
		return activeHand.getStage().compareTo(GameStage.RESULT) < 0 && activeHand.isFirstAction()
				&& !activeHand.getScore().isBlackJack();
	}

	/**
	 * Calculate hand hard(every ace except first is 1 point, first is 11 point
	 * if final score less than or equal to 21 or 1 point if score greather than
	 * 21) score.
	 * 
	 * @param hand
	 * @return calculated Score
	 */
	public static Score calcScore(Hand hand) {
		int aces = 0;
		int total = 0;
		Score score = new Score();
		List<Card> cards = hand.getCardsListCopy();
		boolean isBlackJack = false;
		for (Card card : cards) {
			total += card.getValue();
			if (card.getRank() == Card.Rank.ACE) {
				aces++;
			}
		}
		if (cards.size() == 2 && aces == 1 && total == 21) {
			isBlackJack = true;
		} else {
			while (total > 21 && aces > 0) {
				total -= 10;
				aces--;
			}
		}
		score.setBlackJack(isBlackJack);
		score.setValue(total);

		return score;
	}

	/* OTHER OPERATIONS */

	/**
	 * Make hand pay out depends on hand GameResult 
	 * @param gameId
	 * @param activePlayer
	 * @param activeHand
	 * @return
	 */
	public static boolean payOut(GameId gameId, Player activePlayer, Hand activeHand) {
		boolean result = false;
		if (activeHand.getStage() == GameStage.DONE) {
			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			AccountOperationLogic aoLogic = new AccountOperationLogic();
			UserstatDAO ustatDAO = daoFactory.getUserstatDAO();

			UserId userId = activePlayer.getUserId();
			BigDecimal betSize = activeHand.getBid();
			BigDecimal insurance = activeHand.getInsurance();
			GameResult gameResult = activeHand.getResult();
			String comment = "Game: " + gameId.getValue() + " Result: " + gameResult.name() + " Bet: " + betSize
					+ " Insurance: " + insurance;
			AccountOperationLogic.Result operResult = null;

			try {

				switch (gameResult) {

				case BLACKJACK:
					operResult = aoLogic.doGamePayment(betSize.multiply(BLACK_JACK_MULTIPLER), betSize, BigDecimal.ZERO,
							userId, AccountOperation.Type.WIN, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementBlackjack(userId);
						ustatDAO.incrementWin(userId);
						result = true;
					}
					break;
				case WIN:
					operResult = aoLogic.doGamePayment(betSize.multiply(WIN_MULTIPLER), betSize, insurance, userId,
							AccountOperation.Type.WIN, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementWin(userId);
						result = true;
					}
					break;
				case BLACKJACK_INSURANCE:
					operResult = aoLogic.doGamePayment(insurance.multiply(INSURANCE_MULTIPLER_PAY),
							betSize.add(insurance), BigDecimal.ZERO, userId, AccountOperation.Type.WIN, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementBlackjack(userId);
						ustatDAO.incrementWin(userId);
						result = true;
					}
					break;
				case LOSS_WIN_INSURANCE:
					operResult = aoLogic.doGamePayment(insurance.multiply(INSURANCE_MULTIPLER_PAY), insurance, betSize,
							userId, AccountOperation.Type.WIN, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementLoss(userId);
						result = true;
						result = true;
					}
					break;
				case TIE:
					operResult = aoLogic.doGamePayment(BigDecimal.ZERO, betSize, BigDecimal.ZERO, userId, null,
							comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementTie(userId);
						result = true;
					}
					break;
				case TIE_LOSS_INSURANCE:
					operResult = aoLogic.doGamePayment(BigDecimal.ZERO, betSize, insurance, userId,
							AccountOperation.Type.LOSS, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementTie(userId);
						result = true;
					}
					break;
				case LOSS:
					operResult = aoLogic.doGamePayment(BigDecimal.ZERO, BigDecimal.ZERO, betSize.add(insurance), userId,
							AccountOperation.Type.LOSS, comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementLoss(userId);
						result = true;
					}
					break;
				case SURRENDER:
					operResult = aoLogic.doGamePayment(BigDecimal.ZERO, betSize.multiply(SURRENDER_MULTIPLER),
							betSize.subtract(betSize.multiply(SURRENDER_MULTIPLER)), userId, AccountOperation.Type.LOSS,
							comment);
					if (operResult == AccountOperationLogic.Result.OK) {
						ustatDAO.incrementLoss(userId);
						result = true;
					}
					break;
				case NONE:
					break;
				default:
					break;
				}
				activeHand.setStage(GameStage.UNACTIVE);
				ModifyUserLogic muLogic = new ModifyUserLogic();
				muLogic.updateUserInPool(userId);
			} catch (DAOException e) {
				log.error(UNABLE_TO_UPDATE_USER_S_BALANCE_OR_STATS_ACCOUNT_OPERATION_RESULT_IS + operResult, e);
			} finally {
			}
		}

		return result;
	}

	/**
	 * Calculate hand GameResult out based on hand's Score and dealer's hand's Score  
	 * @param playerHand
	 * @param dealerHand
	 */
	public static void calcHandResult(Hand playerHand, Hand dealerHand) {
		Score playerScore = playerHand.getScore();
		Score dealerScore = dealerHand.getScore();

		if (playerScore.isBlackJack()) {
			if (!dealerScore.isBlackJack()) {
				playerHand.setResult(GameResult.BLACKJACK);
			} else {
				if (playerHand.getInsurance().equals(BigDecimal.ZERO)) {
					playerHand.setResult(GameResult.TIE);
				} else {
					playerHand.setResult(GameResult.BLACKJACK_INSURANCE);
				}
			}
		} else if (dealerScore.isBlackJack()) {
			if (playerHand.getInsurance().equals(BigDecimal.ZERO)) {
				playerHand.setResult(GameResult.LOSS);
			} else {
				playerHand.setResult(GameResult.LOSS_WIN_INSURANCE);
			}
		} else {
			if (playerScore.getValue() <= 21 && dealerScore.getValue() <= 21) {
				if (playerScore.getValue() > dealerScore.getValue()) {
					playerHand.setResult(GameResult.WIN);
				} else if (playerScore.getValue() < dealerScore.getValue()) {
					playerHand.setResult(GameResult.LOSS);
				} else {
					if (playerHand.getInsurance().equals(BigDecimal.ZERO)) {
						playerHand.setResult(GameResult.TIE);
					} else {
						playerHand.setResult(GameResult.TIE_LOSS_INSURANCE);
					}
				}
			} else if (playerScore.getValue() > 21) {
				playerHand.setResult(GameResult.LOSS);
			} else if (dealerScore.getValue() > 21) {
				playerHand.setResult(GameResult.WIN);
			}
		}
	}
}
