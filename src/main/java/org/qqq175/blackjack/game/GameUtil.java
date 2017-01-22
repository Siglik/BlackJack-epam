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
import org.qqq175.blackjack.persistence.dao.util.PhotoManager;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

@SuppressWarnings("unchecked")
public class GameUtil {
	public static Score calcScore(Hand hand) {
		int aces = 0;
		int total = 0;
		Score score = new Score();
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

		return score;
	}

	public boolean isInsuranceable(Dealer dealer) {
		Hand hand = dealer.getHand();
		if (hand != null && hand.getCards() != null && hand.getCards().size() == 2) {
			Card firstCard = hand.getCards().get(0);

			return firstCard.getRank() == Card.Rank.ACE;
		} else {
			return false;
		}
	}

	public static JSONObject toJSON(BlackJackGame game, User user) {
		JSONObject result = new JSONObject();
		JSONArray players = new JSONArray();
		boolean isUserActive = user.getId().equals(game.getActivePlayer().getUserId());
		for (Player player : game.getPlayers()) {
			boolean isCurrentUser = player.getUserId().equals(user.getId());
			players.add(toJSON(player, isCurrentUser));
		}
		result.put("dealer", toJSON(game.getDealer()));
		result.put("result", null);
		result.put("players", players);
		result.put("controls", null);
		{
			JSONObject controls = new JSONObject();
			{
				JSONObject balance = new JSONObject();
				balance.put("value", user.getAccountBalance());
				controls.put("balance", balance);
			}
			{
				JSONObject actions = new JSONObject();
				{
					JSONObject surrender = new JSONObject();
					surrender.put("isActive", isUserActive);
					actions.put("surrender", surrender);
				}
				{
					JSONObject split = new JSONObject();
					split.put("isActive", isUserActive);
					actions.put("split", split);
				}
				{
					JSONObject doubleAct = new JSONObject();
					doubleAct.put("isActive", isUserActive);
					actions.put("double", doubleAct);
				}
				{
					JSONObject hit = new JSONObject();
					hit.put("isActive", isUserActive);
					actions.put("hit", hit);
				}
				{
					JSONObject deal = new JSONObject();
					deal.put("isActive", isUserActive);
					actions.put("bid", deal);
				}
				{
					JSONObject stay = new JSONObject();
					stay.put("isActive", isUserActive);
					actions.put("stay", stay);
				}
				{
					JSONObject insurance = new JSONObject();
					insurance.put("isActive", isUserActive);
					actions.put("insurance", insurance);
				}
			}
			{
				JSONObject bid = new JSONObject();
				bid.put("isActive", isUserActive);
				controls.put("bid", bid);
			}
			result.put("controls", controls);
		}
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
		result.put("hand", dealer.getHand());
		return result;
	}

	public static JSONObject toJSON(Player player, boolean isCurrentUser) {
		JSONObject result = new JSONObject();
		UserPool userPool = UserPool.getInstance();
		PhotoManager photoManager = new PhotoManager();
		User user = userPool.get(player.getUserId());
		result.put("id", player.getUserId().getValue());
		if (user != null) {
			result.put("id", user.getDisplayName());
			result.put("img", photoManager.findPhotoRelativePath(player.getUserId()));
			result.put("hands", toJSON(player.getHands()));
			result.put("isActive", player.isActive());
		} else {
			result.put("id", "");
			result.put("img", "");
			result.put("hands", "");
			result.put("isActive", false);
		}
		return result;
	}

	private static JSONArray toJSON(List<Hand> hands) {
		JSONArray result = new JSONArray();
		for (Hand hand : hands) {
			result.add(toJSON(hand, true));
		}
		return result;
	}

	private static JSONObject toJSON(Hand hand, boolean isPlayer) {
		JSONObject result = new JSONObject();
		result.put("score", toJSON(hand.getScore(), true));
		result.put("cards", toJSON(hand.getCards(), isPlayer));
		if (isPlayer) {
			result.put("bet", hand.getBid());// ?toString
			result.put("isActive", hand.isActive());
		}
		return result;
	}

	private static String toJSON(Score score, boolean showScore) {
		String result = "";
		if (showScore) {
			if (score.isBlackJack()) {
				result = "blackjack";
			} else {
				result = Integer.toString(score.getValue());
			}
		}

		return result;
	}

	private static JSONArray toJSON(List<Card> cards, boolean showAll) {
		JSONArray result = new JSONArray();
		if (showAll) {
			for (Card card : cards) {
				result.add(card.getImgPath());
			}
		} else {
			if (!cards.isEmpty()) {
				result.add(cards.get(0));
				for (int i = 1; i < cards.size(); i++) {
					result.add(Card.CARD_BACK);
				}
			}
		}
		return result;
	}
}
