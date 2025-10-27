import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        
        System.out.println("Welcome to UNO!");
        System.out.println("================");
        
        // Ask for number of players
        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.print("How many players? (2-4): ");
            try {
                numPlayers = Integer.parseInt(scanner.nextLine().trim());
                if (numPlayers < 2 || numPlayers > 4) {
                    System.out.println("Please enter a number between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

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