package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class Player {
	private UserId userId;
	private State state;
	private List<Hand> hands;
	private boolean isActive;
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

	private abstract class State {
		abstract void nextState();

		public boolean canHit() {
			return false;
		}

		public boolean canDouble() {
			return false;
		}

		public boolean canSplit() {
			return false;
		}

		public boolean canSurrender() {
			return false;
		}

		public boolean tryDeal(BigDecimal betSize) {
			return false;
		}

		public boolean tryInsurance() {
			return false;
		}

		public boolean canStand() {
			return false;
		}
	}

	private class DealState extends State {

		@Override
		public boolean canSurrender() {
			return true;
		}

		@Override
		public boolean tryDeal(BigDecimal betSize) {
			UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
			try {
				boolean lockResult = userDAO.lockBalance(userId, betSize);
				if (lockResult) {
					Hand hand = new Hand();
					hand.setBid(betSize);
					addHand(hand);
					nextState();
					return true;
				} else {
					return false;
				}
			} catch (DAOException e) {
				// TODO log
				e.printStackTrace();
				return false;
			}
		}

		@Override
		void nextState() {
			state = new PlayState();
		}
	}

	private class PlayState extends State {

		public boolean tryDouble() {
			if (activeHand.canDouble()) {
				///
				///
				///
				///

				return true;
			} else {
				return false;
			}
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
			state = new DealState();
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

	public boolean canHit() {
		// Hit: Take another card from the dealer.
		return state.canHit();
	}

	public boolean canDouble() {
		return state.canDouble();
	}

	public boolean canSplit() {
		if (getHands().size() <= 4) {
			return state.canSplit();
		} else {
			return false;
		}
	}

	public boolean canSurrender() {
		return state.canSurrender();
	}

	public boolean tryDeal(BigDecimal betSize) {
		return state.tryDeal(betSize);
	}

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

	public boolean canStand() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the activeHand
	 */
	public Hand getActiveHand() {
		return activeHand;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
