package org.qqq175.blackjack.game.impl;

import java.util.List;

public class Dealer {
	private List<Hand> hands;

	public boolean isInsuranceable() {
		if (hands.size() > 0 && hands.get(0).getCards().size() > 0) {
			Card firstCard = hands.get(0).getCards().get(0);

			return firstCard.getRank() == Card.Rank.ACE;
		} else {
			return false;
		}
	}

}
