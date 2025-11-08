import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Test class for Player.
 * Tests player initialization, scoring functionality,
 * and hand management.
 *
 * @author G27
 * @version 1.0
 */
public class PlayerTest{
    private Player player;

    /**
     * Setup method that runs once before all tests.
     */
    @BeforeAll
    static void beforeAll() {
    }

    /**
     * Setup method that runs before each test.
     * Initializes a new Player named "Alice".
     */
    @BeforeEach
    void setUp(){
        player = new Player("Alice");
    }

    /**
     * Tests player initialization.
     * Verifies that name, score, and hand are properly initialized.
     */
    @Test
    void testInitializations(){
        assertEquals("Alice", player.getName(), "Player name was set");
        assertEquals(0, player.getScore(),"Initial score of player is 0");
        assertNotNull(player.getHand(), "Player hand should not be null");
        assertEquals(0, player.getHand().getSize(), "New player should begin with empty hand");
    }

    /**
     * Tests setting a player's score.
     * Verifies that the score is updated correctly.
     */
    @Test
    void testScore(){
        player.setScore(5);
        assertEquals(5, player.getScore(), "Player score was updated to 5");
    }

    /**
     * Tests adding points to a player's score.
     * Verifies that scores accumulate correctly.
     */
    @Test
    void testAddScore(){
        player.addScore(5);
        assertEquals(5, player.getScore(), "Player score increases to 5");
        player.addScore(5);
        assertEquals(10, player.getScore(), "Player score should correctly increase by 5 points, resulting in a total of 10.");
    }

    /**
     * Tests that getHand returns the same Hand object consistently.
     * Verifies that a player maintains a single hand throughout the game.
     */
    @Test
    void testGetHand(){
        Hand first = player.getHand();
        Hand second = player.getHand();
        assertSame(first, second);  //ensures player keeps one consistent hand throughout the game
    }

    /**
     * Tests the toString method.
     * Verifies that the player's string representation is formatted correctly.
     */
    @Test
    void testToString(){
        Player player = new Player("Alice");
        String result = player.toString();
        assertEquals("Player Alice (Score: 0, Cards: 0)", result);
    }

}
