package org.qqq175.blackjack.game;

import java.math.BigDecimal;

public interface PlayerAction {
	boolean canHit();

	boolean canDouble();

	boolean canSplit();

	boolean canSurrender();

	boolean canDeal(BigDecimal betSize);

	boolean canInsurance();

	boolean canStand();
}
