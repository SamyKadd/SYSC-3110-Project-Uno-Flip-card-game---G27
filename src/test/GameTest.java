import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Game.
 * Tests game initialization, player management, card validation,
 * and action card handling.
 *
 * @author G27
 * @version 1.0
 */
public class GameTest {

    private Game game;
    private Player p1, p2, p3;

    /**
     * Setup method that runs once before all tests.
     */
    @BeforeAll
    static void beforeAll() {
    }

    /**
     * Setup method that runs before each test.
     * Initializes a new Game and three Player objects.
     */
    @BeforeEach
    void setUp() {
        game = new Game();
        p1 = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
    }

    /**
     * Tests that the Game constructor creates a valid Game object.
     */
    @Test
    void testConstructor() {
        assertEquals(Game.class, game.getClass());
    }

    /**
     * Tests adding players to the game.
     * Verifies that players are added in the correct order.
     */
    @Test
    void testAddPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertSame(p1, game.getPlayer(0));
        assertSame(p2, game.getPlayer(1));
    }

    /**
     * Tests that the game enforces a maximum of 4 players.
     * Attempts to add a 5th player and verifies it is rejected.
     */
    @Test
    void testMaximumPlayers() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(new Player("p4"));

        Player p5 = new Player("p5");
        game.addPlayer(p5);

        assertThrows(IndexOutOfBoundsException.class, () -> game.getPlayer(4));
    }

    /**
     * Tests removing a player from the game.
     * Verifies that remaining players shift positions correctly.
     */
    @Test
    void testRemovePlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.removePlayer(p1);
        assertSame(p2, game.getPlayer(0));
        assertThrows(IndexOutOfBoundsException.class, () -> game.getPlayer(1));
    }

    /**
     * Tests retrieving a player by index.
     * Verifies that the correct player is returned.
     */
    @Test
    void testGetPlayer() {
        game.addPlayer(p1);
        assertSame(p1, game.getPlayer(0));
    }

    /**
     * Tests starting the game with valid player count.
     * Verifies that the game starts without throwing exceptions.
     */
    @Test
    void testStartGame() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertDoesNotThrow(() -> game.startGame());
    }

    /**
     * Tests basic card play validation.
     * Verifies that wild cards and regular cards can be validated.
     */
    @Test
    void testIsValidPlay() {
        Card wild = new Card(null, Card.Value.WILD);
        Card any = new Card(Card.Color.RED, Card.Value.FIVE);
        boolean r1 = game.isValidPlay(wild);
        boolean r2 = game.isValidPlay(any);
        assertEquals(true, r1);
        assertEquals(true, r2);
    }

    /**
     * Tests card validation for color and value matching.
     * Verifies that cards matching either color or value are valid.
     */
    @Test
    void testIsValidPlayColorMatch() {
        Card redFive = new Card(Card.Color.RED, Card.Value.FIVE);
        Card redTwo = new Card(Card.Color.RED, Card.Value.TWO);
        Card blueTwo = new Card(Card.Color.BLUE, Card.Value.TWO);
        
        assertTrue(game.isValidPlay(redTwo));
        assertTrue(game.isValidPlay(blueTwo));
    }

    /**
     * Tests that wild cards are always valid to play.
     * Verifies both WILD and WILD_DRAW_TWO cards.
     */
    @Test
    void testWildCardAlwaysValid() {
        Card wild = new Card(null, Card.Value.WILD);
        Card wildDrawTwo = new Card(null, Card.Value.WILD_DRAW_TWO);
        
        assertTrue(game.isValidPlay(wild));
        assertTrue(game.isValidPlay(wildDrawTwo));
    }

    /**
     * Tests handling of action cards.
     * Verifies that action cards can be processed without errors.
     */
    @Test
    void testHandleActionCard() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertDoesNotThrow(() -> game.handleActionCard(new Card(Card.Color.RED, Card.Value.SKIP)));
    }
}