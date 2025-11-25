import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for AIPlayer behavior in game scenarios.
 * Tests AI player as it interacts with the Model layer.
 * Note: AI decision-making is handled by GameController, so these tests
 * focus on AIPlayer as a Player subclass and basic game integration.
 *
 * @author G27
 * @version 3.0
 */
public class AIPlayerIntegrationTest {
    
    private Game game;
    private AIPlayer ai1;
    private Player human;
    
    @BeforeEach
    void setUp() {
        game = new Game();
        ai1 = new AIPlayer("AI Test");
        human = new Player("Human");
    }
    
    /**
     * Tests that AI player can be added to game and starts normally.
     */
    @Test
    void testAIPlayerCanBeAddedToGame() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        
        assertDoesNotThrow(() -> game.startGame());
        assertEquals(7, ai1.getHand().getSize(), "AI should receive 7 starting cards");
    }
    
    /**
     * Tests that Model allows AI to play valid cards.
     */
    @Test
    void testModelAcceptsAICardPlay() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        // Force AI to be current player
        while (!(game.getCurrentPlayer() instanceof AIPlayer)) {
            game.advanceTurn();
        }
        
        Card topCard = game.getTopCard();
        Card matchingCard = new Card(topCard.getColor(), Card.Value.FIVE);
        
        ai1.getHand().getCardsList().clear();
        ai1.getHand().addCard(matchingCard);
        
        int initialSize = ai1.getHand().getSize();
        boolean played = game.playCardFromHand(0);
        
        assertTrue(played, "Model should accept valid card from AI");
        assertEquals(initialSize - 1, ai1.getHand().getSize(), 
            "Hand size should decrease after play");
    }
    
    /**
     * Tests that AI can draw cards from the deck.
     */
    @Test
    void testAICanDrawCards() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        // Force AI to be current player
        while (!(game.getCurrentPlayer() instanceof AIPlayer)) {
            game.advanceTurn();
        }
        
        int initialSize = ai1.getHand().getSize();
        boolean drew = game.drawCardForCurrentPlayer();
        
        assertTrue(drew, "AI should be able to draw cards");
        assertEquals(initialSize + 1, ai1.getHand().getSize(),
            "Hand size should increase after draw");
    }
    
    /**
     * Tests that wild cards are valid plays for AI.
     */
    @Test
    void testWildCardsValidForAI() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        Card wild = new Card(null, Card.Value.WILD);
        Card wildDrawTwo = new Card(null, Card.Value.WILD_DRAW_TWO);
        
        assertTrue(game.isValidPlay(wild), 
            "WILD should be valid for AI");
        assertTrue(game.isValidPlay(wildDrawTwo),
            "WILD_DRAW_TWO should be valid for AI");
    }
    
    /**
     * Tests multiple AI players in sequence.
     */
    @Test
    void testMultipleAIPlayersInSequence() {
        AIPlayer ai2 = new AIPlayer("AI 2");
        AIPlayer ai3 = new AIPlayer("AI 3");
        
        game.addPlayer(ai1);
        game.addPlayer(ai2);
        game.addPlayer(ai3);
        game.addPlayer(human);
        game.startGame();
        
        // Verify all players initialized correctly
        assertEquals(7, ai1.getHand().getSize());
        assertEquals(7, ai2.getHand().getSize());
        assertEquals(7, ai3.getHand().getSize());
        assertEquals(7, human.getHand().getSize());
        
        // Advance through turns
        assertDoesNotThrow(() -> {
            game.advanceTurn();
            game.advanceTurn();
            game.advanceTurn();
        }, "Game should handle multiple AI players");
    }
    
    /**
     * Tests that AI can win the game.
     */
    @Test
    void testAICanWinGame() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        // Force AI to be current player
        while (!(game.getCurrentPlayer() instanceof AIPlayer)) {
            game.advanceTurn();
        }
        
        Card topCard = game.getTopCard();
        
        // Give AI only one matching card (winning condition)
        ai1.getHand().getCardsList().clear();
        ai1.getHand().addCard(new Card(topCard.getColor(), Card.Value.FIVE));
        
        int initialScore = ai1.getScore();
        
        // AI plays last card
        game.playCardFromHand(0);
        
        assertEquals(0, ai1.getHand().getSize(), "AI should have empty hand");
        assertTrue(ai1.getScore() > initialScore, 
            "AI should receive points for winning");
    }
    
    /**
     * Tests AI with action cards.
     */
    @Test
    void testAICanPlayActionCards() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        // Force AI to be current player
        while (!(game.getCurrentPlayer() instanceof AIPlayer)) {
            game.advanceTurn();
        }
        
        Card topCard = game.getTopCard();
        
        // Give AI a SKIP card
        Card skipCard = new Card(topCard.getColor(), Card.Value.SKIP);
        ai1.getHand().getCardsList().clear();
        ai1.getHand().addCard(skipCard);
        
        boolean played = game.playCardFromHand(0);
        
        assertTrue(played, "AI should be able to play action cards");
    }
 
    /**
     * Tests AI on dark side.
     */
    @Test
    void testAIOnDarkSide() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        // Switch to dark side
        Card topCard = game.getTopCard();
        Card flipCard = new Card(topCard.getColor(), Card.Value.FLIP);
        game.getCurrentPlayer().getHand().addCard(flipCard);
        game.playCardFromHand(game.getCurrentPlayer().getHand()
            .getCardsList().indexOf(flipCard));
        
        assertEquals(Side.DARK, game.getCurrentSide(),
            "Game should be on dark side");
        
        // Force AI to be current
        while (!(game.getCurrentPlayer() instanceof AIPlayer)) {
            game.advanceTurn();
        }
        
        // AI should be able to play dark cards
        Card darkTop = game.getTopCard();
        if (darkTop.getColor() != null) {
            Card darkCard = new Card(darkTop.getColor(), Card.Value.DRAW_FIVE);
            assertTrue(game.isValidPlay(darkCard),
                "Dark cards should be valid on dark side");
        }
    }
    
    /**
     * Tests that game tracks AI vs human players correctly.
     */
    @Test
    void testGameTracksPlayerTypes() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        AIPlayer ai2 = new AIPlayer("AI 2");
        game.addPlayer(ai2);
        game.startGame();
        
        int aiCount = 0;
        int humanCount = 0;
        
        for (int i = 0; i < 3; i++) {
            Player p = game.getPlayer(i);
            if (p instanceof AIPlayer) {
                aiCount++;
            } else {
                humanCount++;
            }
        }
        
        assertEquals(2, aiCount, "Should have 2 AI players");
        assertEquals(1, humanCount, "Should have 1 human player");
    }
    
    /**
     * Tests PropertyChangeListener fires for game state changes.
     */
    @Test
    void testPropertyChangeListenerFires() {
        final boolean[] listenerFired = {false};
        
        game.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                listenerFired[0] = true;
            }
        });
        
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        assertTrue(listenerFired[0], 
            "PropertyChangeListener should fire when game starts");
    }
    
    /**
     * Tests game with maximum AI players (3 AI + 1 human).
     */
    @Test
    void testMaximumAIPlayers() {
        AIPlayer ai2 = new AIPlayer("AI 2");
        AIPlayer ai3 = new AIPlayer("AI 3");
        
        game.addPlayer(ai1);
        game.addPlayer(ai2);
        game.addPlayer(ai3);
        game.addPlayer(human);
        
        assertDoesNotThrow(() -> game.startGame(),
            "Game should support 3 AI + 1 human");
        
        assertEquals(4, (game.getPlayer(0).getHand().getSize() +
                        game.getPlayer(1).getHand().getSize() +
                        game.getPlayer(2).getHand().getSize() +
                        game.getPlayer(3).getHand().getSize()) / 7,
            "All 4 players should have cards");
    }
    
    /**
     * Tests turn advancement with AI players.
     */
    @Test
    void testTurnAdvancementWithAI() {
        game.addPlayer(ai1);
        game.addPlayer(human);
        game.startGame();
        
        Player first = game.getCurrentPlayer();
        game.advanceTurn();
        Player second = game.getCurrentPlayer();
        
        assertNotSame(first, second,
            "Turn should advance to different player");
    }
}