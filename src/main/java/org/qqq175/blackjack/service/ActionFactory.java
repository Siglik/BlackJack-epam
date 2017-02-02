package org.qqq175.blackjack.service;

import org.qqq175.blackjack.controller.CommandParser.CommandContext;

/**
 * Action factory interface
 * 
 * @author qqq175
 *
 */
public interface ActionFactory {
	/**
	 * Define action by it's scope and name
	 * 
	 * @param context
	 * @return
	 */
	Action defineAction(CommandContext context);
}
