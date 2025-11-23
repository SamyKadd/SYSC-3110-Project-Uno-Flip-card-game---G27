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
    private ArrayList<Card> lightDeck;
    private ArrayList<Card> darkDeck;
    private int currentPlayerIndex;
    private boolean clockwise; //+1 forward and -1 reverse order
    private Card top; //The card thats on the top of the discard pile
    private Card.Color topWild = null; //If the top card on discard pile is wild card
    List<Card> discardedPile =  new ArrayList<>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private int pendingSkips = 0; // number of upcoming players to skip on the next "Next Player" click
    private Side currentSide = Side.LIGHT; //to track Light/Dark state
    private Card.Color darkWildColor = null;

    private static final Card.Color[] LIGHT_COLORS = {Card.Color.RED, Card.Color.BLUE, Card.Color.GREEN, Card.Color.YELLOW };
    private static final Card.Color[] DARK_COLORS = {Card.Color.PINK, Card.Color.PURPLE, Card.Color.TEAL, Card.Color.ORANGE };



    /**
     * Constructs a new Game instance.
     * Initializes an empty player list, creates and shuffles the deck,
     * and sets the game to start with the first player going clockwise.
     */
    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        lightDeck = new ArrayList<>();
        darkDeck = new ArrayList<>();
        discardedPile = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
