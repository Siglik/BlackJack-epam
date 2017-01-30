/**
 * 
 */
package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.ActionFactory;

/**
 * @author qqq175
 *
 */
public class ActionFactoryImpl implements ActionFactory {

	@Override
	public Action defineAction(String scope, String action) {
		Class<? extends Enum> scopeEnum = null;
		Action concreteAction = null;
		if ((scope != null && !scope.isEmpty()) && (action != null && !action.isEmpty())) {
			try {
				scopeEnum = ScopeEnum.valueOf(scope.toUpperCase()).getScope();
			} catch (IllegalArgumentException e) {
				concreteAction = MainActionEnum.UNKNOWN.getAction();
			}
			if (scopeEnum != null) {
				try {
					Enum concreteActionItem = Enum.valueOf(scopeEnum, action.toUpperCase());
					if (concreteActionItem instanceof ActionEnum) {
						concreteAction = ((ActionEnum) concreteActionItem).getAction();
					} else {
						throw new RuntimeException();
					}
				} catch (IllegalArgumentException e) {
					// TODO log warning
					concreteAction = MainActionEnum.UNKNOWN.getAction();
				}
			} else {
				concreteAction = MainActionEnum.NONE.getAction();
			}
		}
		return concreteAction;
	}
}
