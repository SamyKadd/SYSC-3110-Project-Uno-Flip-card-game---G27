import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the main UNO game logic and state.
 * This class manages players, the deck, turns, and game rules.
 * Supports 2-4 players and handles all UNO card actions.
 * 
 * @author 
 * @version 1.0
 */
public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;
    private int currentPlayerIndex;
    private boolean clockwise; //+1 forward and -1 reverse order
    private Card top; //The card thats on the top of the discard pile
    private Card.Color topWild = null; //If the top card on discard pile is wild card

    /**
     * Constructs a new Game instance.
     * Initializes an empty player list, creates and shuffles the deck,
     * and sets the game to start with the first player going clockwise.
     */
    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
        // Initialize deck with cards
        initializeDeck();
    }

    // Not 100% sure if this is correct/works, have to test this later
    /**
     * Initializes the UNO deck with all standard cards.
     * Creates:
     * - One 0 card per color (4 total)
     * - Two of each numbered card 1-9 per color (72 total)
     * - Two of each action card (SKIP, DRAW_ONE, REVERSE) per color (24 total)
     * - Four WILD cards (4 total)
     * - Four WILD_DRAW_TWO cards (4 total)
     * Total: 108 cards
     * The deck is shuffled after creation.
     */
    private void initializeDeck() {
        // Adding numbered cards (0-9)
        for (Card.Color color : Card.Color.values()) {
            // According to the UNO website, there is only one 0 card per color
            deck.add(new Card(color, Card.Value.ZERO));

            // Every other number has two per color
            deck.add(new Card(color, Card.Value.ONE));
            deck.add(new Card(color, Card.Value.ONE));
            deck.add(new Card(color, Card.Value.TWO));
            deck.add(new Card(color, Card.Value.TWO));
            deck.add(new Card(color, Card.Value.THREE));
            deck.add(new Card(color, Card.Value.THREE));
            deck.add(new Card(color, Card.Value.FOUR));
            deck.add(new Card(color, Card.Value.FOUR));
            deck.add(new Card(color, Card.Value.FIVE));
            deck.add(new Card(color, Card.Value.FIVE));
            deck.add(new Card(color, Card.Value.SIX));
            deck.add(new Card(color, Card.Value.SIX));
            deck.add(new Card(color, Card.Value.SEVEN));
            deck.add(new Card(color, Card.Value.SEVEN));
            deck.add(new Card(color, Card.Value.EIGHT));
            deck.add(new Card(color, Card.Value.EIGHT));
            deck.add(new Card(color, Card.Value.NINE));
            deck.add(new Card(color, Card.Value.NINE));

            // Adding two of each action card per color
            deck.add(new Card(color, Card.Value.SKIP));
            deck.add(new Card(color, Card.Value.SKIP));
            deck.add(new Card(color, Card.Value.DRAW_ONE));
            deck.add(new Card(color, Card.Value.DRAW_ONE));
            deck.add(new Card(color, Card.Value.REVERSE));
            deck.add(new Card(color, Card.Value.REVERSE));
        }

        // Wild cards (4 of each type, no color)
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(null, Card.Value.WILD));
            deck.add(new Card(null, Card.Value.WILD_DRAW_TWO));
        }

        // Shuffle the deck
        Collections.shuffle(deck);
    }

    /**
     * Adds a player to the game.
     * Maximum of 4 players allowed.
     * 
     * @param p the player to add
     */
    public void addPlayer(Player p){
        if (players.size() >= 4) {
            System.out.println("Cannot add more players. Maximum is 4 players.");
            return;
        }
        players.add(p);
    }

    /**
     * Removes a player from the game.
     * 
     * @param p the player to remove
     */
    public void removePlayer(Player p){
        players.remove(p);
    }

    /**
     * Gets a player at the specified index.
     * 
     * @param index the position of the player in the players list
     * @return the player at the given index
     */
    public Player getPlayer(int index){
        return players.get(index);
    }

    /**
     * Starts the UNO game.
     * Validates player count (2-4 players required), deals 7 cards to each player,
     * places the first card on the discard pile, and begins the game loop.
     */
    public void startGame(){

        if (players.size() < 2) {
            System.out.println("Need at least 2 players to start the game.");
            return;
        }

        if (players.size() > 4) {
            System.out.println("Maximum 4 players allowed.");
            return;
        }

        for (Player player: players){
            player.getHand().StartCards(deck);
            System.out.println(player.getName() + " hand:");
            player.displayHand();

        }

        //
        top = deck.remove(0);
        System.out.println("Starting card: " + top);
        System.out.println("\nGame starting!\n");

        playGame();
    }

    // NOT COMPLETE
    /**
     * Main game loop that handles player turns.
     * Displays game state, shows current player's hand,
     * and manages turn progression.
     * This method contains the core game logic
     */
    private void playGame() {
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // Display game state
            System.out.println("===========================================");
            System.out.println(currentPlayer.getName() + "'s turn");
            System.out.println("Top card on discard pile: " + (topWild != null ? topWild : top));
            System.out.println("-------------------------------------------");
            
            // Show current player's hand with numbers
            System.out.println("Your cards:");
            Hand hand = currentPlayer.getHand();
            for (int i = 0; i < hand.getSize(); i++) {
                System.out.println(i + ": " + hand.getCard(i));
            }

            // More remaining
    }

    /**
     * Checks if a card can be legally played on the current top card.
     * A card is valid if:
     * - It's a wild card (can always be played)
     * - Its color matches the wild color (if a wild was played)
     * - Its color or value matches the top card
     * 
     * @param cardToPlay the card being checked for validity
     * @return true if the card can be played, false otherwise
     */
    public boolean isValidPlay(Card cardToPlay) {

        if (cardToPlay.getValue() == Card.Value.WILD || cardToPlay.getValue() == Card.Value.WILD_DRAW_TWO) {
            return true;
        }
        
        if (topWild != null) {
            return cardToPlay.getColor() == topWild;
        }
        
        if (top != null) {
            return cardToPlay.getColor() == top.getColor() || cardToPlay.getValue() == top.getValue();
        }
        
        return true;
    }

    /**
     * Executes the special action associated with an action card.
     * Handles: SKIP, WILD, WILD_DRAW_TWO, DRAW_ONE, and REVERSE cards.
     * Updates game state and advances to the next player as appropriate.
     * 
     * @param card the action card whose effect should be applied
     */
    public void handleActionCard(Card card){
        if(card.isActionCard()) {
            switch (card.getValue()) {
                case SKIP:
                    currentPlayerIndex = nextPlayer(nextPlayer(currentPlayerIndex));
                    System.out.println("Skipping player. Next Turn: " + players.get(currentPlayerIndex).getName());
                    break;

                case WILD:
                    topWild = askColorSwitch();
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Wild has been played, color is set to " + topWild + ". Next Turn: "  + players.get(currentPlayerIndex).getName());
                    break;

                case WILD_DRAW_TWO:
                    topWild = askColorSwitch();
                    drawCards(nextPlayer(currentPlayerIndex), 2);
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Wild +2 has been played, color is set to " + topWild + ". " + players.get(currentPlayerIndex).getName() + " Drew 2 card, it is now their turn.");
                    break;

                case DRAW_ONE:
                    drawCards(nextPlayer(currentPlayerIndex), 1);
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println(players.get(currentPlayerIndex).getName() + " Drew 1 card, it is now their turn.");
                    break;

                case REVERSE:
                    clockwise = !clockwise;
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Reversing direction. Next Turn: " + players.get(currentPlayerIndex).getName());
                    break;
            }
        }
    }

    //This class is desgined to return the next player
    /**
     * Calculates the index of the next player based on current direction.
     * Handles wrapping around the player list in both clockwise and 
     * counter-clockwise directions.
     * 
     * @param index the current player index
     * @return the index of the next player
     */
    private int nextPlayer(int index){
        if (clockwise) {
            return (index + 1) % players.size();
        } else {
            return (index - 1 + players.size()) % players.size();
        }
    }
    
    //Taking a card from the top of the deck and returning it
    /**
     * Draws a single card from the top of the deck.
     * 
     * @return the card drawn from the deck, or null if deck is empty
     */
    private Card drawCard(){
        if (deck.isEmpty()) {
            System.out.println("Deck is empty! (TODO: reshuffle from discard if you add one)");
            return null;
        }
        // draw from top of list; if you prefer, use remove(deck.size()-1)
        return deck.remove(0);
    }
    
    //Drawing a Card from deck and putting it in players hands
    /**
     * Makes a player draw multiple cards from the deck.
     * Cards are added directly to the specified player's hand.
     * 
     * @param index the index of the player who will draw cards
     * @param count the number of cards to draw
     */
    private void drawCards(int index, int count) {
        for (int i = 0; i < count; i++) {
            Card card = drawCard();
            if (card != null) {
                players.get(index).getHand().addCard(card);
            }
        }
    }

    //Getting user to pick next color
    /**
     * Prompts the current player to choose a color for a wild card.
     * Continues prompting until a valid color is entered.
     * Valid inputs: R (Red), G (Green), Y (Yellow), B (Blue)
     * 
     * @return the color chosen by the player
     */
    private Card.Color askColorSwitch(){
        while (true){
            System.out.print("Choose a color (R/G/Y/B:   ");
            Scanner scanner = new Scanner(System.in);
            String colorChose = scanner.nextLine();
            switch (colorChose){
                case "R": return Card.Color.RED;
                case "G": return Card.Color.GREEN;
                case "Y": return Card.Color.YELLOW;
                case "B": return Card.Color.BLUE;
                default:;
                    System.out.println("Invalid option");
            }
        }
    }
}