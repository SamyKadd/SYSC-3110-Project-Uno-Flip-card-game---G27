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
    private void initializeComponents() {
        // Top panel - shows the top card
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - shows player's hand
        handPanel = createHandPanel();
        add(handPanel, BorderLayout.CENTER);
        
        // Bottom panel - shows controls and scoreboard
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the top panel displaying the current top card.
     *
     * @return JPanel containing top card display
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title label
        JLabel titleLabel = new JLabel("Top Card");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Top card display
        topCardPanel = new JPanel();
        topCardPanel.setPreferredSize(new Dimension(150, 100));
        topCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        topCardPanel.setBackground(Color.WHITE);
        
        topCardLabel = new JLabel("", SwingConstants.CENTER);
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topCardPanel.add(topCardLabel);
        
        panel.add(topCardPanel, BorderLayout.CENTER);
        
        // Status section
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        currentPlayerLabel = new JLabel("Current Player: ");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusMessageLabel = new JLabel("Status Message: ");
        statusMessageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        statusPanel.add(currentPlayerLabel);
        statusPanel.add(statusMessageLabel);
        
        panel.add(statusPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Creates the center panel for displaying player's cards.
     *
     * @return JPanel for the player's hand
     */
    private JPanel createHandPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setBorder(BorderFactory.createTitledBorder("Your Hand"));
        panel.setBackground(new Color(200, 220, 240));
        return panel;
    }
}