package text.backend;

import java.util.ArrayList;

import text.backend.Card.Suit;

public class Hand extends ArrayList<Card> {

	/**
	 * creates empty hand
	 */
	public Hand() {
	}

	/**
	 * fills the hand with some cards in the deck across all the hands
	 *
	 * @param num         amount of cards to put in the deck
	 * @param deck        deck to extract cards from
	 * @param playersLeft the amount of players splitting the deck (simulates
	 *                    dealing)
	 */
	public Hand(int num, Deck deck, int playersLeft) {
		playersLeft--;
		if (playersLeft <= 0) {
			addAll(deck);
			deck.clear();
		} else {
			for (int i = 0; i < num * playersLeft; i += playersLeft) {
				add(deck.draw(i));
			}
		}
	}

	/**
	 * changes this to hold another hand's cards
	 *
	 * @param hand list of cards to set this to
	 * @throws IndexOutOfBoundsException parameter and object must have the same
	 *                                   length
	 */
	private void changeTo(Hand hand) {
		if (this.size() != hand.size()) {
			throw new IndexOutOfBoundsException("Parameter Hand must have the same length as this Hand.");
		}
		for (int i = 0; i < hand.size(); i++) {
			this.set(i, hand.get(i));
		}
	}

	/**
	 * printing the cards in this list
	 */
	public void printHand() { // for BJ1, BJ2, Gin, and Crazy 8s
		organizeCards();
		int width = this.size() > 9 ? 2 : 1;
		for (int i = 0; i < this.size(); i++) {
			System.out.printf("%" + width + "d: " + this.get(i) + "\n", i + 1);
		}
	}

	/**
	 * alternate printing the cards in this list
	 * 
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	public void printHand2() throws InterruptedException { // for 7-8 and GOPS
		organizeCards();
		int width = this.size() > 9 ? 2 : 1;
		for (Suit suit : Game.suits) {
			for (int i = 0; i < this.size(); i++) {
				if (this.get(i).getSuit() == suit) {
					this.get(i).setCounted(false);
					System.out.printf("%" + width + "d: " + this.get(i) + "\n", i + 1);
					Game.sleep(75);
				}
			}
		}
	}

	public void printHand3() { // for Kings
		if (this.size() == 0) {
			System.out.println("Empty.");
		}
		for (int i = 0; i < this.size(); i++) {
			System.out.print(this.get(i));
			if (i != this.size() - 1) {
				System.out.print(", ");
			} else {
				System.out.println(".");
			}
		}
	}

	/**
	 * ordering this list by rank
	 */
	private void organizeByRank() {
		Hand newHand = new Hand();
		int totalCardsAdded = 0;
		int newIndex = 0;
		for (Suit suit : Game.suits) {
			for (Card card : this) {
				if (card.getSuit() == suit) {
					try {
						for (Card otherCard : newHand) {
							if (card.getRankValue() < otherCard.getRankValue()) {
								newIndex++;
							}
						}
						newHand.add(newIndex, card);
						totalCardsAdded++;
						if (totalCardsAdded == this.size()) {
							break;
						}
					} catch (IndexOutOfBoundsException e) {
						newHand.add(card);
					}
				}
				newIndex = 0;
			}
		}
		this.changeTo(newHand);
	}

	/**
	 * ordering this list by suit (uses 'suits' field for priority)
	 */
	private void organizeBySuit() {
		Hand newHand = new Hand();
		for (Suit suit : Game.suits) {
			for (Card card : this) {
				if (card.getSuit() == suit) {
					newHand.add(card);
				}
			}
		}
		this.changeTo(newHand);
	}

	/**
	 * organizes this list by rank, then suit
	 */
	public void organizeCards() {
		organizeByRank();
		organizeBySuit();
	}

	/**
	 * organizes this list by suit, then rank
	 */
	public void organizeCardsInverse() {
		organizeBySuit();
		organizeByRank();
	}

	/**
	 * draws the top card in the deck and removes it
	 *
	 * @return the top card
	 */
	public Card draw() {
		return remove(0);
	}

	public void add(Hand otherHand) {
		for (Card card : otherHand) {
			this.add(card);
		}
	}
}
