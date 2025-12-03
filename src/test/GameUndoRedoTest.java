import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameUndoRedoTest {
    private Game game;

    @BeforeEach
    public void setup() {
        game = new Game();

        game.addPlayer(new Player("A"));
        game.addPlayer(new Player("B"));

        game.startGame();
    }

    private String currentPlayerName() {
        return game.getCurrentPlayer().getName();
    }

    @Test
    void testUndoRestoresTurnOrder() {
        String start = currentPlayerName();

        game.saveState();
        game.advanceTurn();

        String after = currentPlayerName();
        assertNotEquals(start, after);

        game.undo();

        String restored = currentPlayerName();
        assertEquals(start, restored);
    }

    @Test
    void testRedoClearedAfterNewAction() {
        game.saveState();
        game.advanceTurn();
        game.undo();

        assertTrue(game.canRedo());

        game.saveState();
        game.drawCardForCurrentPlayer();

        assertFalse(game.canRedo());
    }

    @Test
    void testUndoRestoresTopCard() {
        Card originalTop = game.getTopCard();

        Player p = game.getCurrentPlayer();
        p.getHand().addCard(new Card(Card.Color.RED, Card.Value.THREE));

        game.saveState();
        game.playCardFromHand(p.getHand().getSize() - 1);

        Card newTop = game.getTopCard();
        assertNotEquals(originalTop, newTop);

        game.undo();
        Card restored = game.getTopCard();
        assertEquals(originalTop, restored);
    }
}

