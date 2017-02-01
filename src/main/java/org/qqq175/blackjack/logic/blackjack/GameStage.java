package org.qqq175.blackjack.logic.blackjack;

/**
 * Enum of available game stages
 * 
 * @author qqq175
 *
 */
public enum GameStage {
	/**
	 * just entered or leaving for players, just created or closing for game
	 */
	UNACTIVE,
	/**
	 * making deals
	 */
	DEAL,
	/**
	 * deals made - play
	 */
	PLAY,
	/**
	 *  dealers plays, players await for game results, game calculate results for each hand 
	 */
	RESULT,
	/**
	 * players waiting for pay out and/or leaving, game pay out and start new round
	 */
	DONE;

	
	/**
	 * return next stage
	 * 
	 * @return
	 */
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
