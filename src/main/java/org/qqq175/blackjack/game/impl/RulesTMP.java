package org.qqq175.blackjack.game.impl;

public class RulesTMP {

	/*
	 * Hit: Take another card from the dealer.
	 */
	/*
	 * Stand: Take no more cards, also known as "stand pat", "stick", or "stay".
	 */
	/*
	 * Double down: The player is allowed to increase the initial bet by up to
	 * 100% in exchange for committing to stand after receiving exactly one more
	 * card. The additional bet is placed in the betting box next to the
	 * original bet. Some games do not permit the player to increase the bet by
	 * amounts other than 100%. Non-controlling players may double their wager
	 * or decline to do so, but they are bound by the controlling player's
	 * decision to take only one card.
	 */
	/*
	 * Split: If the first two cards of a hand have the same value, the player
	 * can split them into two hands, by moving a second bet equal to the first
	 * into an area outside the betting box. The dealer separates the two cards
	 * and draws an additional card on each, placing one bet with each hand. The
	 * player then plays out the two separate hands in turn; except for a few
	 * restrictions, the hands are treated as independent new hands, with the
	 * player winning or losing their wager separately for each hand.
	 * Occasionally, in the case of ten-valued cards, some casinos allow
	 * splitting only when the cards have the identical ranks; for instance, a
	 * hand of 10-10 may be split, but not one of 10-king. However, usually all
	 * 10-value cards are treated the same. Doubling and further splitting of
	 * post-split hands may be restricted, and blackjacks after a split are
	 * counted as non-blackjack 21 when comparing against the dealer's hand.
	 * Hitting split aces is usually not allowed. Non-controlling players may
	 * follow the controlling player by putting down an additional bet or
	 * decline to do so, instead associating their existing wager with one of
	 * the two post-split hands. In that case they must choose which hand to
	 * play behind before the second cards are drawn. Some casinos do not give
	 * non-controlling players this option, and require that the wager of a
	 * player not electing to split remains with the first of the two post-split
	 * hands.
	 */
	/*
	 * Surrender (only available as first decision of a hand): Some games offer
	 * the option to "surrender", usually in hole-card games and directly after
	 * the dealer has checked for blackjack (but see below for variations). When
	 * the player surrenders, the house takes half the player's bet and returns
	 * the other half to the player; this terminates the player's interest in
	 * the hand.
	 */

	/*
	 * Insurance
	 * 
	 * If the dealer's upcard is an ace, the player is offered the option of
	 * taking "insurance" before the dealer checks the hole card.
	 * 
	 * Insurance is a side bet that the dealer has blackjack and is treated
	 * independently of the main wager. It pays 2:1 (meaning that the player
	 * receives two dollars for every dollar bet) and is available when the
	 * dealer's exposed card is an ace. The idea is that the dealer's second
	 * card has a fairly high probability (nearly one-third) to be ten-valued,
	 * giving the dealer blackjack and disappointment for the player. It is
	 * attractive (although not necessarily wise) for the player to insure
	 * against the possibility of a dealer blackjack by making a maximum
	 * "insurance" bet, in which case the "insurance proceeds" will make up for
	 * the concomitant loss on the original bet. The player may add up to half
	 * the value of their original bet to the insurance and these extra chips
	 * are placed on a portion of the table usually marked
	 * "Insurance pays 2 to 1".
	 * 
	 * Players with a blackjack may also take insurance, and in taking maximum
	 * insurance they commit themselves to winning an amount exactly equal to
	 * their main wager, regardless of the dealer's outcome. Fully insuring a
	 * blackjack against blackjack is thus referred to as "taking even money",
	 * and paid out immediately, before the dealer's hand is resolved; the
	 * players do not need to place more chips for the insurance wager.
	 * 
	 * Insurance bets are expected to lose money in the long run, because the
	 * dealer is likely to have blackjack less than one-third of the time.
	 * However the insurance outcome is strongly anti-correlated with that of
	 * the main wager, and if the player's priority is to reduce variation, they
	 * might choose to pay for this.
	 * 
	 * Furthermore, the insurance bet is susceptible to advantage play. It is
	 * advantageous to make an insurance bet whenever the hole card has more
	 * than a chance of one in three of being a ten. Advantage play techniques
	 * can sometimes identify such situations. In a multi-hand, face-up, single
	 * deck game, it is possible to establish whether insurance is a good bet
	 * simply by observing the other cards on the table after the deal; even if
	 * there are just 2 player hands exposed, and neither of their two initial
	 * cards is a ten, then 16 in 47 of the remaining cards are tens, which is
	 * larger than 1 in 3, so insurance is a good bet. This is an elementary
	 * example of the family of advantage play techniques known as card
	 * counting.
	 * 
	 * Bets to insure against blackjack are slightly less likely to be
	 * advantageous than insurance bets in general, since the ten in the
	 * player's blackjack makes it less likely that the dealer has blackjack too
	 */
}
