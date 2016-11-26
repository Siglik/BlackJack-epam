/**
 * 
 */
package org.qqq175.blackjack.service.action.implemented;

import org.qqq175.blackjack.service.SessionRequestContent;
import org.qqq175.blackjack.service.action.Action;

/**
 * @author qqq175
 *
 */
public class ActionFactory {
	public Action defineAction(SessionRequestContent requestContext) {
		String actionString = requestContext.getParameter("action");
		System.out.println(actionString);
		Action action;
		if (actionString == null || actionString.isEmpty()) {
			// если команда не задана в текущем запросе
			action = ActionEnum.NONE.getAction();
		} else {
			try {
				ActionEnum actionEnum = ActionEnum.valueOf(actionString.toUpperCase());
				action = actionEnum.getAction();
			} catch (IllegalArgumentException e) {
				// Logger.getInstance().log(e);
				// if unknown action
				action = ActionEnum.UNKNOWN.getAction();
			}
		}
		return action;
	}
}
