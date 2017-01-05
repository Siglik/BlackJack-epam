/**
 * 
 */
package org.qqq175.blackjack.exception;

/**
 * @author qqq175
 *
 */
public class EntityNotFoundException extends Exception {

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
