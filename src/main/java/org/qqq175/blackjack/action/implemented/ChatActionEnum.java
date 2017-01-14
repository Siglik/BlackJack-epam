package org.qqq175.blackjack.action.implemented;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionEnum;
import org.qqq175.blackjack.action.implemented.chat.GetChatAction;
import org.qqq175.blackjack.action.implemented.chat.PostAction;
import org.qqq175.blackjack.action.implemented.main.IndexAction;

public enum ChatActionEnum implements ActionEnum {
	UNKNOWN {
		{
			this.action = new IndexAction();
		}
	},
	POST {
		{
			this.action = new PostAction();
		}
	},
	GETCHAT{
		{
			this.action = new GetChatAction();
		}
	};
	Action action;

	@Override
	public Action getAction() {
		return action;
	}

}
