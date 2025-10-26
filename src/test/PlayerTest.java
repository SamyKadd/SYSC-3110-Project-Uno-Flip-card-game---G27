import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class PlayerTest{
    private Player player;

    @BeforeEach
    void setUp(){
        player = new Player("Alice");
    }

    @Test
    void testInitializations(){
        assertEquals("Alice", player.getName(), "Player name was set");
        assertEquals(0, player.getScore(),"Initial score of player is 0");
        assertNotNull(player.getHand(), "Player hand should not be null");
        assertEquals(0, player.getHand().getSize(), "New player should begin with empty hand");
    }

    @Test
    void testScore(){
        player.setScore(5);
        assertEquals(5, player.getScore(), "Player score was updated to 5");
    }

    @Test
    void testAddScore(){
        player.addScore(5);
        assertEquals(5, player.getScore(), "Player score increases to 5");
        player.addScore(5);
        assertEquals(10, player.getScore(), "Player score should correctly increase by 5 points, resulting in a total of 10.");
    }

    @Test
    void testGetHand(){
        Hand first = player.getHand();
        Hand second = player.getHand();
        assertSame(first, second);  //ensures player keeps one consistent hand throughout the game
    }
    @Test
    void testToString(){
        Player player = new Player("Alice");
        String result = player.toString();
        assertEquals("Player Alice (Score: 0, Cards: 0)", result);
    }

    //@Test
}
