import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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

    private GameUIListener uiListener;
    
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

        JScrollPane handScrollPane = new JScrollPane(
                handPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        handScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Constrain height properly
        handScrollPane.setPreferredSize(new Dimension(1400, 240));

        add(handScrollPane, BorderLayout.CENTER);
        
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
        panel.setPreferredSize(new Dimension(1400, 200));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        // Title label
        JLabel titleLabel = new JLabel("Top Card");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(titleLabel, BorderLayout.WEST);
        
        // Top card display
        topCardPanel = new JPanel();
        topCardPanel.setPreferredSize(new Dimension(100, 140));
        topCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        topCardPanel.setBackground(Color.WHITE);
        
        topCardLabel = new JLabel("", SwingConstants.CENTER);
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topCardPanel.add(topCardLabel);
        
        topCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(topCardPanel);
        panel.add(leftPanel, BorderLayout.WEST);
        
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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 30));
        panel.setBorder(BorderFactory.createTitledBorder("Your Hand"));
        panel.setBackground(new Color(200, 220, 240));
        panel.setPreferredSize(new Dimension(1400, 300));
        return panel;
    }

    /**
     * Creates the bottom panel with game controls and scoreboard.
     *
     * @return JPanel containing controls and scoreboard
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(1400, 80));

        // Left side - control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        nextPlayerButton = new JButton("NEXT PLAYER");
        nextPlayerButton.setFont(new Font("Arial", Font.BOLD, 12));
        nextPlayerButton.setPreferredSize(new Dimension(150, 40));
        
        drawCardButton = new JButton("DRAW CARD");
        drawCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        drawCardButton.setPreferredSize(new Dimension(150, 40));

        nextPlayerButton.addActionListener(e-> {if (uiListener !=null) uiListener.onNext();});
        drawCardButton.addActionListener(e->{if (uiListener !=null ) uiListener.onDraw();});

        controlPanel.add(nextPlayerButton);
        controlPanel.add(drawCardButton);
        
        panel.add(controlPanel, BorderLayout.WEST);
        
        // Right side - scoreboard
        scoreboardLabel = new JLabel("Scoreboard: ");
        scoreboardLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(scoreboardLabel, BorderLayout.EAST);
        
        return panel;
    }

    /**
     * Updates the display of the top card.
     *
     * @param card the card to display
     */
    public void updateTopCard(Card card) {
        if (card != null) {
            topCardLabel.setText(formatCardText(card));
            topCardPanel.setBackground(getColorForCard(card));
        }
    }
    
    /**
     * Updates the top card display with a wild card color.
     *
     * @param color the color chosen for the wild card
     */
    public void updateTopCardWild(Card.Color color) {
        topCardLabel.setText(color.toString());
        topCardPanel.setBackground(getColorForCardColor(color));
    }

    /**
     * Displays the cards in the player's hand.
     *
     * @param hand the Hand object containing the player's cards
     */
    public void displayHand(Hand hand) {
        handPanel.removeAll();
        cardButtons.clear();
        
        for (int i = 0; i < hand.getSize(); i++) {
            Card card = hand.getCard(i);
            JButton cardButton = createCardButton(card, i);
            cardButtons.add(cardButton);
            handPanel.add(cardButton);
        }

        // Will have to implement these        
        handPanel.revalidate();
        handPanel.repaint();
    }

    /**
     * Creates a button representing a card in the player's hand.
     *
     * @param card the card to represent
     * @param index the position of the card in the hand
     * @return JButton styled as a card
     */
    private JButton createCardButton(Card card, int index) {
        JButton button = new JButton("<html><center>" + formatCardText(card) + "</center></html>");
        button.setPreferredSize(new Dimension(120, 180));
        button.setBackground(getColorForCard(card));
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        button.setFocusPainted(false);
        
        // Store the index in the button's action command
        button.setActionCommand(String.valueOf(index));
        button.addActionListener(e -> {
            if (uiListener != null){
                uiListener.onPlayCard((Integer.parseInt((e.getActionCommand()))));
            }
        });

        return button;
    }
    
    /**
     * Formats card text for display.
     *
     * @param card the card to format
     * @return formatted string representation
     */
    private String formatCardText(Card card) {
        if (card.getColor() == null) {
            return card.getValue().toString().replace("_", " ");
        }
        return card.getColor().toString() + "<br>" + 
               card.getValue().toString().replace("_", " ");
    }
    
    /**
     * Gets the background color for a card.
     *
     * @param card the card
     * @return Color object for the card's background
     */
    private Color getColorForCard(Card card) {
        if (card.getColor() == null) {
            return Color.BLACK;
        }
        return getColorForCardColor(card.getColor());
    }

    /**
     * Converts Card.Color enum to java.awt.Color.
     *
     * @param color the Card.Color
     * @return corresponding java.awt.Color
     */
    private Color getColorForCardColor(Card.Color color) {
        switch (color) {
            case RED:
                return new Color(255, 80, 80);
            case BLUE:
                return new Color(80, 150, 255);
            case GREEN:
                return new Color(80, 200, 120);
            case YELLOW:
                return new Color(255, 220, 80);
            default:
                return Color.WHITE;
        }
    }
    
    /**
     * Updates the current player display.
     *
     * @param playerName name of the current player
     */
    public void updateCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Current Player: " + playerName);
    }
    
    /**
     * Updates the status message.
     *
     * @param message the status message to display
     */
    public void updateStatusMessage(String message) {
        statusMessageLabel.setText("Status Message: " + message);
    }

    /**
     * Updates the scoreboard display.
     *
     * @param players list of players with their scores
     */
    public void updateScoreboard(java.util.List<Player> players) {
        StringBuilder sb = new StringBuilder("Scoreboard: ");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            sb.append(p.getName()).append(": ").append(p.getScore());
            if (i < players.size() - 1) {
                sb.append(" | ");
            }
        }
        scoreboardLabel.setText(sb.toString());
    }

    /**
     * Render the view
     *
     * @param s is the current games state
     */
    public void render(GameState s){
        if (s.topCard != null) {
            updateTopCard(s.topCard);
        }
        if (s.curPlayerName != null) {
            updateCurrentPlayer(s.curPlayerName);
        }
        updateStatusMessage(s.statusMessage == null ? "" : s.statusMessage);

        drawCardButton.setEnabled(s.canDraw);
        nextPlayerButton.setEnabled(s.canNext);

        handPanel.removeAll();
        cardButtons.clear();
        List<Card> hand = s.curHand == null ? Collections.emptyList() : s.curHand;
        for (int i = 0; i < hand.size(); i++) {
            JButton cardBtn = createCardButton(hand.get(i), i);
            cardButtons.add(cardBtn);
            handPanel.add(cardBtn);
        }
        handPanel.revalidate();
        handPanel.repaint();

        if (s.needsWildColor) {
            Card.Color chosen = promptForWildColor();
            if (uiListener != null && chosen != null) {
                uiListener.onChooseWildCardCol(chosen);
            }
        }
    }
    
    /**
     * Gets the next player button.
     *
     * @return the next player button
     */
    public JButton getNextPlayerButton() {
        return nextPlayerButton;
    }
    
    /**
     * Gets the draw card button.
     *
     * @return the draw card button
     */
    public JButton getDrawCardButton() {
        return drawCardButton;
    }
    
    /**
     * Gets the list of card buttons.
     *
     * @return ArrayList of card buttons
     */
    public ArrayList<JButton> getCardButtons() {
        return cardButtons;
    }

    /**
     * Setting the listener
     *
     * @param listener the listener to set
     */
    public void setListener(GameUIListener listener){
        this.uiListener = listener;
    }

    /**
     * Displays an error message dialog.
     *
     * @param message the error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Displays an information message dialog.
     *
     * @param message the information message
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Prompts the user to select a color for a wild card.
     *
     * @return the selected color
     */
    public Card.Color promptForWildColor() {
        Object[] options = {"Red", "Blue", "Green", "Yellow"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a color for the wild card:",
                "Wild Card",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        switch (choice) {
            case 0: return Card.Color.RED;
            case 1: return Card.Color.BLUE;
            case 2: return Card.Color.GREEN;
            case 3: return Card.Color.YELLOW;
            default: return Card.Color.RED;
        }
    }
}