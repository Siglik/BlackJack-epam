package org.qqq175.blackjack.game.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.game.GameState;
import org.qqq175.blackjack.game.GameLogic;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class Player {
	private UserId userId;
	private GameState state;
	private List<Hand> hands;
	private boolean isActive;
	private Hand activeHand = null;
	
	public Player(UserId userId, boolean isActive) {
		this.userId = userId;
		hands = new ArrayList();
		Hand hand = new Hand();
		this.addHand(hand);
		this.activeHand = hand;
		if (isActive) {
			this.state = GameState.DEAL;
		} else {
			this.state = GameState.DONE;
		}
	}

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

	private abstract class State {
		abstract void nextState();

		abstract GameState getState();

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
		void nextState() {
			state = new PlayState();
		}

		@Override
		GameState getState() {
			// TODO Auto-generated method stub
			return GameState.DEAL;
		}
	}

	private class PlayState extends State {

		public boolean tryDouble() {
			if (GameLogic.canDouble(activeHand)) {
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
		GameState getState() {
			return GameState.PLAY;
		}

	}

	private class DoneState extends State {

		@Override
		void nextState() {
			state = new DealState();
		}

		@Override
		GameState getState() {
			return GameState.DONE;
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

	public boolean addHand(Hand hand) {
		return hands.add(hand);
	}

	/**
	 * @return the copy of hands list
	 */
	public List<Hand> getHands() {
		List<Hand> copyOfHands = new ArrayList<>(hands);
		return copyOfHands;
	}
	
	/** 
	 * @return number of player's hands.
	 */
	public int handsCount(){
		return hands.size();
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

	public GameState getState() {
		return state;
	}

	public void nextState() {
		// TODO Auto-generated method stub
		
	}
}
