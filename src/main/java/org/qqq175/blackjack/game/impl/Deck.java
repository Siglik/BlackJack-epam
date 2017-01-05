package org.qqq175.blackjack.game.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class Deck {
	// deck is 52 cards, most popular is six-deck game (312 card)
	private Deque<Card> cards;
	private int redeckIndex;

	/**
	 * creates standard six decks game
	 */
	public Deck() {
		this(6);
	}

	public Deck(int decksCount) {
		int cardCount = decksCount * 52;
		List<Card> cards = new ArrayList<>(cardCount);
		for (Card.Suit s : Card.Suit.values()) {
			for (Card.Rank r : Card.Rank.values()) {
				for (int i = 0; i < decksCount; i++) {
					cards.add(new Card(s, r));
				}
			}
		}
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		Collections.shuffle(cards, rand);
		this.cards = new ArrayDeque<>(cards);
		// update this index according to bl rules (min - max values)
		this.redeckIndex = rand.nextInt(cardCount);
	}

	public Card pullCard() {
		return cards.getFirst();
	}
}
