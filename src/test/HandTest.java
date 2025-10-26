import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

}
