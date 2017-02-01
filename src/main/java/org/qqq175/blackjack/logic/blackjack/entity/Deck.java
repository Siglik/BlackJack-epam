package org.qqq175.blackjack.logic.blackjack.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Playcard deck data class
 * 
 * @author qqq175
 *
 */
public class Deck {
	// deck is 52 cards, most popular is six-deck game (312 card)
	private final static int ONE_DECK = 52;
	private Deque<Card> cards;
	private int redeckOnLeft;
	private int decksCount;
	private ReentrantLock lock;

	/**
	 * creates standard "six decks" Deck
	 */
	public Deck() {
		this(6);
	}

	/**
	 * creates Deck that contains 'deckCount' decks('deckCount'*52 cards)
	 * 
	 * @param decksCount
	 */
	public Deck(int decksCount) {
		this.lock = new ReentrantLock();
		this.decksCount = decksCount;

		this.cards = new ArrayDeque<>(Deck.initDeck(decksCount));

		Random rand = new Random();
		int cardCount = decksCount * ONE_DECK;
		rand.setSeed(System.currentTimeMillis());
		// redeck random on 30-50% cards left
		this.redeckOnLeft = (int) (cardCount * 0.5) - rand.nextInt((int) (cardCount * 0.2));
	}

	/**
	 * initialize and shuffle deck
	 * 
	 * @param decksCount
	 * @return
	 */
	private static List<Card> initDeck(int decksCount) {
		int cardCount = decksCount * ONE_DECK;
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		List<Card> cards = new ArrayList<>(cardCount);
		for (Card.Suit s : Card.Suit.values()) {
			for (Card.Rank r : Card.Rank.values()) {
				for (int i = 0; i < decksCount; i++) {
					cards.add(new Card(s, r));
				}
			}
		}
		Collections.shuffle(cards, rand);

		return cards;
	}

	public Card pullCard() {
		if (cards.size() < 1) {
			this.redeck();
		}
		lock.lock();
		Card card = cards.poll();
		lock.unlock();
		return card;
	}

	/**
	 * shuffle cards if 'redeck' value is reached, also shuffle if cards count
	 * less than one deck(52), so for one deck game deck will be shuffled
	 */
	public void newRound() {
		int cardsLeft = cards.size();
		if (cardsLeft < ONE_DECK || cardsLeft < this.redeckOnLeft) {
			this.redeck();
		}
	}

	/**
	 * reinit deck
	 */
	private void redeck() {
		lock.lock();
		this.cards = new ArrayDeque<>(Deck.initDeck(decksCount));
		lock.unlock();
	}
}
