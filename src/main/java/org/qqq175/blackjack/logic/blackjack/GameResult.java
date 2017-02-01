package org.qqq175.blackjack.logic.blackjack;

/**available game results*/
public enum GameResult {
	/**
	 * casino pays 3 to 2, bid is back, insurance lost(BLACKJACK)
	 */
	BLACKJACK,
	/**
	 * 2 to 1 insurance, bid(TIE) and insurance is back(WIN)
	 */
	BLACKJACK_INSURANCE, 
	/**
	 * casino pays 1 to 1 bid, bid is back. Loss insurance if it made.(WIN)
	 */
	WIN, 
	/**
	 * Loss main bid, casino pays 2 to 1 insurance, insurance is back(WIN)
	 */
	LOSS_WIN_INSURANCE, 
	/**
	 * loss bid, no insurance made(TIE)
	 */
	TIE, 
	/**
	 * loss insurance and bid(LOSS)
	 */
	LOSS,
	/**
	 * got 50% of deal back(LOSS)
	 */
	SURRENDER,
	/**
	 * get bid back, loss insurance(LOSS)
	 */ 
	TIE_LOSS_INSURANCE,
	/**
	 * not played(LOSS)
	 */
	NONE;
}
