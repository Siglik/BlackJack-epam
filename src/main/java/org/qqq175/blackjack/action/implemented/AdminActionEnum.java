package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.admin.ChangePlayerTypeAction;
import org.qqq175.blackjack.action.implemented.admin.PlayerBanUnbanAction;
import org.qqq175.blackjack.action.implemented.admin.PlayerInfoAction;
import org.qqq175.blackjack.action.implemented.admin.PlayersListAction;
import org.qqq175.blackjack.action.implemented.main.IndexAction;

public enum AdminActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	CHANGEPLAYERTYPE {
		{
			this.action = new ChangePlayerTypeAction();
		}
	},
	PLAYERBAN {
		{
			this.action = new PlayerBanUnbanAction(PlayerBanUnbanAction.Mode.BAN);
		}
	},
	PLAYERINFO {
		{
			this.action = new PlayerInfoAction();
		}
	},
	PLAYERSLIST {
		{
			this.action = new PlayersListAction();
		}
	},
	PLAYERUNBAN {
		{
			this.action = new PlayerBanUnbanAction(PlayerBanUnbanAction.Mode.UNBAN);
		}
	};
	Action action;

	@Override
	public Action getAction() {
		return action;
	}

}
