package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.game.NewGameMultiAction;
import org.qqq175.blackjack.action.implemented.game.NewGameSoloAction;
import org.qqq175.blackjack.action.implemented.main.IndexAction;

public enum GameActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	NEWSOLO{
		{
			this.action = new NewGameSoloAction();
		}
	},
	NEWMULTI{
		{
			this.action = new NewGameMultiAction();
		}
	};
	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
