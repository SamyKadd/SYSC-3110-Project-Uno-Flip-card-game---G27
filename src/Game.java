import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents the main UNO game logic and state.
 * This class manages players, the deck, turns, and game rules.
 * Supports 2-4 players and handles all UNO card actions.
 *
 * @author G27
 * @version 1.0
 */
public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;
    private int currentPlayerIndex;
    private boolean clockwise; //+1 forward and -1 reverse order
    private Card top; //The card thats on the top of the discard pile
    private Card.Color topWild = null; //If the top card on discard pile is wild card
    List<Card> discardedPile =  new ArrayList<>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    /**
     * Constructs a new Game instance.
     * Initializes an empty player list, creates and shuffles the deck,
     * and sets the game to start with the first player going clockwise.
     */
    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        discardedPile = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
//        input = new Scanner(System.in);
        initializeDeck();
    }

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
    public void startGame() {
        if (players.size() < 2 || players.size() > 4) {
            throw new IllegalStateException("Game must have 2–4 players before starting.");
        }

        // Deal 7 cards to each player
        for (Player player : players) {
            player.getHand().startCards(deck);
        }

        // Pick first top card that’s not an action
        do {
            top = deck.remove(0);
            if (top.isActionCard()) {
                deck.add(top);
            }
        } while (top.isActionCard());

        discardedPile.add(top);

        // Trigger initial state for GUI
        notifyStateChanged();
    }


    /**
     * Main game loop that handles player turns.
     * Displays game state, shows current player's hand,
     * and manages turn progression.
     * This method contains the core game logic
     */
