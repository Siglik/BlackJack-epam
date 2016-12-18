package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.GameActionDeniedException;
import org.qqq175.blackjack.game.GameActions;
import org.qqq175.blackjack.persistence.dto.User;

public class Player implements GameActions {
	private User user;
	private State state;
	private List<Hand> hands;
	private Hand activeHand = null;

	private abstract class State implements GameActions {
		abstract void nextState();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#hit()
		 */
		@Override
		public void hit() throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#doubleBet()
		 */
		@Override
		public void doubleBet() throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#split()
		 */
		@Override
		public void split() throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#surrender()
		 */
		@Override
		public void surrender() throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#deal(java.math.BigDecimal)
		 */
		@Override
		public void deal(BigDecimal betSize) throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.qqq175.blackjack.game.GameActions#insurance()
		 */
		@Override
		public void insurance() throws GameActionDeniedException {
			throw new GameActionDeniedException("Unable to perform action at current player state");
		}
	}

	private class DealState extends State {

		@Override
		public void surrender() throws GameActionDeniedException {
			state = new DoneState();
		}

		@Override
		public void deal(BigDecimal betSize) throws GameActionDeniedException {
			if (user.getBalance().compareTo(betSize) >= 0) {
				Hand hand = new Hand();
				hand.setBid(betSize);
				addHand(hand);
				nextState();
			} else {
				throw new GameActionDeniedException("Bet greater than user balance");
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
	public void hit() throws GameActionDeniedException {
		state.hit();
	}

	@Override
	public void doubleBet() throws GameActionDeniedException {
		state.doubleBet();
	}

	@Override
	public void split() throws GameActionDeniedException {
		if (getHands().size() <= 4) {
			state.split();
		} else {
			// TODO exception
		}
	}

	@Override
	public void surrender() throws GameActionDeniedException {
		state.surrender();
	}

	@Override
	public void deal(BigDecimal betSize) throws GameActionDeniedException {
		state.deal(betSize);
	}

	@Override
	public void insurance() throws GameActionDeniedException {
		state.insurance();
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return !(state instanceof DoneState);
	}

	private void addHand(Hand hand) {
		hands.add(hand);
	}
}
