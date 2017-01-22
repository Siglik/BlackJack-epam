package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.PlayerAction;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;
import org.qqq175.blackjack.pool.UserPool;

public class Player implements PlayerAction {
	private UserId userId;
	private State state;
	private List<Hand> hands;
	private Hand activeHand = null;

	private void nextHand() {
		int handId = activeHand != null ? hands.indexOf(activeHand) : 0;
		boolean foundNext = false;

		while (!foundNext || handId < hands.size()) {
			Hand hand = hands.get(handId);
			if (hand != null && hand.isActive()) {
				activeHand = hand;
				foundNext = true;
			} else {
				handId++;
			}
		}

		if (!foundNext) {
			activeHand = null;
			state.nextState();
		}
	}

	public enum States {
		DEAL(/* new DealState() */null), PLAY(/* new PlayState() */null), DONE(
				/* new DoneState() */null);

		private States(State state) {
			this.state = state;
		}

		private final State state;

		/**
		 * @return the state
		 */
		public State getState() {
			return state;
		}
	}

	private abstract class State implements PlayerAction {
		abstract void nextState();

		@Override
		public boolean canHit() {
			return false;
		}

		@Override
		public boolean canDouble() {
			return false;
		}

		@Override
		public boolean canSplit() {
			return false;
		}

		@Override
		public boolean canSurrender() {
			return false;
		}

		@Override
		public boolean canDeal(BigDecimal betSize) {
			return false;
		}

		@Override
		public boolean canInsurance() {
			return false;
		}
	}

	private class DealState extends State {

		@Override
		public boolean canSurrender() {
			state = new DoneState();
			return true;
		}

		@Override
		public boolean canDeal(BigDecimal betSize) {
			UserPool uPool = UserPool.getInstance();
			User user = uPool.get(userId);
			if (user != null && user.getAccountBalance().compareTo(betSize) >= 0) {
				Hand hand = new Hand();
				hand.setBid(betSize);
				addHand(hand);
				nextState();
				// TODO i dont remember what it is
				return true;
			} else {
				return false;
			}
		}

		@Override
		void nextState() {
			state = new PlayState();
		}

		@Override
		public boolean canStand() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class PlayState extends State {

		@Override
		void nextState() {
		}

		@Override
		public boolean canStand() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	private class DoneState extends State {

		@Override
		void nextState() {
		}

		@Override
		public boolean canStand() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public Player(UserId userId, boolean isActive) {
		this.userId = userId;
		if (isActive) {
			this.state = new DealState();
		} else {
			this.state = new DoneState();
		}
	}

	/**
	 * @return the user
	 */
	public UserId getUserId() {
		return userId;
	}

	@Override
	public boolean canHit() {
		return state.canHit();
	}

	@Override
	public boolean canDouble() {
		return state.canDouble();
	}

	@Override
	public boolean canSplit() {
		if (getHands().size() <= 4) {
			return state.canSplit();
		} else {
			return false;
			// TODO exception
		}
	}

	@Override
	public boolean canSurrender() {
		return state.canSurrender();
	}

	@Override
	public boolean canDeal(BigDecimal betSize) {
		return state.canDeal(betSize);
	}

	@Override
	public boolean canInsurance() {
		return state.canInsurance();
	}

	/**
	 * @return the isActive
	 */
	public boolean isDone() {
		return !(state instanceof DoneState);
	}

	private void addHand(Hand hand) {
		hands.add(hand);
	}

	/**
	 * @return the hands
	 */
	public List<Hand> getHands() {
		return hands;
	}

	@Override
	public boolean canStand() {
		// TODO Auto-generated method stub
		return false;
	}
}
