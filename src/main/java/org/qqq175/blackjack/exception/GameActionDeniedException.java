package org.qqq175.blackjack.exception;

/**
 * BlackJack exception. CAn be thrown at calling game action in illegal state or by 
 * user that hasn't enough right to perform operation.
 * @author qqq175
 *
 */
public class GameActionDeniedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GameActionDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameActionDeniedException(String message) {
		super(message);
	}

	public GameActionDeniedException(Throwable cause) {
		super(cause);
	}

}
