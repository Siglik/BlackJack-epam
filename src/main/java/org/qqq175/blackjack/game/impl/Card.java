package org.qqq175.blackjack.game.impl;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.qqq175.blackjack.persistence.dao.util.Settings;

public class Card {
	/**
	 * contain all card types image locations.
	 */
	private static Map<Suit, Map<Rank, String>> imgPaths;
	public final static String CARD_BACK;
	/* init card image paths */
	static {
		String imgLocation = Settings.getInstance().getCardFolder();
		CARD_BACK = Settings.getInstance().getContextPath() + File.separator + imgLocation + File.separator + "back.gif";
		imgPaths = new ConcurrentHashMap<>();
		for (Suit s : Suit.values()) {
			Map<Rank, String> innerMap = new ConcurrentHashMap<>();
			imgPaths.put(s, innerMap);
			for (Rank r : Rank.values()) {
				String imgPath = Settings.getInstance().getContextPath() + File.separator + imgLocation + File.separator + r.name().toLowerCase()
						+ "_of_" + s.name().toLowerCase() + ".svg";
				innerMap.put(r, imgPath);
			}
		}
	}

	private Suit suit;
	private Rank rank;

	/**
	 * @param suit
	 * @param rank
	 */
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES;
	}

	public enum Rank {
		ACE(11), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);

		private int value;

		private Rank(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public int getValue() {
		return rank.getValue();
	}

	/**
	 * @return the suit
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}

	public String getImgPath() {
		return imgPaths.get(suit).get(rank);
	}
}
