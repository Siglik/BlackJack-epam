package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.game.PlayerAction;
import org.qqq175.blackjack.persistence.entity.User;

public class Player implements PlayerAction {
	private User user;
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
		DEAL(new DealState()), PLAY(new PlayState()), DONE(new DoneState());

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
			if (user.getBalance().compareTo(betSize) >= 0) {
				Hand hand = new Hand();
				hand.setBid(betSize);
				addHand(hand);
				nextState();
			} else {
				return false;
			}
		}

		@Override
		void nextState() {
			state = new PlayState();
		}
	}

	private class PlayState extends State {

		@Override
		void nextState() {
		}

	}

	private class DoneState extends State {

		@Override
		void nextState() {
		}

	}

	public Player(User user, boolean isActive) {
		this.user = user;
		if (isActive) {
			this.state = new DealState();
		} else {
			this.state = new DoneState();
		}
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	@Override
	public boolean canHit() {
		state.canHit();
	}

	@Override
	public boolean canDouble() {
		state.canDouble();
	}

	@Override
	public boolean canSplit() {
		if (getHands().size() <= 4) {
			state.canSplit();
		} else {
			// TODO exception
		}
	}

	@Override
	public boolean canSurrender() {
		state.canSurrender();
	}

	@Override
	public boolean canDeal(BigDecimal betSize) {
		state.canDeal(betSize);
	}

	@Override
	public boolean canInsurance() {
		state.canInsurance();
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
}
