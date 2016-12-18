package org.qqq175.blackjack.service.action.implemented;

import org.qqq175.blackjack.service.action.Action;
import org.qqq175.blackjack.service.action.ActionEnum;

public enum AdminActionEnum implements ActionEnum {
	NONE;
	Action action;

	@Override
	public Action getAction() {
		return action;
	}
}
