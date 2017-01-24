package org.qqq175.blackjack.game;

public enum GameState {
	UNACTIVE, DEAL, PLAY, DONE, RESULT;

	public GameState nextState() {
		GameState values[] = GameState.values();
		int nextIndex = this.ordinal() + 1;
		if (nextIndex < values.length) {
			return values[nextIndex];
		} else {
			return DEAL;
		}
	}
}
