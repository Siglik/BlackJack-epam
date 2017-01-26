package org.qqq175.blackjack.game;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.game.impl.Card;
import org.qqq175.blackjack.game.impl.Dealer;
import org.qqq175.blackjack.game.impl.Deck;
import org.qqq175.blackjack.game.impl.Hand;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.game.impl.Score;
import org.qqq175.blackjack.persistence.dao.AccountOperationDAO;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.UserstatDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.UserPool;

public class GameLogic {
	private final static BigDecimal BLACK_JACK_MULTIPLER = null;
	private final static BigDecimal WIN_MULTIPLER = null;
	private final static BigDecimal SURRENDER_MULTIPLER = null;

	/* DEALER OPERATIONS */

	public static void dealDealer(Dealer dealer, Deck deck) {
		// TODO Auto-generated method stub

	}

	public static void playDealer(Dealer dealer, Deck deck) {
		// TODO Auto-generated method stub

	}

	/* PLAYER OPERATIONS */
	/*
	 * ****** try ******
	 */
	/**
	 * 
	 * @param player
	 * @param betSize
	 * @param deck
	 * @return
	 */
	public static boolean tryDeal(Player player, BigDecimal betSize, Deck deck) {
		boolean result = false;
		if (player.getStage() == GameStage.DEAL) {
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
					// TODO log
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
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

					newHand.setBid(betSize);
					activeHand.setFirstAction(true);
					newHand.setFirstAction(true);
					result = true;
				}
			} catch (DAOException e) {
				// TODO log
				e.printStackTrace();

			}
		}
		return result;
	}

	/**
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
				// TODO log
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean tryInsurance(Dealer dealer, Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canInsurance(dealer, player)) {
			BigDecimal betSize = activeHand.getBid();
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			boolean lockResult;
			try {
				lockResult = userDAO.lockBalance(player.getUserId(), betSize);
				if (lockResult) {
					activeHand.setInsurance(betSize);
					if (activeHand.isFirstAction()) {
						activeHand.setFirstAction(false);
					}
					result = true;
				}
			} catch (DAOException e) {
				// TODO log
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean trySurrender(Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canSurrender(activeHand)) {
			if (activeHand.getStage() == GameStage.PLAY) {
				activeHand.setStage(GameStage.RESULT);
				activeHand.setResult(GameResult.SURRENDER);
				GameLogic.payOut(player, activeHand);
			} else if (activeHand.getStage() == GameStage.DEAL) {
				activeHand.setStage(GameStage.DONE);
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
	 * 
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
	 * 
	 * @param player
	 * @return
	 */
	public static boolean tryStand(Player player) {
		boolean result = false;
		Hand activeHand = player.getActiveHand();
		if (activeHand != null && canStand(activeHand)) {
			activeHand.setStage(GameStage.RESULT);
			if (activeHand.isFirstAction()) {
				activeHand.setFirstAction(false);
			}
			result = true;
		}
		return result;
	}

	/*
	 * ****** can ******
	 */

	/**
	 * 
	 * @param activePlayer
	 * @return
	 */
	public static boolean canDeal(Player activePlayer) {
		boolean result = false;
		if (activePlayer.getStage() == GameStage.DEAL) {
			Hand activeHand = activePlayer.getActiveHand();
			result = activeHand != null && activeHand.size() == 0;
		}

		return result;
	}

	public static boolean canDouble(Player activePlayer) {
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
	 * 
	 * @param activePlayer
	 * @return
	 */
	public static boolean canStand(Player activePlayer) {
		boolean result = false;
		if (activePlayer.getStage() == GameStage.PLAY) {
			Hand activeHand = activePlayer.getActiveHand();
			result = canStand(activeHand);
		}

		return result;
	}

	/**
	 * Player can insurance if dealer first card is Ace and player has enough
	 * money
	 * 
	 * @param dealer
	 * @param activePlayer
	 * @param activeHand
	 * @return
	 */
	public static boolean canInsurance(Dealer dealer, Player activePlayer) {
		boolean result = false;
		Hand hand = dealer.getHand();
		Hand activeHand = activePlayer.getActiveHand();
		if (hand != null) {
			List<Card> cards = hand.getCardsListCopy();
			if (activeHand != null && activeHand.getInsurance().equals(new BigDecimal(0.0)) && cards != null && cards.size() == 2) {
				Card firstCard = cards.get(0);
				if (firstCard.getRank() == Card.Rank.ACE) {
					User user = UserPool.getInstance().get(activePlayer.getUserId());
					BigDecimal bid = activeHand.getBid();
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
	 * 
	 * @param activePlayer
	 * @return
	 */
	public static boolean canSurrender(Player activePlayer) {
		boolean result = false;
		Hand activeHand = activePlayer.getActiveHand();
		if (activeHand != null) {
			result = canSurrender(activeHand);
		}

		return result;
	}

	/**
	 * 
	 * @param activePlayer
	 * @return
	 */
	public static boolean canHit(Player activePlayer) {
		Hand hand = activePlayer.getActiveHand();
		if (hand != null) {
			return canHit(hand);
		} else {
			return false;
		}
	}

	/**
	 * Player can split he has less than 4 hands and enough money
	 * 
	 * @param activePlayer
	 * @return
	 */
	public static boolean canSplit(Player activePlayer) {
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
	 * Player can hit hand if score is less than 21
	 * 
	 * @param activeHand
	 * @return
	 */
	public static boolean canHit(Hand activeHand) {
		Score score = activeHand.getScore();
		if (activeHand.getStage() == GameStage.PLAY && !score.isBlackJack()) {
			if (score.getValue() < 21) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
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
	 * player can Stand hand if hand score is lesser than or equal to 21 and
	 * isnt Blackjack;
	 * 
	 * @param activeHand
	 * @return
	 */
	private static boolean canStand(Hand activeHand) {
		return activeHand.getStage() == GameStage.PLAY && !activeHand.getScore().isBlackJack() && activeHand.getScore().getValue() <= 21;
	}

	/**
	 * player can split hand if it contain two cards and both cards have same
	 * rank
	 * 
	 * @param activeHand
	 * @return
	 */
	public static boolean canSplit(Hand activeHand) {
		List<Card> cards = activeHand.getCardsListCopy();
		return activeHand.getStage() == GameStage.PLAY && cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
	}

	/**
	 * player can surrender hand if only on first action and if it is not
	 * BlackJack (or if it lesser or equal 21? but at first action is impossible
	 * to have score greater than 21, so it's not necessary to check)
	 * 
	 * @param activeHand
	 * @return boolean is able to surrender
	 */
	public static boolean canSurrender(Hand activeHand) {
		return activeHand.getStage().compareTo(GameStage.RESULT) < 0 && activeHand.isFirstAction() && !activeHand.getScore().isBlackJack();
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

	/**
	 * 
	 * @param activeHand
	 */
	public static boolean payOut(Player activePlayer, Hand activeHand) {
		if (activeHand.getStage() == GameStage.RESULT) {
			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
			UserDAO userDAO = daoFactory.getUserDAO();
			UserstatDAO ustatDAO = daoFactory.getUserstatDAO();

			UserId userId = activePlayer.getUserId();
			BigDecimal betSize = activeHand.getBid();
			BigDecimal insurance = activeHand.getInsurance();
			switch (activeHand.getResult()) {
			case WIN:
				break;
			case BLACKJACK:
				break;
			case TIE:
				break;
			case LOSS:
				break;
			case LOSS_INSURANCE:
				break;
			case SURRENDER:
				break;
			case NONE:
				break;
			default:
				break;
			}
			activeHand.setStage(GameStage.DONE);
		}

		return false;
	}

	public static void calcHandResult(Hand playerHand, Hand dealerHand) {
		Score playerScore = playerHand.getScore();
		Score dealerScore = dealerHand.getScore();

		if (playerScore.isBlackJack()) {
			if (!dealerScore.isBlackJack()) {
				playerHand.setResult(GameResult.BLACKJACK);
			} else {
				playerHand.setResult(GameResult.TIE);
			}
		} else if (dealerScore.isBlackJack()) {
			if (playerHand.getInsurance().equals(new BigDecimal(0.0))) {
				playerHand.setResult(GameResult.LOSS);
			} else {
				playerHand.setResult(GameResult.LOSS_INSURANCE);
			}
		} else {
			if (playerScore.getValue() > dealerScore.getValue()) {
				playerHand.setResult(GameResult.WIN);
			} else if (playerScore.getValue() < dealerScore.getValue()) {
				playerHand.setResult(GameResult.LOSS);
			} else {
				playerHand.setResult(GameResult.TIE);
			}
		}
	}
}
