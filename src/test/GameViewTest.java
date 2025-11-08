import javax.swing.*;

/**
 * Simple test to view the GameView GUI.
 * Creates sample data to see how the interface looks.
 *
 * @author G27
 * @version 2.0
 */
public class GameViewTest {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Creating GameView...");
            
            // Create the view
            GameView view = new GameView();
            
            // Create test data
            Hand testHand = new Hand();
            testHand.addCard(new Card(Card.Color.RED, Card.Value.THREE));
            testHand.addCard(new Card(Card.Color.BLUE, Card.Value.FIVE));
            testHand.addCard(new Card(Card.Color.GREEN, Card.Value.REVERSE));
            testHand.addCard(new Card(Card.Color.YELLOW, Card.Value.DRAW_ONE));
            testHand.addCard(new Card(Card.Color.RED, Card.Value.SKIP));
            testHand.addCard(new Card(null, Card.Value.WILD));
            testHand.addCard(new Card(Card.Color.BLUE, Card.Value.SEVEN));
            
            // Display the hand
            view.displayHand(testHand);
            
            // Set top card
            Card topCard = new Card(Card.Color.GREEN, Card.Value.TWO);
            view.updateTopCard(topCard);
            
            // Update other elements
            view.updateCurrentPlayer("Test Player");
            view.updateStatusMessage("Testing the view");
            
            // Create test players for scoreboard
            java.util.List<Player> players = new java.util.ArrayList<>();
            players.add(new Player("Player 1"));
            players.add(new Player("Player 2"));
            view.updateScoreboard(players);
            
            System.out.println("GameView created! Window should be visible.");
            
            // Add test listeners
            view.getDrawCardButton().addActionListener(e -> {
                System.out.println("Draw Card button works!");
            });
            
            view.getNextPlayerButton().addActionListener(e -> {
                System.out.println("Next Player button works!");
            });
            
            for (JButton btn : view.getCardButtons()) {
                btn.addActionListener(e -> {
                    System.out.println("Card button " + btn.getActionCommand() + " works!");
                });
            }
        });
    }
}