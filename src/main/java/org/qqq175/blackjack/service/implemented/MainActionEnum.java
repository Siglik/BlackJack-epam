/**
 * 
 */
package org.qqq175.blackjack.service.implemented;

import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionEnum;
import org.qqq175.blackjack.service.implemented.main.LogVisitorInfoAction;
import org.qqq175.blackjack.service.implemented.main.LoginAction;
import org.qqq175.blackjack.service.implemented.main.LogoutAction;
import org.qqq175.blackjack.service.implemented.main.RegisterAction;
import org.qqq175.blackjack.service.implemented.main.SetLocaleAction;

/**
 * contains MAIN scope actions (avaliable for guests)
 * 
 * @author qqq175
 *
 */
public enum MainActionEnum implements ActionEnum {
	NONE {
		{
			this.action = ActionConstants.INDEX_ACTION;
		}
	},
	UNKNOWN {
		{
			this.action = ActionConstants.INDEX_ACTION;
		}
	},
	INDEX {
		{
			this.action = ActionConstants.INDEX_ACTION;
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
	SETLOCALE {
		{
			this.action = new SetLocaleAction();
		}
	},
	SAVEVISITORINFO {
		{
			this.action = new LogVisitorInfoAction();
		}
	};

	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
