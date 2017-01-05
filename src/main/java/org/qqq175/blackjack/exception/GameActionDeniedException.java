package org.qqq175.blackjack.exception;

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
