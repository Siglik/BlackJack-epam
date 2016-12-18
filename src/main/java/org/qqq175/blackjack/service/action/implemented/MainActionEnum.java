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
public enum MainActionEnum implements ActionEnum {
	NONE {
		{
			this.action = new EmptyAction();
		}
	},
	UNKNOWN {
		{
			this.action = new EmptyAction();
		}
	},
	LOGIN {
		{
			this.action = new LoginAction();
		}
	},
	LOG_OUT {
		{
			this.action = new LogoutAction();
		}
	};

	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
