/**
 * 
 */
package org.qqq175.blackjack.service.action.implemented;

import org.qqq175.blackjack.service.action.Action;
import org.qqq175.blackjack.service.action.ActionEnum;
import org.qqq175.blackjack.service.action.implemented.main.IndexAction;
import org.qqq175.blackjack.service.action.implemented.main.LoginAction;
import org.qqq175.blackjack.service.action.implemented.main.LogoutAction;

/**
 * @author qqq175
 *
 */
public enum MainActionEnum implements ActionEnum {
	NONE {
		{
			this.action = new IndexAction();
		}
	},
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	LOGIN {
		{
			this.action = new LoginAction();
		}
	},
	LOGOUT {
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
