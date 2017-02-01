package org.qqq175.blackjack.service.implemented;

import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionEnum;
import org.qqq175.blackjack.service.implemented.admin.ChangePlayerTypeAction;
import org.qqq175.blackjack.service.implemented.admin.PlayerBanUnbanAction;
import org.qqq175.blackjack.service.implemented.admin.PlayerInfoAction;
import org.qqq175.blackjack.service.implemented.admin.PlayersListAction;

/**
 * contains actions of ADMIN scope
 * 
 * @author qqq175
 */
public enum AdminActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = ActionConstants.INDEX_ACTION;
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
