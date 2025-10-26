import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private Player p1, p2, p3;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        game = new Game();
        p1 = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
    }

    @Test
    void testConstructor() {
        assertEquals(Game.class, game.getClass());
    }

    @Test
    void testAddPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertSame(p1, game.getPlayer(0));
        assertSame(p2, game.getPlayer(1));
    }

    @Test
    void testRemovePlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.removePlayer(p1);
        assertSame(p2, game.getPlayer(0));
        assertThrows(IndexOutOfBoundsException.class, () -> game.getPlayer(1));
    }

    @Test
    void testGetPlayer() {
        game.addPlayer(p1);
        assertSame(p1, game.getPlayer(0));
    }

    @Test
    void testStartGame() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertDoesNotThrow(() -> game.startGame());
    }

    @Test
    void testIsValidPlay() {
        Card wild = new Card(null, Card.Value.WILD);
        Card any = new Card(Card.Color.RED, Card.Value.FIVE);
        boolean r1 = game.isValidPlay(wild);
        boolean r2 = game.isValidPlay(any);
        assertEquals(true, r1);
        assertEquals(true, r2);
    }

    @Test
    void testHandleActionCard() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertDoesNotThrow(() -> game.handleActionCard(new Card(Card.Color.RED, Card.Value.SKIP)));
    }
}