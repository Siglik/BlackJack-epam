/**
 * 
 */
package org.qqq175.blackjack.action.implemented;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.ActionFactory;

/**
 * ActionFactory of command pattern.
 * 
 * @author qqq175
 */
public class ActionFactoryImpl implements ActionFactory {
	private static Logger log = LogManager.getLogger(ActionFactoryImpl.class);

	@Override
	public Action defineAction(String scope, String action) {
		Class<? extends Enum> scopeEnum = null;
		Action concreteAction = null;
		/* define scope */
		if ((scope != null && !scope.isEmpty()) && (action != null && !action.isEmpty())) {
			try {
				scopeEnum = ScopeEnum.valueOf(scope.toUpperCase()).getScope();
			} catch (IllegalArgumentException e) {
				concreteAction = MainActionEnum.UNKNOWN.getAction();
			}
			/* if scope is found - define action */
			if (scopeEnum != null) {
				try {
					Enum concreteActionItem = Enum.valueOf(scopeEnum, action.toUpperCase());
					if (concreteActionItem instanceof ActionEnum) {
						concreteAction = ((ActionEnum) concreteActionItem).getAction();
					} else {
						throw new RuntimeException();
					}
				} catch (IllegalArgumentException e) {
					log.warn("Unable to found action " + action.toUpperCase() + " in scope " + scope.toUpperCase(), e);
					concreteAction = MainActionEnum.UNKNOWN.getAction();
				}
			} else {
				log.warn("Unable to found scope " + scope.toUpperCase());
				concreteAction = MainActionEnum.NONE.getAction();
			}
		}
		return concreteAction;
	}
}
