import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AIPlayer.
 * Tests AI player creation and decision-making logic.
 *
 * @author G27
 * @version 3.0
 */
public class AIPlayerTest {
    
    private AIPlayer aiPlayer;
    private Card topCard;
    
    @BeforeEach
    void setUp() {
        aiPlayer = new AIPlayer("TestAI");
    }
    
    @Test
    void testAIPlayerCreation() {
        assertEquals("TestAI (AI)", aiPlayer.getName());
        assertNotNull(aiPlayer.getHand());
        assertEquals(0, aiPlayer.getScore());
    }
    
    @Test
    void testAIPlayerIsInstanceOfPlayer() {
        assertTrue(aiPlayer instanceof Player);
        assertTrue(aiPlayer instanceof AIPlayer);
    }
    
    @Test
    void testAIPlayerHand() {
        // Test that AI can receive cards
        Card card1 = new Card(Card.Color.RED, Card.Value.FIVE);
        Card card2 = new Card(Card.Color.BLUE, Card.Value.SKIP);
        
        aiPlayer.getHand().addCard(card1);
        aiPlayer.getHand().addCard(card2);
        
        assertEquals(2, aiPlayer.getHand().getSize());
    }
    
    @Test
    void testAIPlayerCanDrawCards() {
        // Test AI drawing cards
        for (int i = 0; i < 7; i++) {
            aiPlayer.getHand().addCard(new Card(Card.Color.RED, Card.Value.ONE));
        }
        
        assertEquals(7, aiPlayer.getHand().getSize());
    }
    
    @Test
    void testAIPlayerScore() {
        // Test score manipulation
        aiPlayer.setScore(100);
        assertEquals(100, aiPlayer.getScore());
        
        aiPlayer.addScore(50);
        assertEquals(150, aiPlayer.getScore());
    }
    
    @Test
    void testAIPlayerToString() {
        String result = aiPlayer.toString();
        assertTrue(result.contains("TestAI (AI)"));
        assertTrue(result.contains("Score: 0"));
        assertTrue(result.contains("Cards: 0"));
    }
}