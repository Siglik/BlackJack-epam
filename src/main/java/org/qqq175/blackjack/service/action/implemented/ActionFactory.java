/**
 * 
 */
package org.qqq175.blackjack.service.action.implemented;

import org.qqq175.blackjack.service.action.Action;
import org.qqq175.blackjack.service.action.ActionEnum;

/**
 * @author qqq175
 *
 */
public class ActionFactory {
	public Action defineAction(String scope, String action) {
		Class<? extends Enum> scopeEnum = null;
		Action concreteAction;
		if ((scope != null && !scope.isEmpty()) && (action != null && !action.isEmpty())) {
			try {
				scopeEnum = ScopeEnum.valueOf(scope.toUpperCase()).getScope();
			} catch (IllegalArgumentException e) {
				// TODO catch
			}
			try {
				Enum concreteActionItem = Enum.valueOf(scopeEnum, action.toUpperCase());
				if (concreteActionItem instanceof ActionEnum) {
					concreteAction = ((ActionEnum) concreteActionItem).getAction();
				} else {
					throw new RuntimeException();
				}
			} catch (IllegalArgumentException e) {
				// Logger.getInstance().log(e);
				// if unknown action
				concreteAction = MainActionEnum.UNKNOWN.getAction();
			}
		} else {
			concreteAction = MainActionEnum.NONE.getAction();
		}

		return concreteAction;
	}
}
