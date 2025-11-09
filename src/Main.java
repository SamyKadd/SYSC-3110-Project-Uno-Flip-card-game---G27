import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game model = new Game();
            GameView view = new GameView();
            new GameController(model, view);

            // Show player selection dialog
            int numPlayers = PlayerSelectionDialog.showDialog(view);

            // If user cancels, close the app
            if (numPlayers == -1) {
                System.exit(0);
            }

            // Create players automatically
            for (int i = 1; i <= numPlayers; i++) {
                model.addPlayer(new Player("Player " + i));
            }

            // Start the game
            model.startGame();
        });
    }
}
