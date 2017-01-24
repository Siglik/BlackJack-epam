package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.game.GameUtil;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class Player {
	private UserId userId;
	private State state;
	private List<Hand> hands;
	private boolean isActive;
	private Hand activeHand = null;

	private boolean nextHand() {
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
		}

		return foundNext;
	}

	public enum StateEnum {
		DEAL, PLAY, DONE;
	}

	private abstract class State {
		abstract void nextState();

		abstract StateEnum getState();

		boolean tryDeal(BigDecimal betSize) {
			return false;
		}

		boolean tryInsurance() {
			return false;
		}

		void setStateDone() {
			state = new DoneState();
		}
	}

	private class DealState extends State {

		@Override
		boolean tryDeal(BigDecimal betSize) {
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

		@Override
		StateEnum getState() {
			// TODO Auto-generated method stub
			return StateEnum.DEAL;
		}
	}

	private class PlayState extends State {

		public boolean tryDouble() {
			if (GameUtil.canDouble(activeHand)) {
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
		void nextState() {
		}

		@Override
		StateEnum getState() {
			return StateEnum.PLAY;
		}

	}

	private class DoneState extends State {

		@Override
		void nextState() {
			state = new DealState();
		}

		@Override
		StateEnum getState() {
			return StateEnum.DONE;
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

	public boolean tryDeal(BigDecimal betSize) {
		return state.tryDeal(betSize);
	}

	private boolean addHand(Hand hand) {
		return hands.add(hand);
	}

	/**
	 * @return the hands
	 */
	public List<Hand> getHands() {
		List<Hand> copyOfHands = new ArrayList<>(hands);
		return copyOfHands;
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
