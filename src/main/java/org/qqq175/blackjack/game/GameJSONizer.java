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
public class GameJSONizer {

	public static JSONObject toJSON(BlackJackGame game, User user) {
		JSONObject result = new JSONObject();
		JSONArray players = new JSONArray();
		Player activePlayer = game.getActivePlayer();
		boolean isUserActive = false;
		if (activePlayer != null) {
			isUserActive = user.getId().equals(activePlayer.getUserId());
		}
		for (Player player : game.getPlayersList()) {
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
					boolean isActive = isUserActive && game.getGameStage().compareTo(GameStage.PLAY) <= 0 && GameLogic.canSurrender(activePlayer);
					surrender.put("isActive", isActive);
					actions.put("surrender", surrender);
				}
				{
					JSONObject split = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage() == GameStage.PLAY && GameLogic.canSplit(activePlayer);
					split.put("isActive", isActive);
					actions.put("split", split);
				}
				{
					JSONObject doubleBet = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage() == GameStage.PLAY && GameLogic.canDouble(activePlayer);
					doubleBet.put("isActive", isActive);
					actions.put("double", doubleBet);
				}
				{
					JSONObject hit = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage() == GameStage.PLAY && GameLogic.canHit(activePlayer);
					hit.put("isActive", isActive);
					actions.put("hit", hit);
				}
				{
					JSONObject deal = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage() == GameStage.DEAL && GameLogic.canDeal(activePlayer);
					deal.put("isActive", isActive);
					actions.put("deal", deal);
				}
				{
					JSONObject stay = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage().compareTo(GameStage.PLAY) <= 0 && GameLogic.canStay(activePlayer);
					stay.put("isActive", isActive);
					actions.put("stay", stay);
				}
				{
					JSONObject insurance = new JSONObject();
					boolean isActive = isUserActive && game.getGameStage() == GameStage.PLAY
							&& GameLogic.canInsurance(game.getDealer(), activePlayer);
					insurance.put("isActive", isActive);
					actions.put("insurance", insurance);
				}
				controls.put("actions", actions);
			}
			{
				JSONObject bid = new JSONObject();
				boolean isActive = isUserActive && game.getGameStage() == GameStage.DEAL && GameLogic.canDeal(activePlayer);
				bid.put("isActive", isActive);
				controls.put("bid", bid);
			}
			result.put("controls", controls);
		}

		return result;
	}

	private static JSONObject toJSON(Dealer dealer) {
		JSONObject result = new JSONObject();
		result.put("hand", toJSON(dealer.getHand(), dealer.isShowAllCards()));
		return result;
	}

	private static JSONObject toJSON(Player player, boolean isCurrentUser) {
		JSONObject result = new JSONObject();
		UserPool userPool = UserPool.getInstance();
		PhotoManager photoManager = new PhotoManager();
		User user = userPool.get(player.getUserId());
		result.put("id", player.getUserId().getValue());
		if (user != null) {
			result.put("id", user.getDisplayName());
			result.put("img", photoManager.findPhotoRelativePath(player.getUserId()));
			result.put("hands", toJSON(player.getHandsListCopy()));
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

	private static JSONObject toJSON(Hand hand, boolean showAll) {
		JSONObject result = new JSONObject();
		result.put("score", toJSON(hand.getScore(), true));
		result.put("cards", toJSON(hand.getCardsListCopy(), showAll));
		if (showAll) {
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
				result.add(cards.get(0).getImgPath());
				for (int i = 1; i < cards.size(); i++) {
					result.add(Card.CARD_BACK);
				}
			}
		}
		return result;
	}

}
