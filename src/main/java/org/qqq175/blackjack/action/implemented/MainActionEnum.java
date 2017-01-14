/**
 * 
 */
package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.main.IndexAction;
import org.qqq175.blackjack.action.implemented.main.LoginAction;
import org.qqq175.blackjack.action.implemented.main.LogoutAction;
import org.qqq175.blackjack.action.implemented.main.RegisterAction;
import org.qqq175.blackjack.action.implemented.main.SetLocaleAction;

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
	},
	REGISTER {
		{
			this.action = new RegisterAction();
		}
	},
	SET_LOCALE {
		{
			this.action = new SetLocaleAction();
		}
	};

	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
