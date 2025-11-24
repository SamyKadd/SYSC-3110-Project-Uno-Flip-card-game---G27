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

    @Test
    void testStartGameRequiresMinimumTwoPlayers() {
        game.addPlayer(p1);
        assertThrows(IllegalStateException.class, () -> game.startGame());
    }

    @Test
    void testStartGameDealsSevenCardsToEachPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        assertEquals(7, p1.getHand().getSize());
        assertEquals(7, p2.getHand().getSize());
    }

    @Test
    void testGetCurrentPlayerReturnsFirstPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        assertSame(p1, game.getCurrentPlayer());
    }

    @Test
    void testTopCardExistsAfterGameStarts() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        assertNotNull(game.getTopCard());
    }

    @Test
    void testDrawCardForCurrentPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        int initialSize = p1.getHand().getSize();
        game.drawCardForCurrentPlayer();
        assertEquals(initialSize + 1, p1.getHand().getSize());
    }

    @Test
    void testAdvanceTurnChangesCurrentPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        Player firstPlayer = game.getCurrentPlayer();
        game.advanceTurn();
        Player secondPlayer = game.getCurrentPlayer();
        assertNotSame(firstPlayer, secondPlayer);
    }

    @Test
    void testSetTopWildColor() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        game.setTopWildColor(Card.Color.RED);
        // Test that a red card is now valid
        Card redCard = new Card(Card.Color.RED, Card.Value.FIVE);
        assertTrue(game.isValidPlay(redCard));
    }

    @Test
    void testIsValidPlayWithWildColor() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();
        game.setTopWildColor(Card.Color.BLUE);
        
        Card blueCard = new Card(Card.Color.BLUE, Card.Value.THREE);
        Card redCard = new Card(Card.Color.RED, Card.Value.THREE);
        
        assertTrue(game.isValidPlay(blueCard));
        assertFalse(game.isValidPlay(redCard));
    }

    @Test
    void testDarkSideCardsNotValidOnLightSide() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();

        assertEquals(Side.LIGHT, game.getCurrentSide());

        Card drawFive = new Card(Card.Color.TEAL, Card.Value.DRAW_FIVE);
        Card wildDrawColor = new Card(null, Card.Value.WILD_DRAW_COLOR);

        assertFalse(game.isValidPlay(drawFive));
        assertFalse(game.isValidPlay(wildDrawColor));
    }

    @Test
    void testFlipCardSwitchesSideToDark() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();

        assertEquals(Side.LIGHT, game.getCurrentSide());

        Player current = game.getCurrentPlayer();
        Card top = game.getTopCard();

        Card flipCard = new Card(top.getColor(), Card.Value.FLIP);
        current.getHand().addCard(flipCard);
        int flipIndex = current.getHand().getCardsList().indexOf(flipCard);

        game.playCardFromHand(flipIndex);

        assertEquals(Side.DARK, game.getCurrentSide(),
                "Side should be DARK after playing a FLIP card.");
    }

    @Test
    void testDrawFiveMakesNextPlayerDrawFiveAndSkip() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.startGame();

        Player first = game.getCurrentPlayer();
        Card lightTop = game.getTopCard();
        Card flipCard = new Card(lightTop.getColor(), Card.Value.FLIP);
        first.getHand().addCard(flipCard);
        int flipIndex = first.getHand().getCardsList().indexOf(flipCard);
        game.playCardFromHand(flipIndex);
        assertEquals(Side.DARK, game.getCurrentSide());

        game.advanceTurn();
        assertSame(p2, game.getCurrentPlayer());

        Player attacker = game.getCurrentPlayer();
        Card darkTop = game.getTopCard();
        Card drawFive = new Card(darkTop.getColor(), Card.Value.DRAW_FIVE);
        attacker.getHand().addCard(drawFive);
        int dfIndex = attacker.getHand().getCardsList().indexOf(drawFive);

        int before = p3.getHand().getSize();

        game.playCardFromHand(dfIndex);

        int after = p3.getHand().getSize();
        assertEquals(before + 5, after,
                "Next player should draw exactly 5 cards after DRAW_FIVE.");

        game.advanceTurn();
        assertSame(p1, game.getCurrentPlayer(),
                "After DRAW_FIVE, the drawn player should be skipped on the next turn.");
    }

    @Test
    void testWildDrawColorDrawsUntilMatchAndSkipsNextPlayer() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.startGame();

        int before = p2.getHand().getSize();

        game.setDarkWildColor(Card.Color.PURPLE);

        int after = p2.getHand().getSize();
        assertTrue(after > before,
                "Next player should draw at least one card after WILD_DRAW_COLOR.");

        game.advanceTurn();
        assertSame(p1, game.getCurrentPlayer(),
                "After WILD_DRAW_COLOR, the targeted player should be skipped.");
    }

    @Test
    void testSkipEveryoneCardSkipsAllOtherPlayers() {
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.startGame();

        Player current = game.getCurrentPlayer();
        Card top = game.getTopCard();

        // Flip to dark side
        Card flip = new Card(top.getColor(), Card.Value.FLIP);
        current.getHand().addCard(flip);
        game.playCardFromHand(current.getHand().getCardsList().indexOf(flip));

        // Now dark side → play SKIP_EVERYONE
        game.advanceTurn();  // go to next player
        Player attacker = game.getCurrentPlayer();
        Card skipEveryone = new Card(null, Card.Value.SKIP_EVERYONE);
        attacker.getHand().addCard(skipEveryone);

        game.playCardFromHand(attacker.getHand().getCardsList().indexOf(skipEveryone));

        game.advanceTurn();
        assertSame(attacker, game.getCurrentPlayer(),
                "Player should get another turn after SKIP_EVERYONE");
    }



}