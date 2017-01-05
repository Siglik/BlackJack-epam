package org.qqq175.blackjack.service.action;

import org.qqq175.blackjack.service.SessionRequestContent;

public interface IActionFactory {
	Action defineAction(SessionRequestContent requestContext);
}
