/**
 * 
 */
package org.qqq175.blackjack.logic.blackjack;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qqq175.blackjack.logic.blackjack.entity.Card;
import org.qqq175.blackjack.logic.blackjack.entity.Card.Rank;
import org.qqq175.blackjack.logic.blackjack.entity.Card.Suit;
import org.qqq175.blackjack.logic.blackjack.entity.Deck;
import org.qqq175.blackjack.logic.blackjack.entity.Hand;

/**
 * @author qqq175
 *
 */
public class GameLogicTest {
	static Deck deck;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		deck = new Deck();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deck = null;
	}

	@Test
	public void canDoubleHandTrue() {
		Hand hand = new Hand();
		hand.setStage(GameStage.PLAY);
		hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		hand.addCard(new Card(Suit.CLUBS, Rank.SIX));
		Assert.assertTrue("PLAY Score:" + hand.getScore().getValue(), GameLogic.canDouble(hand));

		hand = new Hand();
		hand.setStage(GameStage.PLAY);
		hand.addCard(new Card(Suit.DIAMONDS, Rank.FOUR));
		hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		Assert.assertTrue("PLAY Score:" + hand.getScore().getValue(), GameLogic.canDouble(hand));
	}

	@Test
	public void canDoubleHandFalse() {
		fail("Not yet implemented");
	}
}
