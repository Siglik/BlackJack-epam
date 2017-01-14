package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.main.IndexAction;
import org.qqq175.blackjack.action.implemented.player.BalanceAction;
import org.qqq175.blackjack.action.implemented.player.ChangePasswordAction;
import org.qqq175.blackjack.action.implemented.player.ChangePersonalAction;
import org.qqq175.blackjack.action.implemented.player.PayAction;
import org.qqq175.blackjack.action.implemented.player.SettingsAction;
import org.qqq175.blackjack.action.implemented.player.StatsAction;
import org.qqq175.blackjack.action.implemented.player.WithdrawalAction;

public enum PlayerActionEnum implements ActionEnum{
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	BALANCE{
		{
			this.action = new BalanceAction();
		}
	},
	CHANGEPASSWORD{
		{
			this.action = new ChangePasswordAction();
		}
	},
	CHANGEPERSONAL{
		{
			this.action = new ChangePersonalAction();
		}
	},
	PAY{
		{
			this.action = new PayAction();
		}
	},
	SETTINGS{
		{
			this.action = new SettingsAction();
		}
	},
	STATS{
		{
			this.action = new StatsAction();
		}
	},
	WITHDRAWAL{
		{
			this.action = new WithdrawalAction();
		}
	};

	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
