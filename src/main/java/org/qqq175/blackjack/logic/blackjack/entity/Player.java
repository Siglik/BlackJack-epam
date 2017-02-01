package org.qqq175.blackjack.logic.blackjack.entity;

import java.util.ArrayList;
import java.util.List;

import org.qqq175.blackjack.logic.blackjack.GameStage;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * Player data class
 * 
 * @author qqq175
 */
public class Player {
	private UserId userId;
	private GameStage stage;
	private List<Hand> hands;
	private boolean isActive;
	private Hand activeHand = null;

	/**
	 * construct new player
	 * 
	 * @param userId
	 *            - user's id
	 * @param isActive
	 *            - is player active (if it his turn to play)
	 */
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

	/**
	 * Iterate user hands of given Stage. Found hand will be saved to
	 * activeHand. If nothing found - next hand will be null
	 * 
	 * @param stage
	 * @return true if next Hand found, else false
	 */
	public boolean nextHand(GameStage stage) {
		int nextHandId = activeHand != null ? hands.indexOf(activeHand) + 1 : 0;
		boolean foundNext = false;

		while (!foundNext && nextHandId < hands.size()) {
			Hand hand = hands.get(nextHandId);
			if (hand != null && hand.getStage() == stage) {
				hand.setActive(true);
				if (activeHand != null) {
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

	/**
	 * add additional hand to player
	 * 
	 * @param hand
	 * @return
	 */
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

	/**
	 * set new game stage, if new round(game stage is DEAL) - renicialize
	 * player.
	 * 
	 * @param stage
	 */
	public void setStage(GameStage stage) {
		if (stage == GameStage.DEAL) {
			hands.clear();
			Hand hand = new Hand();
			this.addHand(hand);
			this.activeHand = null;
		}
		for (Hand hand : hands) {
			GameStage handStage = hand.getStage();
			if (handStage != GameStage.UNACTIVE && handStage.compareTo(stage) < 0) {
				hand.setStage(stage);
			}
		}
		this.stage = stage;
	}

	/** reset active hand to null */
	public void resetActiveHand() {
		if (activeHand != null) {
			activeHand.setActive(false);
			activeHand = null;
		}

	}
}
