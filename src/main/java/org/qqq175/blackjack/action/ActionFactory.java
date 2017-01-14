package org.qqq175.blackjack.action;

public interface ActionFactory {
	Action defineAction(String scope, String action);
}
