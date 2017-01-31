package org.qqq175.blackjack.action;

/**
 * Action factory interface
 * @author qqq175
 *
 */
public interface ActionFactory {
	/**
	 * Define action by it's scope and name
	 * @param scope
	 * @param action
	 * @return
	 */
	Action defineAction(String scope, String action);
}
