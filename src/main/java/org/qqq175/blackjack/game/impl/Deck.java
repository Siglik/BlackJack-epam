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

	/**
	 * creates standard six decks game
	 */
	public Deck() {
		this(6);
	}

	public Deck(int decksCount) {
		List<Card> cards = new ArrayList<>(decksCount*52+1);
		for (Card.Suit s : Card.Suit.values()) {
			for (Card.Rank r : Card.Rank.values()) {
				for(int i = 0; i < decksCount; i++){
					cards.add(new Card(s,r));
				}
			}
		}
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		Collections.shuffle(cards, rand);
		this.cards = new ArrayDeque<>(cards);
	}
	
	public Card pullCard(){
		return cards.getFirst();
	}
}
