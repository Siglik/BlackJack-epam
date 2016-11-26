package org.qqq175.blackjack.game;

public class Card {
	Suit suit;

	/**
	 * @param suit
	 * @param rank
	 */
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	Rank rank;

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES;
	}

	public enum Rank {
		ACE(-1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(-1), QUEEN(-1), KING(-1);

		int value;

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

}
