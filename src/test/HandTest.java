import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    @Test
    public void testHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.getSize());
    }

    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        assertEquals(1, hand.getSize());
    }
    @Test
    public void testAddCards() {
        Hand hand = new Hand();
        Card card = new Card(Card.Color.GREEN, Card.Value.EIGHT);
        hand.addCard(card);
        assertEquals(card, hand.getCard(0));
    }

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
