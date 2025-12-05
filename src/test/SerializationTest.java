import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for serialization and deserialization functionality.
 * Tests the ability to save game state to a file and reload it accurately.
 *
 * @author G27
 * @version 4.0
 */
public class SerializationTest {
    
    private Game game;
    private static final String TEST_FILE = "test_game.uno";
    
    /**
     * Set up a new game before each test.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        game.addPlayer(new Player("Alice"));
        game.addPlayer(new Player("Bob"));
        game.startGame();
    }
    
    /**
     * Clean up test files after each test.
     */
    @AfterEach
    public void tearDown() {
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    /**
     * Tests that saveGame creates a file on disk.
     */
    @Test
    public void testSaveGameCreatesFile() {
        boolean success = game.saveGame(TEST_FILE);
        
        assertTrue(success, "Save operation should succeed");
        
        File savedFile = new File(TEST_FILE);
        assertTrue(savedFile.exists(), "Save file should exist");
        assertTrue(savedFile.length() > 0, "Save file should not be empty");
    }
    
    /**
     * Tests that loadGame returns a non-null Game object.
     */
    @Test
    public void testLoadGameReturnsNonNull() {
        game.saveGame(TEST_FILE);
        
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        assertNotNull(loadedGame, "Loaded game should not be null");
    }
    
    /**
     * Tests that the number of players is preserved after save/load.
     */
    @Test
    public void testLoadGameRestoresPlayerCount() {
        game.saveGame(TEST_FILE);
        
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        assertEquals(game.getPlayer(0).getName(), loadedGame.getPlayer(0).getName(),
            "Player count should be preserved");
        assertEquals(game.getPlayer(1).getName(), loadedGame.getPlayer(1).getName(),
            "Player count should be preserved");
    }
    
    /**
     * Tests that hand sizes are preserved after save/load.
     */
    @Test
    public void testLoadGameRestoresHandSizes() {
        int size1 = game.getPlayer(0).getHand().getSize();
        int size2 = game.getPlayer(1).getHand().getSize();
        
        game.saveGame(TEST_FILE);
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        assertEquals(size1, loadedGame.getPlayer(0).getHand().getSize(),
            "Player 1 hand size should be preserved");
        assertEquals(size2, loadedGame.getPlayer(1).getHand().getSize(),
            "Player 2 hand size should be preserved");
    }
    
    /**
     * Tests that player scores are preserved after save/load.
     */
    @Test
    public void testLoadGameRestoresScores() {
        game.getPlayer(0).setScore(100);
        game.getPlayer(1).setScore(250);
        
        game.saveGame(TEST_FILE);
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        assertEquals(100, loadedGame.getPlayer(0).getScore(),
            "Player 1 score should be preserved");
        assertEquals(250, loadedGame.getPlayer(1).getScore(),
            "Player 2 score should be preserved");
    }
    
    /**
     * Tests that the top card is preserved after save/load.
     */
    @Test
    public void testLoadGameRestoresTopCard() {
        Card originalTop = game.getTopCard();
        
        game.saveGame(TEST_FILE);
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        Card loadedTop = loadedGame.getTopCard();
        assertNotNull(loadedTop, "Top card should not be null");
        assertEquals(originalTop.getColor(), loadedTop.getColor(),
            "Top card color should be preserved");
        assertEquals(originalTop.getValue(), loadedTop.getValue(),
            "Top card value should be preserved");
    }
    
    /**
     * Tests that loading a non-existent file returns null.
     */
    @Test
    public void testLoadNonExistentFileReturnsNull() {
        Game loadedGame = Game.loadGame("nonexistent_file.uno");
        
        assertNull(loadedGame, "Loading non-existent file should return null");
    }
    
    /**
     * Tests that game state is preserved after playing cards and saving.
     */
    @Test
    public void testSaveAfterGameplayRestoresState() {
        Player p1 = game.getPlayer(0);
        int originalHandSize = p1.getHand().getSize();
        
        game.drawCardForCurrentPlayer();
        int newHandSize = p1.getHand().getSize();
        
        game.saveGame(TEST_FILE);
        Game loadedGame = Game.loadGame(TEST_FILE);
        
        assertEquals(newHandSize, loadedGame.getPlayer(0).getHand().getSize(),
            "Hand size after draw should be preserved");
        assertNotEquals(originalHandSize, loadedGame.getPlayer(0).getHand().getSize(),
            "Hand size should reflect the draw action");
    }
    
    /**
     * Tests that multiple save/load cycles preserve game state correctly.
     */
    @Test
    public void testMultipleSaveLoadCycles() {
        String file1 = "test_save_1.uno";
        String file2 = "test_save_2.uno";
        
        try {
            game.getPlayer(0).setScore(50);
            game.saveGame(file1);
            
            Game loaded1 = Game.loadGame(file1);
            loaded1.getPlayer(0).setScore(150);
            loaded1.saveGame(file2);
            
            Game loaded2 = Game.loadGame(file2);
            
            assertEquals(150, loaded2.getPlayer(0).getScore(),
                "Score should reflect changes through multiple cycles");
            
        } finally {
            new File(file1).delete();
            new File(file2).delete();
        }
    }
}