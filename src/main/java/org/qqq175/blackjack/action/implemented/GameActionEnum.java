package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.game.GameAction;
import org.qqq175.blackjack.action.implemented.game.NewGameAction;
import org.qqq175.blackjack.action.implemented.main.IndexAction;

public enum GameActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	GAME {
		{
			this.action = new GameAction();
		}
	},
	NEWSOLO {
		{
			this.action = new NewGameAction(NewGameAction.Mode.SINGLEPLAYER);
		}
	},
	NEWMULTI {
		{
			this.action = new NewGameAction(NewGameAction.Mode.MULTIPLAYER);
		}
	};
	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
