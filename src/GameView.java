import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The View component of the MVC pattern for the UNO game.
 * Displays the game state including top card, player's hand, 
 * status messages, and game controls.
 *
 * @author G27
 * @version 2.0
 */
public class GameView extends JFrame {
    
    // These are the bare minimum I imagine we'll need to show the game state, I'll remove
    // and add some as we progress
    // Top panel components
    private JLabel topCardLabel;
    private JPanel topCardPanel;
    
    // Status panel components
    private JLabel currentPlayerLabel;
    private JLabel statusMessageLabel;
    
    // Center panel for player's hand
    private JPanel handPanel;
    private ArrayList<JButton> cardButtons;
    
    // Bottom panel components
    private JButton nextPlayerButton;
    private JButton drawCardButton;
    
    // Scoreboard
    private JLabel scoreboardLabel;
    
    /**
     * Constructs the GameView window.
     * Sets up all GUI components and layout.
     */
    public GameView() {
        setTitle("Uno Flip Game");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        cardButtons = new ArrayList<>();
        
        initializeComponents();
        
        setVisible(true);
    }

    /**
     * Initializes all GUI components and adds them to the frame.
     */
    private void initializeComponents() {}
}