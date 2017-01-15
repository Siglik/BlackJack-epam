package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.main.IndexAction;
import org.qqq175.blackjack.action.implemented.player.BalanceAction;
import org.qqq175.blackjack.action.implemented.player.ChangePasswordAction;
import org.qqq175.blackjack.action.implemented.player.ChangePersonalAction;
import org.qqq175.blackjack.action.implemented.player.PaymentAction;
import org.qqq175.blackjack.action.implemented.player.SettingsAction;
import org.qqq175.blackjack.action.implemented.player.ShowStatsAction;

public enum PlayerActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	BALANCE {
		{
			this.action = new BalanceAction();
		}
	},
	CHANGEPASSWORD {
		{
			this.action = new ChangePasswordAction();
		}
	},
	CHANGEPERSONAL {
		{
			this.action = new ChangePersonalAction();
		}
	},
	SETTINGS {
		{
			this.action = new SettingsAction();
		}
	},
	SHOWSTATS {
		{
			this.action = new ShowStatsAction();
		}
	},
	PAYMENT {
		{
			this.action = new PaymentAction();
		}
	};

	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
