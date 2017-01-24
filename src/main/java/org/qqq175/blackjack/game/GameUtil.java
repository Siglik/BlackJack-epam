package org.qqq175.blackjack.game;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.impl.Card;
import org.qqq175.blackjack.game.impl.Dealer;
import org.qqq175.blackjack.game.impl.Hand;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.game.impl.Score;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

public class GameUtil {
	public static Score calcScore(Hand hand) {
		int aces = 0;
		int total = 0;
		Score score = new Score();
		List<Card> cards = hand.getCardsList();
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

	public boolean canInsurance(Dealer dealer, Player activePlayer, Hand activeHand) {
		Hand hand = dealer.getHand();
		List<Card> cards = hand.getCardsList();
		boolean result = false;
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

	public static boolean canHit(Player activePlayer) {
		return canHit(activePlayer.getActiveHand());
	}

	public static boolean canSplit(Player activePlayer) {
		if (activePlayer.getHands().size() <= 4) {
			return canSplit(activePlayer.getActiveHand());
		} else {
			return false;
		}
	}

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

	public static boolean canDouble(Hand activeHand) {
		Score score = activeHand.getScore();
		return (9 <= score.getValue() && score.getValue() <= 11);
	}

	public static boolean canSplit(Hand activeHand) {
		List<Card> cards = activeHand.getCardsList();
		return cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
	}

	public static boolean canSurrender(Hand activeHand) {
		return activeHand.isFirstAction();
	}
}
