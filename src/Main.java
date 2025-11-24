import javax.swing.*;

/**
 * Main entry point for the UNO Flip card game application.
 * This class initializes the MVC components (Model, View, Controller)
 * and handles player selection dialogs for both human and AI players.
 * 
 * The application supports 2-4 total players in any combination of
 * human and AI players.
 * 
 * @author G27
 * @version 3.0
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game model = new Game();
            GameView view = new GameView();

            // Show player selection dialog
            int numPlayers = PlayerSelectionDialog.showDialog(view);

            // If user cancels, close the app
            if (numPlayers == -1) {
                System.exit(0);
            }

            // Calculate how many AI players we can add (we can have a max of 4 total players)
            int maxAIPlayers = 4 - numPlayers;
            int numAIPlayers = 0;
            
            if (maxAIPlayers > 0) {
                numAIPlayers = AIPlayerSelectionDialog.showDialog(view);
                if (numAIPlayers == -1) {
                    numAIPlayers = 0; // Default to no AI if cancelled
                }
                
                // Ensure we don't exceed 4 total players
                numAIPlayers = Math.min(numAIPlayers, maxAIPlayers);
            }

            // Create players automatically
            for (int i = 1; i <= numPlayers; i++) {
                model.addPlayer(new Player("Player " + i));
            }

            // Create AI players automatically
            for (int i = 1; i <= numAIPlayers; i++) {
                model.addPlayer(new AIPlayer("AI Player " + i));
            }
            new GameController(model, view);
            // Start the game
            model.startGame();
        });
    }
}
