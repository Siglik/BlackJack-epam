package org.qqq175.blackjack.game.impl;

import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.game.GameStage;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class Player {
	private UserId userId;
	private GameStage stage;
	private List<Hand> hands;
	private boolean isActive;
	private Hand activeHand = null;

	public Player(UserId userId, boolean isActive) {
		this.userId = userId;
		hands = new ArrayList<>();
		Hand hand = new Hand();
		this.addHand(hand);
		this.activeHand = null;
		if (isActive) {
			this.stage = GameStage.DEAL;
		} else {
			this.stage = GameStage.UNACTIVE;
		}
	}

	public boolean nextHand(GameStage stage) {
		int nextHandId = activeHand != null ? hands.indexOf(activeHand) + 1 : 0;
		boolean foundNext = false;

		while (!foundNext && nextHandId < hands.size()) {
			Hand hand = hands.get(nextHandId);
			if (hand != null && hand.getStage() == stage) {
				hand.setActive(true);
				if (activeHand != null && activeHand.getStage() == stage) {
					activeHand.setActive(false);
				}
				activeHand = hand;
				foundNext = true;
			} else {
				nextHandId++;
			}
		}

		if (!foundNext) {
			this.resetActiveHand();
		}

		return foundNext;
	}

	/**
	 * @return the user
	 */
	public UserId getUserId() {
		return userId;
	}

	public boolean addHand(Hand hand) {
		return hands.add(hand);
	}

	/**
	 * @return the copy of hands list
	 */
	public List<Hand> getHandsListCopy() {
		List<Hand> copyOfHands = new ArrayList<>(hands);
		return copyOfHands;
	}

	/**
	 * @return number of player's hands.
	 */
	public int handsCount() {
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

	public GameStage getStage() {
		return stage;
	}

	public void setStage(GameStage stage) {
		for (Hand hand : hands) {
			GameStage handStage = hand.getStage();
			if (handStage != GameStage.UNACTIVE && handStage.compareTo(stage) < 0) {
				hand.setStage(stage);
			}
		}
		if (stage == GameStage.DEAL) {
			hands.clear();
			Hand hand = new Hand();
			this.addHand(hand);
			this.activeHand = null;
		}
		this.stage = stage;
	}

	public void resetActiveHand() {
		if (activeHand != null) {
			activeHand.setActive(false);
			activeHand = null;
		}

	}
}
