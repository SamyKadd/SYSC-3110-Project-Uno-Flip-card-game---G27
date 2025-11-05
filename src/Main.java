import java.util.Scanner;

/**
 * Entry point for the UNO card game application.
 * Prompts users for number of players and their names,
 * then starts the game.
 *
 * @author G27
 * @version 1.0
 */
public class Main {
    /**
     * Main method to start the UNO game.
     * Prompts for player count (2-4) and player names,
     * then initializes and starts the game.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        
        // Using GUI dialog for player selection
        int numPlayers = PlayerSelectionDialog.showDialog(null);
        
        if (numPlayers == -1) {
            System.out.println("Game cancelled.");
            System.exit(0);
        }

        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        
        System.out.println("Welcome to UNO!");
        System.out.println("================");

        // Get player names
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + i;
            }
            game.addPlayer(new Player(name));
        }

        System.out.println();
        game.startGame();

    }
}