package org.qqq175.blackjack.game;

import java.util.Set;

import org.qqq175.blackjack.util.Settings;

public class GamePool {
	private Set<Game> games;

	private static volatile GamePool instance;

	/**
	 * Double Checked Locking & volatile singleton get instance method
	 * 
	 * @return ConnectionPool instance
	 */
	public static GamePool getInstance() {
		GamePool localInstance = instance;
		if (localInstance == null) {
			synchronized (Settings.class) {
				localInstance = instance;
				if (instance == null) {
					instance = localInstance = new GamePool();
				}
			}
		}

		return localInstance;
	}
}
