package org.qqq175.blackjack.game;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.game.impl.Card;
import org.qqq175.blackjack.game.impl.Dealer;
import org.qqq175.blackjack.game.impl.Hand;
import org.qqq175.blackjack.game.impl.Player;
import org.qqq175.blackjack.game.impl.Score;
import org.qqq175.blackjack.persistence.entity.User;

public class GameUtil {
	public static void changeScore(Hand hand) {
		int aces = 0;
		int total = 0;
		Score score = hand.getScore();
		List<Card> cards = hand.getCards();
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
	}

	public static JSONObject toJSON(BlackJackGame game, User user) {
		JSONObject result = new JSONObject();
		JSONArray players = new JSONArray();
		for (Player player : game.getPlayers()) {
			boolean isCurrentUser = player.getUserId().equals(user.getId());
			boolean isActive = player == game.getActivePlayer();
			players.add(toJSON(player, isActive, isCurrentUser));
		}
		result.put("dealer", toJSON(game.getDealer()));
		result.put("players", players);

		// controls
		// ---balance
		// ------value
		// ---actions
		// ------surrender(isActive)
		// ------split(isActive)
		// ------insurance(isActive)
		// ------double(isActive)
		// ------hit(isActive)
		// ------deal(isActive)
		// ------stay(isActive)
		// ---bid(isActive)

		return result;
	}

	public static JSONObject toJSON(Dealer dealer) {
		JSONObject result = new JSONObject();

		return result;
	}

	public static JSONObject toJSON(Player player, boolean isActive, boolean isCurrentUser) {
		JSONObject result = new JSONObject();
		// id
		// name
		// img
		// hands
		// isActive

		return result;
	}

	public static JSONObject toJSON(Hand hand, boolean isPlayer) {
		JSONObject result = new JSONObject();
		// score
		// cards
		// ::for player
		// bet
		// isActive
		return result;
	}

	public static JSONObject toJSON(Score score) {
		JSONObject result = new JSONObject();

		return result;
	}

	public static JSONObject toJSON(Card card) {
		JSONObject result = new JSONObject();

		return result;
	}
}