//        input = new Scanner(System.in);
        initializeDeck();
    }

    /**
     * Creates all cards used on the light side of UNO Flip.
     * This includes:
     * - Number cards (0–9)
     * - Skip, Reverse, Draw One
     * - Wild and Wild Draw Two cards
     *
     * All cards are added to the lightDeck list.
     */
    private void buildLightDeck() {
        for (Card.Color color : LIGHT_COLORS) {
            // According to the UNO website, there is only one 0 card per color
            lightDeck.add(new Card(color, Card.Value.ZERO));

            // Every other number has two per color
            lightDeck.add(new Card(color, Card.Value.ONE));
            lightDeck.add(new Card(color, Card.Value.ONE));
            lightDeck.add(new Card(color, Card.Value.TWO));
            lightDeck.add(new Card(color, Card.Value.TWO));
            lightDeck.add(new Card(color, Card.Value.THREE));
            lightDeck.add(new Card(color, Card.Value.THREE));
            lightDeck.add(new Card(color, Card.Value.FOUR));
            lightDeck.add(new Card(color, Card.Value.FOUR));
            lightDeck.add(new Card(color, Card.Value.FIVE));
            lightDeck.add(new Card(color, Card.Value.FIVE));
            lightDeck.add(new Card(color, Card.Value.SIX));
            lightDeck.add(new Card(color, Card.Value.SIX));
            lightDeck.add(new Card(color, Card.Value.SEVEN));
            lightDeck.add(new Card(color, Card.Value.SEVEN));
            lightDeck.add(new Card(color, Card.Value.EIGHT));
            lightDeck.add(new Card(color, Card.Value.EIGHT));
            lightDeck.add(new Card(color, Card.Value.NINE));
            lightDeck.add(new Card(color, Card.Value.NINE));

            // Light side action cards (two of each per color)
            lightDeck.add(new Card(color, Card.Value.SKIP));
            lightDeck.add(new Card(color, Card.Value.SKIP));
            lightDeck.add(new Card(color, Card.Value.DRAW_ONE));
            lightDeck.add(new Card(color, Card.Value.DRAW_ONE));
            lightDeck.add(new Card(color, Card.Value.REVERSE));
            lightDeck.add(new Card(color, Card.Value.REVERSE));

        }
        // Light-Side Wild cards (4 of each type, no color)
        for (int i = 0; i < 4; i++) {
            lightDeck.add(new Card(null, Card.Value.WILD));
            lightDeck.add(new Card(null, Card.Value.WILD_DRAW_TWO));
        }
    }


    /**
     * Creates all cards used on the dark side of UNO Flip.
     * This includes:
     * - Draw Five
     * - Skip Everyone
     * - Wild Draw Color
     * All cards are added to the darkDeck list.
     */
    private void buildDarkDeck() {
        for  (Card.Color color : DARK_COLORS) {
            // Dark side special cards
            darkDeck.add(new Card(color, Card.Value.DRAW_FIVE));
            darkDeck.add(new Card(color, Card.Value.DRAW_FIVE));

            darkDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
            darkDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
        }
        // Dark-side Wild Draw Color (4 copies)
        for (int i = 0; i < 4; i++) {
            darkDeck.add(new Card(null, Card.Value.WILD_DRAW_COLOR));
        }
    }

    /**
     * Builds both the light and dark decks, then loads the light deck
     * as the starting draw deck. The deck is shuffled after loading.
     */
    private void initializeDeck() {
        buildLightDeck();
        buildDarkDeck();

        //start game using light deck
        deck.addAll(lightDeck);

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
     * Returns the current side of the game (LIGHT or DARK).
     * The game always begins on the LIGHT side, and switches when a FLIP card is played.
     *
     * @return the active Side of the game
     */
    public Side getCurrentSide() {
        return currentSide;
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
            if (cardToPlay == null) return false;

            if (getCurrentSide() == Side.LIGHT && (cardToPlay.getValue() == Card.Value.SKIP_EVERYONE || cardToPlay.getValue() == Card.Value.DRAW_FIVE || cardToPlay.getValue() == Card.Value.WILD_DRAW_COLOR)) {
                return false;
            }
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
         * Flips the current top card to its opposite-side version.
         * Light-side cards convert to their dark equivalents,
         * and dark-side cards convert to their light equivalents.
         */
        private void flipTopCard() {
            if (top == null) return;

            Card.Value v = top.getValue();

            // If top card is a light-side card, convert to dark equivalent
            if (currentSide == Side.DARK) {
                switch (v) {
                    case SKIP:          top = new Card(null, Card.Value.SKIP_EVERYONE); break;
                    case DRAW_ONE:      top = new Card(null, Card.Value.DRAW_FIVE); break;
                    case WILD:          top = new Card(null, Card.Value.WILD_DRAW_COLOR); break;
                    case WILD_DRAW_TWO: top = new Card(null, Card.Value.WILD_DRAW_COLOR); break;
                    // number cards simply "flip" by recolor
                    default: top = new Card(Card.Color.PINK, v); break;
                }
            }

            // If top card is a dark-side card, convert to light equivalent
            else {
                switch (v) {
                    case SKIP_EVERYONE: top = new Card(null, Card.Value.SKIP); break;
                    case DRAW_FIVE:     top = new Card(null, Card.Value.DRAW_ONE); break;
                    case WILD_DRAW_COLOR: top = new Card(null, Card.Value.WILD); break;
                    default: top = new Card(Card.Color.RED, v); break;
                }
            }
        }


        /**
         * Switches the active deck between the light deck and dark deck.
         * Moves all remaining cards from the current deck into their side’s deck,
         * and replaces the main deck with the opposite side’s cards.
         */
        private void switchDeck() {
            if (currentSide == Side.LIGHT) {
                // Move leftover cards to light deck
                lightDeck.clear();
                lightDeck.addAll(deck);

                // Switch to dark side deck
                deck.clear();
                deck.addAll(darkDeck);
            } else {
                // Move leftover cards to dark deck
                darkDeck.clear();
                darkDeck.addAll(deck);

                // Switch to light side deck
                deck.clear();
                deck.addAll(lightDeck);
            }

            // Shuffle new deck
            Collections.shuffle(deck);
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
                    case SKIP: {
                        // Do not advance now. Let the "Next Player" button apply the skip.
                        pendingSkips += 1;

                        GameState s = exportState();
                        s.statusMessage = "SKIP played. Next player will be skipped when you click Next Player.";
                        s.turnComplete = true; // current player’s action is done; must click Next
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case REVERSE: {
                        // Flip direction immediately, but don't advance. Next button will move according to new direction.
                        clockwise = !clockwise;

                        GameState s = exportState();
                        if (players.size() == 2) {
                            // In 2-player, Reverse acts like a Skip → same player goes again after Next
                            pendingSkips += 1;
                            s.statusMessage = "REVERSE played. (2 players) Acts like SKIP — same player after Next Player.";
                        } else {
                            s.statusMessage = "REVERSE played. Direction changed. Click Next Player to continue.";
                        }
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case WILD: {
                        // Choose color now; don't advance. Next button will move turn.
                        topWild = null;

                        GameState s = exportState();
                        s.needsWildColor = true;
                        s.statusMessage = "WILD played. Choose a color, then click Next Player.";
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case WILD_DRAW_TWO: {
                        topWild = null;

                        // Draw to the next player now, but don't advance. On Next Player, we skip that player (as per UNO).
                        int target = nextPlayer(currentPlayerIndex);
                        
                        for (int i = 0; i < 2; i++) {
                            Card drawnCard = drawCard();
                            if (drawnCard != null) {
                                players.get(target).getHand().addCard(drawnCard);
                            }
                        }

                        // After a +2, the target loses their turn, so schedule a skip for when Next is pressed.
                        pendingSkips += 1;

                        GameState s = exportState();
                        s.needsWildColor = true;
                        s.statusMessage = players.get(target).getName() + " draws 2. Click Next Player to continue (they will be skipped).";
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case DRAW_ONE: {
                        // Draw to the next player now, but don't advance. On Next Player, we skip that player.
                        int target = nextPlayer(currentPlayerIndex);
                        
                        Card drawnCard = drawCard();
                        if (drawnCard != null) {
                            players.get(target).getHand().addCard(drawnCard);
                        }

                        pendingSkips += 1;

                        GameState s = exportState();
                        s.statusMessage = players.get(target).getName() + " draws 1. Click Next Player to continue (they will be skipped).";
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case FLIP: {
                        if(currentSide == Side.LIGHT){
                            currentSide = Side.DARK;
                        }
                        else{
                            currentSide = Side.LIGHT;
                        }

                        switchDeck();
                        flipTopCard();

                        GameState s = exportState();
                        s.statusMessage = "Flipped to " + getCurrentSide();
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case DRAW_FIVE: {
                        // Next player draws 5 cards and loses their turn
                        int target = nextPlayer(currentPlayerIndex);
                        
                        for (int i = 0; i < 5; i++) {
                            Card drawnCard = drawCard();
                            if (drawnCard != null) {
                                players.get(target).getHand().addCard(drawnCard);
                            }
                        }

                        pendingSkips += 1;

                        GameState s = exportState();
                        s.statusMessage = players.get(target).getName() + " draws 5. Click Next Player to continue (they will be skipped).";
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }

                    case SKIP_EVERYONE: {
                        // Skip all other players - current player plays again
                        pendingSkips = players.size() - 1;
                        
                        GameState s = exportState();
                        s.statusMessage = "SKIP EVERYONE played! " + getCurrentPlayer().getName() + " plays again!";
                        s.turnComplete = true;
                        pcs.firePropertyChange("state", null, s);
                        return;
                    }
                    case WILD_DRAW_COLOR: {
                        // PHASE 1 — prompt user for color, do NOT draw cards yet
                        darkWildColor = null; // IMPORTANT: reset any previous color

                        GameState s = exportState();
                        s.needsDarkWildColor = true;  // tells GameView to open dark color dialog
                        s.statusMessage = "WILD DRAW COLOUR played! Choose a DARK color.";
                        s.turnComplete = true;

                        pcs.firePropertyChange("state", null, s);
                        return;
                    }


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



        /**
         * Calculates and awards points to the winning player.
         * Points are calculated based on cards remaining in other players' hands:
         * - Number cards: face value (0-9 points)
         * - Action cards: 20 points each
         * - Wild cards: 50 points each
         * 
         * @param winner the player who won the round
         */
        private void calculateAndAwardScore(Player winner) {
            int totalScore = 0;
            
            for (Player player : players) {
                if (player != winner) {
                    for (Card card : player.getHand().getCardsList()) {
                        if (card.getValue() == Card.Value.WILD || 
                            card.getValue() == Card.Value.WILD_DRAW_TWO) {
                            totalScore += 50;
                        } else if (card.getValue() == Card.Value.SKIP_EVERYONE) {
                            totalScore += 30;  
                        } else if (card.isActionCard()) {
                            totalScore += 20;
                        } else {
                            // Number cards - use their face value 
                            totalScore += card.getValue().ordinal();
                        }
                    }
                }
            }
            
            winner.addScore(totalScore);
            
            // Update the state to show scores
            GameState s = exportState();
            s.statusMessage = winner.getName() + " wins and scores " + totalScore + " points!";
            pcs.firePropertyChange("state", null, s);
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

            // Wild colors
            s.wildColor = topWild;
            s.darkWildColor = this.darkWildColor; // send chosen dark color to UI

            // UI flags
            s.canDraw = true;
            s.canPlay = true;
            s.canNext = true;

            s.needsWildColor = false;
            s.needsDarkWildColor = false;

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
            // Apply any pending skip(s) when the user presses "Next Player"
            if (pendingSkips > 0) {
                // skip exactly one player per click (the "skipped" one),
                // and land on the following player
                currentPlayerIndex = nextPlayer(nextPlayer(currentPlayerIndex));
                pendingSkips -= 1;
            } else {
                // normal one-step advance in current direction
                currentPlayerIndex = nextPlayer(currentPlayerIndex);
            }

            GameState s = exportState();
            s.statusMessage = getCurrentPlayer().getName() + "'s turn!";
            // turnComplete is false here; it's a fresh turn
            pcs.firePropertyChange("state", null, s);
        }


    public void setTopWildColor(Card.Color color) {
        this.topWild = color;
        GameState s = exportState();
        s.statusMessage = "Wild color set to " + color + ". Click Next Player to continue.";
        s.needsWildColor = false; // Without this, playing a wildcard will lock the color to "wildcard" permanently
        pcs.firePropertyChange("state", null, s);
    }

    /**
     * Sets the chosen dark wild color and applies the second phase of the
     * WILD DRAW COLOR effect. After the player selects a color, the next
     * player draws cards until they draw a card matching that chosen color.
     * The targeted player also loses their next turn.
     *
     * @param color the dark wild color selected by the player
     */
    public void setDarkWildColor(Card.Color color) {
        if (color == null) return;

        this.darkWildColor = color;
        this.topWild = color; // top card now behaves like a wild with chosen color

        int target = nextPlayer(currentPlayerIndex);
        Card drawn;

        // PHASE 2 — draw until matching the chosen dark color
        do {
            drawn = drawCard();
            if (drawn != null) {
                players.get(target).getHand().addCard(drawn);
            }
        } while (drawn != null && drawn.getColor() != darkWildColor);

        // Target loses their next turn
        pendingSkips += 1;

        GameState s = exportState();
        s.statusMessage = players.get(target).getName()
                + " draws until they get " + darkWildColor + "! Click Next Player.";
        s.needsDarkWildColor = false;
        s.turnComplete = true;

        pcs.firePropertyChange("state", null, s);
    }




    public void playCardFromHand(int handIndex) {
        Player cur = getCurrentPlayer();
        Card played = cur.getHand().removeCard(handIndex);
        if (played == null || !isValidPlay(played)) return;
        top = played;
        discardedPile.add(played);

        if (played.getValue() != Card.Value.WILD && played.getValue() != Card.Value.WILD_DRAW_TWO) {
            topWild = null; // Clear wild color for non-wild cards
        }

        GameState s = exportState();
        s.turnComplete = true; // player finished their turn
        pcs.firePropertyChange("state", null, s);

        if (cur.getHand().getSize() == 0) {
            // Calculate and award scores before declaring winner
            calculateAndAwardScore(cur);

            // Update scoreboard in the view
            s = exportState();
            s.statusMessage = cur.getName() + " wins! Final Score: " + cur.getScore();
            pcs.firePropertyChange("state", null, s);
            return;
        }

        handleActionCard(played);
    }


}