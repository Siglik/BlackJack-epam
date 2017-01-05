package org.qqq175.blackjack.game.impl;

import java.util.List;
import java.util.Optional;

public class GameBox {
	private Optional<AbstractPlayer> player;
	private List<Hand> hand;

	/**
	 * @return
	 * @see java.util.Optional#isPresent()
	 */
	public boolean isPresent() {
		return player.isPresent();
	}

	/**
	 * @return the player
	 */
	public Optional<AbstractPlayer> getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Optional<AbstractPlayer> player) {
		this.player = player;
	}
}