//    private void playGame() {
//        while (true) {
//            Player currentPlayer = players.get(currentPlayerIndex);
//
//            // Display game state
//            System.out.println("===========================================");
//            System.out.println(currentPlayer.getName() + "'s turn");
//            System.out.println("Top card on discard pile: " + (topWild != null ? topWild : top));
//            System.out.println("-------------------------------------------");
//
//            // Show current player's hand with numbers
//            System.out.println("Your cards:");
//            Hand hand = currentPlayer.getHand();
//            for (int i = 0; i < hand.getSize(); i++) {
//                System.out.println(i + ": " + hand.getCard(i));
//            }
//
//            System.out.println("\nEnter card number to play, or 'D' to draw a card:");
//            String s = input.nextLine().trim();
//
//            if (s.equalsIgnoreCase("D")) {
//                Card drawnCard = drawCard();
//                if (drawnCard != null) {
//                    hand.addCard(drawnCard);
//                    System.out.println("You drew: " + drawnCard);
//                }
//                currentPlayerIndex = nextPlayer(currentPlayerIndex);
//                continue;
//            }
//
//            try {
//                int cardIndex = Integer.parseInt(s);
//
//                if (cardIndex < 0 || cardIndex >= hand.getSize()) {
//                    System.out.println("Invalid card number! Try again.");
//                    continue;
//                }
//
//                Card playedCard = hand.getCard(cardIndex);
//
//                if (!isValidPlay(playedCard)) {
//                    System.out.println("You cannot play this card! Card must match colour or value. Try again.");
//                    continue;
//                }
//
//                hand.removeCard(cardIndex);
//                top = playedCard;
//                discardedPile.add(playedCard);
//                topWild = null; // Reset wild color unless a wild is played
//
//                System.out.println("You played: " + playedCard);
//
//                // Check if player won
//                if (hand.getSize() == 0) {
//                    System.out.println("\n===========================================");
//                    System.out.println(currentPlayer.getName() + " wins!");
//                    System.out.println("===========================================");
//                    break;
//                }
//
//                if(playedCard.isActionCard()){
//                    handleActionCard(playedCard);
//                } else {
//                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
//                }
//
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input! Enter a number or 'D'.");
//            }
//        }
//    }

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
            if (cardToPlay == null) return false;
            // Wilds can always be played
            if (cardToPlay.getValue() == Card.Value.WILD || cardToPlay.getValue() == Card.Value.WILD_DRAW_TWO) {
                return true;
            }
            // If a wild color is active (a color chosen from a previous wild)
            if (topWild != null) {
                return cardToPlay.getColor() == topWild;
            }
            // If there is a top card on the discard pile
            if (top != null) {
                // Allow match by color OR value
                return (cardToPlay.getColor() == top.getColor()) ||
                        (cardToPlay.getValue() == top.getValue());
            }
            // Default true if no top card yet
            return true;
        }



        /**
         * Executes the special action associated with an action card.
         * Handles: SKIP, WILD, WILD_DRAW_TWO, DRAW_ONE, and REVERSE cards.
         * Updates game state and advances to the next player as appropriate.
         *
         * @param card the action card whose effect should be applied
         */
        public void handleActionCard(Card card) {
            if (card.isActionCard()) {
                switch (card.getValue()) {
                    case SKIP:
                        currentPlayerIndex = nextPlayer(nextPlayer(currentPlayerIndex));
                        break;

                    case WILD:
                        // tell the view to prompt for color
                        GameState s = exportState();
                        s.needsWildColor = true;
                        pcs.firePropertyChange("state", null, s);
                        return;

                    case WILD_DRAW_TWO:
                        drawCards(nextPlayer(currentPlayerIndex), 2);
                        topWild = null; // color will be set later by controller
                        currentPlayerIndex = nextPlayer(currentPlayerIndex);
                        break;

                    case DRAW_ONE:
                        drawCards(nextPlayer(currentPlayerIndex), 1);
                        currentPlayerIndex = nextPlayer(currentPlayerIndex);
                        break;

                    case REVERSE:
                        clockwise = !clockwise;
                        if (players.size() == 2) {
                            currentPlayerIndex = nextPlayer(nextPlayer(currentPlayerIndex));
                        } else {
                            currentPlayerIndex = nextPlayer(currentPlayerIndex);
                        }
                        break;
                }
            }
            notifyStateChanged();
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

        private void reshuffleFromDiscard() {
            if (discardedPile.size() > 1) {
                Card lastTop = discardedPile.remove(discardedPile.size() - 1);
                deck.addAll(discardedPile);
                discardedPile.clear();
                discardedPile.add(lastTop);
                Collections.shuffle(deck);
            }
        }

    //Taking a card from the top of the deck and returning it
        /**
         * Draws a single card from the top of the deck.
         *
         * @return the card drawn from the deck, or null if deck is empty
         */
        private Card drawCard() {
            if (deck.isEmpty()) {
                reshuffleFromDiscard();
            }
            if (deck.isEmpty()) return null; // still empty
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
            notifyStateChanged();
        }

        //Getting user to pick next color
        /**
         * Prompts the current player to choose a color for a wild card.
         * Continues prompting until a valid color is entered.
         * Valid inputs: R (Red), G (Green), Y (Yellow), B (Blue)
         *
         * @return the color chosen by the player
         */
//        private Card.Color askColorSwitch(){
//            while (true){
//                System.out.print("Choose a color (R/G/Y/B:   ");
//                String colorChose = input.nextLine();
//                switch (colorChose){
//                    case "R": return Card.Color.RED;
//                    case "G": return Card.Color.GREEN;
//                    case "Y": return Card.Color.YELLOW;
//                    case "B": return Card.Color.BLUE;
//                    default:;
//                        System.out.println("Invalid option");
//                }
//            }
//        }
        //need to add javadocs later
        public Player getCurrentPlayer() {
            return players.get(currentPlayerIndex);
        }
        public Card getTopCard() {
            return top;
        }
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
        public GameState exportState() {
            GameState s = new GameState();
            Player cur = getCurrentPlayer();
            s.curPlayerName = cur.getName();
            s.curHand = new ArrayList<>(cur.getHand().getCardsList());
            s.topCard = getTopCard();
            s.canDraw = true;
            s.canPlay = true;
            s.canNext = true;
            s.needsWildColor = false;
            return s;
        }

        private void notifyStateChanged() {
            pcs.firePropertyChange("state", null, exportState());
        }
        /**
         * Makes the current player draw one card and updates state.
         */
        public void drawCardForCurrentPlayer() {
            Player cur = getCurrentPlayer();
            Card card = drawCard();
            if (card != null) {
                cur.getHand().addCard(card);
            }
            notifyStateChanged();
        }

        /**
         * Advances to the next player's turn and updates state.
         */
        public void advanceTurn() {
            currentPlayerIndex = nextPlayer(currentPlayerIndex);
            notifyStateChanged();
        }
    public void setTopWildColor(Card.Color color) {
        this.topWild = color;
        notifyStateChanged();
    }
    public void playCardFromHand(int handIndex) {
        Player cur = getCurrentPlayer();
        Card played = cur.getHand().removeCard(handIndex);
        if (played == null || !isValidPlay(played)) return;
        top = played;
        discardedPile.add(played);

        GameState s = exportState();
        s.turnComplete = true; // player finished their turn
        pcs.firePropertyChange("state", null, s);

        if (cur.getHand().getSize() == 0) {
            s.statusMessage = cur.getName() + " wins!";
            pcs.firePropertyChange("state", null, s);
            return;
        }

        handleActionCard(played);
    }


}