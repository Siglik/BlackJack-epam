/**
 * 
 */
package org.qqq175.blackjack.logic.blackjack;

import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qqq175.blackjack.logic.blackjack.entity.Card;
import org.qqq175.blackjack.logic.blackjack.entity.Card.Rank;
import org.qqq175.blackjack.logic.blackjack.entity.Card.Suit;
import org.qqq175.blackjack.logic.blackjack.entity.Deck;
import org.qqq175.blackjack.logic.blackjack.entity.Hand;
import org.qqq175.blackjack.logic.blackjack.entity.Player;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.id.UserId;

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
		Settings.getInstance().setContextPath("context-path");
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
		Hand hand = new Hand();
		hand.setStage(GameStage.DEAL);
		hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		hand.addCard(new Card(Suit.CLUBS, Rank.SIX));
		Assert.assertFalse("PLAY Score:" + hand.getScore().getValue(), GameLogic.canDouble(hand));

		hand = new Hand();
		hand.setStage(GameStage.PLAY);
		hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		Assert.assertFalse("PLAY Score:" + hand.getScore().getValue(), GameLogic.canDouble(hand));
	}

	@Test
	public void calcHandResultTestBlackJack() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.DIAMONDS, Rank.KING));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.BLACKJACK, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestBlackJackIsurance() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.KING));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.BLACKJACK_INSURANCE, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestWin() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.FIVE));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.DIAMONDS, Rank.KING));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.WIN, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestWiBigScore() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.TWO));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.SEVEN));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.SEVEN));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.WIN, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestLossWinInsurance() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.KING));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.LOSS_WIN_INSURANCE, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestTie() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.TIE, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestTieLossInsurance() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.TIE_LOSS_INSURANCE, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestLossBlackJack() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.ACE));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.KING));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.LOSS, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestLossBigScore() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.LOSS, playerHand.getResult());
	}

	@Test
	public void calcHandResultTestLossNormal() {
		Player player = new Player(new UserId(0l), true);
		Hand playerHand = new Hand();
		playerHand.setStage(GameStage.PLAY);
		playerHand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
		playerHand.addCard(new Card(Suit.CLUBS, Rank.JACK));
		playerHand.setInsurance(BigDecimal.valueOf(10));

		Hand dealerHand = new Hand();
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));
		dealerHand.addCard(new Card(Suit.CLUBS, Rank.FOUR));

		GameLogic.calcHandResult(playerHand, dealerHand);
		Assert.assertSame(GameResult.LOSS, playerHand.getResult());
	}
}
