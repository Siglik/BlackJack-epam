package org.qqq175.blackjack.service.implemented;

import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionEnum;
import org.qqq175.blackjack.service.implemented.main.IndexAction;
import org.qqq175.blackjack.service.implemented.player.BalanceAction;
import org.qqq175.blackjack.service.implemented.player.ChangeAvatarAction;
import org.qqq175.blackjack.service.implemented.player.ChangePasswordAction;
import org.qqq175.blackjack.service.implemented.player.ChangePersonalAction;
import org.qqq175.blackjack.service.implemented.player.PaymentAction;
import org.qqq175.blackjack.service.implemented.player.SettingsAction;
import org.qqq175.blackjack.service.implemented.player.ShowStatsAction;

/**
 * contains PLAYER scope actions
 * @author qqq175
 *
 */
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
	CHANGEAVATAR {
		{
			this.action = new ChangeAvatarAction();
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
