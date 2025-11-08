import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Hand.
 * Tests hand initialization, adding and removing cards,
 * and dealing initial cards from the deck.
 *
 * @author G27
 * @version 1.0
 */
public class HandTest {

    /**
     * Tests hand initialization.
     * Verifies that a new hand starts with zero cards.
     */
    @Test
    public void testHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.getSize());
    }

    /**
     * Tests adding a card to the hand.
     * Verifies that the hand size increases correctly.
     */
    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        assertEquals(1, hand.getSize());
    }

    /**
     * Tests that added cards can be retrieved.
     * Verifies that the correct card is returned at the given index.
     */
    @Test
    public void testAddCards() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        assertEquals(card, hand.getCard(0));
    }

    /**
     * Tests removing a card from the hand.
     * Verifies that the hand size decreases correctly.
     */
    @Test
    public void testRemoveCard() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        Card card2 = new Card(Card.Color.GREEN, Card.Value.SEVEN);
        hand.addCard(card2);
        hand.removeCard(0);
        assertEquals(1, hand.getSize());
    }

    /**
     * Tests that remaining cards shift correctly after removal.
     * Verifies that the correct card is at index 0 after removing the first card.
     */
    @Test
    public void testRemoveCards() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        Card card2 = new Card(Card.Color.GREEN, Card.Value.SEVEN);
        hand.addCard(card2);
        hand.removeCard(0);
        assertEquals(card2, hand.getCard(0));
    }

    /**
     * Tests dealing initial cards from the deck.
     * Verifies that startCards removes exactly 7 cards from the deck
     * and adds them to the hand.
     */
    @Test
    public void testStartCard() {
        Hand hand = new Hand();
        ArrayList<Card> deck = new ArrayList<>();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        deck.add(card);
        Card card2 = new Card(Card.Color.RED, Card.Value.SKIP);
        deck.add(card2);
        Card card3 = new Card(Card.Color.BLUE, Card.Value.SIX);
        deck.add(card3);
        Card card4 = new Card(Card.Color.GREEN, Card.Value.ONE);
        deck.add(card4);
        Card card5 = new Card(Card.Color.YELLOW, Card.Value.THREE);
        deck.add(card5);
        Card card6 = new Card(Card.Color.GREEN, Card.Value.TWO);
        deck.add(card6);
        Card card7 = new Card(Card.Color.RED, Card.Value.EIGHT);
        deck.add(card7);
        hand.startCards(deck);
        assertEquals(0, deck.size());
    }
}
