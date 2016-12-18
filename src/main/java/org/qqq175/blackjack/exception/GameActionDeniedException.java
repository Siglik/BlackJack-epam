package org.qqq175.blackjack.exception;

public class GameActionDeniedException extends Exception {

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
