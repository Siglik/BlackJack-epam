/**
 * 
 */
package org.qqq175.blackjack.service.implemented;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.controller.CommandParser.CommandContext;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionEnum;
import org.qqq175.blackjack.service.ActionFactory;

/**
 * ActionFactory of command pattern.
 * 
 * @author qqq175
 */
public class ActionFactoryImpl implements ActionFactory {
	private static final String EMPTY = "";
	private static Logger log = LogManager.getLogger(ActionFactoryImpl.class);

	@Override
	public Action defineAction(CommandContext context) {
		String action, scope;
		if (context != null && !context.isEmpty()) {
			scope = context.getScope() != null ? context.getScope() : EMPTY;
			action = context.getAction() != null ? context.getAction() : EMPTY;
		} else {
			action = EMPTY;
			scope = EMPTY;
		}

		Class<? extends Enum> scopeEnum = null;
		Action concreteAction = null;
		/* define scope */
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
		return concreteAction;
	}
}
