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
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

public class GameLogic {

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
		if (activeHand != null && activeHand.size() == 2) {
			BigDecimal betSize = activeHand.getBid();
			Hand newHand = new Hand();
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			try {
				boolean lockResult = userDAO.lockBalance(player.getUserId(), betSize);
				if (lockResult) {
					Card oldCard = activeHand.takeLastCard();
					activeHand.addCard(deck.pullCard());
					
					newHand.addCard(oldCard);
					newHand.addCard(deck.pullCard());
					
					newHand.setBid(betSize);
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

		return false;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean tryInsurance(Player player) {
		return false;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean trySurrender(Player player) {
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param deck
	 * @return
	 */
	public static boolean tryHit(Player player, Deck deck) {
		return false;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean tryStand(Player player) {
		return false;
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
	public boolean canInsurance(Dealer dealer, Player activePlayer, Hand activeHand) {
		boolean result = false;
		Hand hand = dealer.getHand();
		List<Card> cards = hand.getCardsListCopy();
		if (hand != null && cards != null && cards.size() == 2) {
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
		if (!score.isBlackJack()) {
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
		return (9 <= score.getValue() && score.getValue() <= 11);
	}

	/**
	 * player can Stand hand if hand score is lesser than or equal to 21 and
	 * isnt Blackjack;
	 * 
	 * @param activeHand
	 * @return
	 */
	private static boolean canStand(Hand activeHand) {
		return !activeHand.getScore().isBlackJack() && activeHand.getScore().getValue() <= 21;
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
		return cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
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

	/**
	 * 
	 * @param activeHand
	 */
	public static boolean payOut(Hand activeHand) {
		if (activeHand.getStage() == GameStage.RESULT) {
			activeHand.setStage(GameStage.DONE);
		}

		return false;
	}
}
