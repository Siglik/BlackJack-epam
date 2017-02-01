package org.qqq175.blackjack.logic.blackjack;

public enum GameStage {
	UNACTIVE, DEAL, PLAY, RESULT, DONE;

	public GameStage nextState() {
		GameStage values[] = GameStage.values();
		int nextIndex = this.ordinal() + 1;
		if (nextIndex < values.length) {
			return values[nextIndex];
		} else {
			return DEAL;
		}
	}
}
