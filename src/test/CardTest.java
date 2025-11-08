import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Card.
 * Tests card creation, action card detection,
 * and string representation.
 *
 * @author G27
 * @version 1.0
 */
public class CardTest {

    private Card redFive;
    private Card blueSkip;

    /**
     * Setup method that runs once before all tests.
     */
    @BeforeAll
    static void beforeAll() {
    }

    /**
     * Setup method that runs before each test.
     * Initializes a RED FIVE card and a BLUE SKIP card.
     */
    @BeforeEach
    void setUp() {
        redFive = new Card(Card.Color.RED, Card.Value.FIVE);
        blueSkip = new Card(Card.Color.BLUE, Card.Value.SKIP);
    }

    /**
     * Tests that the Card constructor correctly sets color and value.
     * Verifies that fields match the provided arguments.
     */
    @Test
    void constructor_setsFieldsFromArguments() {
        Card card = new Card(Card.Color.GREEN, Card.Value.NINE);
        assertEquals(Card.Color.GREEN, card.getColor(), "Color should match");
        assertEquals(Card.Value.NINE, card.getValue(), "Value should match");
    }

    /**
     * Tests the getColor method.
     * Verifies that the correct color is returned.
     */
    @Test
    void testGetColor() {
        assertEquals(Card.Color.RED, redFive.getColor());
    }

    /**
     * Tests the getValue method.
     * Verifies that the correct value is returned.
     */
    @Test
    void testGetValue() {
        assertEquals(Card.Value.FIVE, redFive.getValue());
    }

    /**
     * Tests the isActionCard method.
     * Verifies that action cards (SKIP, DRAW_ONE, REVERSE, WILD, WILD_DRAW_TWO)
     * are correctly identified, and number cards are not.
     */
    @Test
    void testIsAction() {
        Card a1 = new Card(Card.Color.YELLOW, Card.Value.SKIP);
        Card a2 = new Card(Card.Color.RED, Card.Value.DRAW_ONE);
        Card a3 = new Card(Card.Color.BLUE, Card.Value.REVERSE);
        Card a4 = new Card(Card.Color.GREEN, Card.Value.WILD);
        Card a5 = new Card(Card.Color.RED, Card.Value.WILD_DRAW_TWO);

        Card n1 = new Card(Card.Color.BLUE, Card.Value.ZERO);
        Card n2 = new Card(Card.Color.GREEN, Card.Value.FIVE);

        assertTrue(a1.isActionCard(), "SKIP should be the action");
        assertTrue(a2.isActionCard(), "DRAW_ONE should be the action");
        assertTrue(a3.isActionCard(), "REVERSE should be the action");
        assertTrue(a4.isActionCard(), "WILD should be the action");
        assertTrue(a5.isActionCard(), "WILD_DRAW_TWO should be the action");
        assertFalse(n1.isActionCard(), "ZERO should not be the action");
        assertFalse(n2.isActionCard(), "FIVE should not be theaction");
    }

    /**
     * Tests the toString method.
     * Verifies that the card's string representation is formatted correctly.
     */
    @Test
    void testToString() {
        String s = blueSkip.toString();
        assertEquals("BLUE SKIP", s);
    }
}
