import java.io.*;
import java.util.*;
import java.util.List;
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
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Player> players;

    private ArrayList<Card>deck;
    private ArrayList<Card> lightDeck;
    private ArrayList<Card> darkDeck;

    private int currentPlayerIndex;
    private boolean clockwise; //+1 forward and -1 reverse order

    private Card top; //The card thats on the top of the discard pile
    private Card.Color topWild = null; //If the top card on discard pile is wild card

    private List<Card> lightDiscard = new ArrayList<>();
    private List<Card> darkDiscard = new ArrayList<>();

    private transient PropertyChangeSupport pcs;

    private int pendingSkips = 0; // number of upcoming players to skip on the next "Next Player" click

    private Side currentSide = Side.LIGHT; //to track Light/Dark state
    private Card.Color darkWildColor = null;

    private static final Card.Color[] LIGHT_COLORS = {Card.Color.RED, Card.Color.BLUE, Card.Color.GREEN, Card.Color.YELLOW };
    private static final Card.Color[] DARK_COLORS = {Card.Color.PINK, Card.Color.PURPLE, Card.Color.TEAL, Card.Color.ORANGE };

    private transient List<GameViewInterface> views;
    private transient Integer skipEveryoneFinalPlayer = null;

    private int currentRound = 1;
    private static final int WINNING_SCORE = 500;

    // --- Undo/Redo Stacks ---
    private transient ArrayList<GameMemento> undoStack = new ArrayList<>(); //stores past game states
    private transient ArrayList<GameMemento> redoStack = new ArrayList<>(); //rewinds state to a saved snapshot

    /**
     * making sure stack is not null
     */
    private void checkStack(){
        if (undoStack ==null){
            undoStack = new ArrayList<>();
        }
        if (redoStack ==null){
            redoStack = new ArrayList<>();
        }
    }

    /**
     * capturing the current state
     * allows us to go back for undo
     */
    public void saveState(){
        checkStack();;
        GameMemento snapshot = createMemento();
        undoStack.add(snapshot);
        redoStack.clear();
    }

    /**
     * Checks to see if we can undo
     * If we can, true is return
     */
    public boolean canUndo(){
        checkStack();
        return !undoStack.isEmpty();
    }

    /**
     * Checks to see if we can redo
     * If we can, true is return
     */
    public boolean canRedo(){
        checkStack();
        return !redoStack.isEmpty();
    }

    /**
     * We save state to undo
     * Restore to last state
     * Notify a change
     */
    public void undo(){
       checkStack();
        if (!canUndo()){
            return;
        }

        GameMemento current = createMemento();
        redoStack.add(current);
        GameMemento previous = undoStack.remove(undoStack.size() - 1);
        restoreFromMemento(previous);
        notifyStateChanged();
    }

    /**
     * We save state to redo
     * Restore to last state
     * Notify a change
     */
    public void redo(){
        checkStack();
        if (!canRedo()){
            return;
        }

        GameMemento current = createMemento();
        undoStack.add(current);
        GameMemento previous = redoStack.remove(redoStack.size() - 1);
        restoreFromMemento(previous);
        notifyStateChanged();
    }

    /**
     * Build a new Memento
     */
    private GameMemento createMemento(){
        List<Card> activeDiscard = (currentSide == Side.LIGHT) ? lightDiscard : darkDiscard;
        boolean reverseDirection = !clockwise;
        return new GameMemento(players, currentPlayerIndex, new ArrayList<>(deck), new ArrayList<>(activeDiscard), topWild, darkWildColor, reverseDirection);
    }

    /**
     * Putting the game back into the state thats stored
     * Get players, then direction, the restore the draw pile, update discard pile card, restore wild cards
     */
    private void restoreFromMemento(GameMemento m){
        this.players = new ArrayList<>(m.getPlayersSnapshot());
        this.currentPlayerIndex = m.getCurrentPlayerIndex();

        this.clockwise = !m.isReverseDirection();

        List<Card> snapshotDeck = m.getDrawPileSnapshot();
        if (currentSide == Side.LIGHT) {
            lightDeck.clear();
            lightDeck.addAll(snapshotDeck);
            deck = lightDeck;
        } else {
            darkDeck.clear();
            darkDeck.addAll(snapshotDeck);
            deck = darkDeck;
        }

        List<Card> snapshotDiscard = m.getDiscardPileSnapshot();
        List<Card> activeDiscard = (currentSide == Side.LIGHT) ? lightDiscard : darkDiscard;
        activeDiscard.clear();
        activeDiscard.addAll(snapshotDiscard);

        if (!activeDiscard.isEmpty()) {
            top = activeDiscard.get(activeDiscard.size() - 1);
        } else {
            top = null;
        }

        this.topWild = m.getWildColor();
        this.darkWildColor = m.getDarkWildColor();
    }



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
        currentPlayerIndex = 0;
        clockwise = true;
//        input = new Scanner(System.in);

        // Initialize transient fields
        initializeTransientFields();

        initializeDeck();
    }

    /**
     * Initializes transient fields. Called from constructor and after deserialization.
     */
    private void initializeTransientFields() {
        this.pcs = new PropertyChangeSupport(this);
        this.views = new ArrayList<>();
        this.undoStack = new ArrayList<>();
        this.redoStack = new ArrayList<>();
    }

    /**
     *
     */
    public void addView(GameViewInterface view) {
        if (views == null) {
            views = new ArrayList<>();
        }
        if (!views.contains(view)) {
            views.add(view);
        }
    }

    /**
     *
     */
    public void removeView(GameViewInterface view) {
        views.remove(view);
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

            // Draw Five (two per color)
            lightDeck.add(new Card(color, Card.Value.DRAW_FIVE));
            lightDeck.add(new Card(color, Card.Value.DRAW_FIVE));
            
            // Skip Everyone (two per color)
            lightDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
            lightDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
            
            // FLIP card (one per color)
            lightDeck.add(new Card(color, Card.Value.FLIP));

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
            // Dark side has numbers 1-9 (no zero)
            // Two of each number per color
            darkDeck.add(new Card(color, Card.Value.ONE));
            darkDeck.add(new Card(color, Card.Value.ONE));
            darkDeck.add(new Card(color, Card.Value.TWO));
            darkDeck.add(new Card(color, Card.Value.TWO));
            darkDeck.add(new Card(color, Card.Value.THREE));
            darkDeck.add(new Card(color, Card.Value.THREE));
            darkDeck.add(new Card(color, Card.Value.FOUR));
            darkDeck.add(new Card(color, Card.Value.FOUR));
            darkDeck.add(new Card(color, Card.Value.FIVE));
            darkDeck.add(new Card(color, Card.Value.FIVE));
            darkDeck.add(new Card(color, Card.Value.SIX));
            darkDeck.add(new Card(color, Card.Value.SIX));
            darkDeck.add(new Card(color, Card.Value.SEVEN));
            darkDeck.add(new Card(color, Card.Value.SEVEN));
            darkDeck.add(new Card(color, Card.Value.EIGHT));
            darkDeck.add(new Card(color, Card.Value.EIGHT));
            darkDeck.add(new Card(color, Card.Value.NINE));
            darkDeck.add(new Card(color, Card.Value.NINE));
            
            // Dark side action cards (two of each per color)
            darkDeck.add(new Card(color, Card.Value.DRAW_FIVE));
            darkDeck.add(new Card(color, Card.Value.DRAW_FIVE));
            
            darkDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
            darkDeck.add(new Card(color, Card.Value.SKIP_EVERYONE));
            
            // REVERSE exists on both light and dark sides
            darkDeck.add(new Card(color, Card.Value.REVERSE));
            darkDeck.add(new Card(color, Card.Value.REVERSE));
            
            // FLIP card (one per color)
            darkDeck.add(new Card(color, Card.Value.FLIP));
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
        deck = lightDeck;

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

        lightDiscard.add(top);

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

    // Need to return and add JavaDcos later 
    public int getCurrentRound() {
        return currentRound;
    }

    public int getWinningScore() {
        return WINNING_SCORE;
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

        // Side-specific restrictions
        if (getCurrentSide() == Side.LIGHT) {
            // WILD_DRAW_COLOR is dark-side only
            if (cardToPlay.getValue() == Card.Value.WILD_DRAW_COLOR) {
                return false;
            }
            // Dark colors not allowed on light side
            if (cardToPlay.getColor() == Card.Color.TEAL || 
                cardToPlay.getColor() == Card.Color.PURPLE ||
                cardToPlay.getColor() == Card.Color.PINK || 
                cardToPlay.getColor() == Card.Color.ORANGE) {
                return false;
            }
        } else { // DARK side
            // WILD and WILD_DRAW_TWO are light-side only
            if (cardToPlay.getValue() == Card.Value.WILD || 
                cardToPlay.getValue() == Card.Value.WILD_DRAW_TWO) {
                return false;
            }
            // Light colors not allowed on dark side
            if (cardToPlay.getColor() == Card.Color.RED || 
                cardToPlay.getColor() == Card.Color.BLUE ||
                cardToPlay.getColor() == Card.Color.GREEN || 
                cardToPlay.getColor() == Card.Color.YELLOW) {
                return false;
            }
        }
        
        // Wild cards (appropriate for current side) can always be played
        if ((getCurrentSide() == Side.LIGHT && 
            (cardToPlay.getValue() == Card.Value.WILD || 
            cardToPlay.getValue() == Card.Value.WILD_DRAW_TWO)) ||
            (getCurrentSide() == Side.DARK && 
            cardToPlay.getValue() == Card.Value.WILD_DRAW_COLOR)) {
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
        // Flip the side
        currentSide = (currentSide == Side.LIGHT) ? Side.DARK : Side.LIGHT;

        // Point deck to the correct list
        deck = (currentSide == Side.LIGHT) ? lightDeck : darkDeck;

        // Shuffle new active deck
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

                    GameStateEvent s = exportState();
                    s.setStatusMessage("SKIP played. Next player will be skipped when you click Next Player.");
                    s.setTurnComplete(true); // current player’s action is done; must click Next
                    pcs.firePropertyChange("state", null, s);
                    return;
                }

                case REVERSE: {
                    // Flip direction immediately, but don't advance. Next button will move according to new direction.
                    clockwise = !clockwise;

                    GameStateEvent s = exportState();
                    if (players.size() == 2) {
                        // In 2-player, Reverse acts like a Skip → same player goes again after Next
                        pendingSkips += 1;
                        s.setStatusMessage("REVERSE played. (2 players) Acts like SKIP — same player after Next Player.");
                    } else {
                        s.setStatusMessage("REVERSE played. Direction changed. Click Next Player to continue.");
                    }
                    s.setTurnComplete(true);
                    pcs.firePropertyChange("state", null, s);
                    return;
                }

                case WILD: {
                    // Choose color now; don't advance. Next button will move turn.
                    topWild = null;

                    GameStateEvent s = exportState();
                    s.setNeedsWildColor(true);
                    s.setStatusMessage("WILD played. Choose a color, then click Next Player.");
                    s.setTurnComplete(true);
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

                    GameStateEvent s = exportState();
                    s.setNeedsWildColor(true);
                    s.setStatusMessage(players.get(target).getName() + " draws 2. Click Next Player to continue (they will be skipped).");
                    s.setTurnComplete(true);
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

                    GameStateEvent s = exportState();
                    s.setStatusMessage(players.get(target).getName() + " draws 1. Click Next Player to continue (they will be skipped).");
                    s.setTurnComplete(true);
                    pcs.firePropertyChange("state", null, s);
                    return;
                }

                case FLIP: {
                    pendingSkips = 0;

                    switchDeck();
                    
                    flipAllPlayerHands();
                    
                    flipTopCard();

                    GameStateEvent s = exportState();
                    s.setStatusMessage("Flipped to " + getCurrentSide());
                    s.setTurnComplete(true);
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

                    GameStateEvent s = exportState();
                    s.setStatusMessage(players.get(target).getName() + " draws 5. Click Next Player to continue (they will be skipped).");
                    s.setTurnComplete(true);
                    pcs.firePropertyChange("state", null, s);
                    return;
                }

                case SKIP_EVERYONE: {
                    // The test expects: skip every OTHER player once, then land on next player
                    pendingSkips = players.size() - 1;

                    // Store the player who should get the turn AFTER skipping all others
                    int finalTarget = currentPlayerIndex;
                    for (int i = 0; i < pendingSkips; i++) {
                        finalTarget = nextPlayer(finalTarget);
                    }

                    // After the skips are processed in advanceTurn(), force correct landing spot
                    GameStateEvent s = exportState();
                    s.setStatusMessage("SKIP EVERYONE played!");
                    s.setTurnComplete(false);

                    // Save the final target for advanceTurn to use
                    this.skipEveryoneFinalPlayer = finalTarget;

                    pcs.firePropertyChange("state", null, s);
                    return;
                }

                case WILD_DRAW_COLOR: {
                    // PHASE 1 — prompt user for color, do NOT draw cards yet
                    darkWildColor = null; // IMPORTANT: reset any previous color

                    GameStateEvent s = exportState();
                    s.setNeedsDarkWildColor(true);  // tells GameView to open dark color dialog
                    s.setStatusMessage("WILD DRAW COLOUR played! Choose a DARK color.");
                    s.setTurnComplete(true);

                    pcs.firePropertyChange("state", null, s);
                    return;
                }


            }
        }
        notifyStateChanged();
    }

    /**
     * Returns the opposite-side equivalent of a card.
     * Light cards convert to dark, dark cards convert to light.
     * 
     * @param card the card to flip
     * @return the flipped version of the card
     */
    private Card flipCard(Card card) {
        if (card == null) return null;
        
        Card.Value v = card.getValue();
        Card.Color c = card.getColor();
        
        // If currently on DARK side, we're flipping FROM light TO dark
        if (currentSide == Side.DARK) {
            // Convert light colors to dark colors
            Card.Color newColor = null;
            if (c != null) {
                switch (c) {
                    case RED:    newColor = Card.Color.PINK; break;
                    case BLUE:   newColor = Card.Color.PURPLE; break;
                    case GREEN:  newColor = Card.Color.TEAL; break;
                    case YELLOW: newColor = Card.Color.ORANGE; break;
                    default:     newColor = c; // Already dark
                }
            }
            
            // Convert light action cards to dark action cards
            Card.Value newValue = v;
            switch (v) {
                case SKIP:          newValue = Card.Value.SKIP_EVERYONE; break;
                case DRAW_ONE:      newValue = Card.Value.DRAW_FIVE; break;
                case WILD:          newValue = Card.Value.WILD_DRAW_COLOR; break;
                case WILD_DRAW_TWO: newValue = Card.Value.WILD_DRAW_COLOR; break;
                case REVERSE:       newValue = Card.Value.REVERSE; break; // Same on both
                case FLIP:          newValue = Card.Value.FLIP; break; // Same on both
                default:            newValue = v; // Numbers stay same
            }
            
            return new Card(newColor, newValue);
        }
        
        // If currently on LIGHT side, we're flipping FROM dark TO light
        else {
            // Convert dark colors to light colors
            Card.Color newColor = null;
            if (c != null) {
                switch (c) {
                    case PINK:   newColor = Card.Color.RED; break;
                    case PURPLE: newColor = Card.Color.BLUE; break;
                    case TEAL:   newColor = Card.Color.GREEN; break;
                    case ORANGE: newColor = Card.Color.YELLOW; break;
                    default:     newColor = c; // Already light
                }
            }
            
            // Convert dark action cards to light action cards
            Card.Value newValue = v;
            switch (v) {
                case SKIP_EVERYONE:   newValue = Card.Value.SKIP; break;
                case DRAW_FIVE:       newValue = Card.Value.DRAW_ONE; break;
                case WILD_DRAW_COLOR: newValue = Card.Value.WILD; break;
                case REVERSE:         newValue = Card.Value.REVERSE; break; // Same
                case FLIP:            newValue = Card.Value.FLIP; break; // Same
                default:              newValue = v; // Numbers stay same
            }
            
            return new Card(newColor, newValue);
        }
    }

    /**
     * Flips all cards in all players' hands to their opposite-side versions.
     * Called when a FLIP card is played.
     */
    private void flipAllPlayerHands() {
        for (Player player : players) {
            Hand hand = player.getHand();
            List<Card> oldCards = new ArrayList<>(hand.getCardsList());
            
            // Clear the hand
            hand.getCardsList().clear();
            
            // Add flipped versions of each card
            for (Card oldCard : oldCards) {
                Card flippedCard = flipCard(oldCard);
                hand.addCard(flippedCard);
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

    private void reshuffleFromDiscard() {

        List<Card> discard = (currentSide == Side.LIGHT) ? lightDiscard : darkDiscard;

        if (discard.size() > 1) {

            Card lastTop = discard.remove(discard.size() - 1); // keep top card

            deck.addAll(discard);   // return all other cards to deck
            discard.clear();
            discard.add(lastTop);   // put top card back

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
     * Returns the score value of a card according to UNO Flip rules.
     * Scoring is based on the card's value.
     *
     * - Number cards: face value (0-9 points)
     * - Action cards: 10-30 points each
     * - Wild cards: 40-60 points each
     *
     * @param card the card in which assigning points to
     */
    private int getCardScore(Card card) {
        Card.Value v = card.getValue();

        switch (v) {
            //number cards: face value
            case ZERO:  return 0;
            case ONE:   return 1;
            case TWO:   return 2;
            case THREE: return 3;
            case FOUR:  return 4;
            case FIVE:  return 5;
            case SIX:   return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE:  return 9;

            //light-side action card
            case DRAW_ONE:
                return 10;

            //20-point actions
            case DRAW_FIVE:   // dark
            case SKIP:        // light
            case REVERSE:     // light/dark
            case FLIP:        // light/dark
                return 20;

            //special dark action
            case SKIP_EVERYONE:
                return 30;

            //wild cards
            case WILD:
                return 40;
            case WILD_DRAW_TWO:
                return 50;
            case WILD_DRAW_COLOR:
                return 60;

            default:
                return 0;
        }
    }

    /**
     * Calculates and awards points to the winning player.
     * Points are calculated based on cards remaining in other players' hands:
     *
     * @param winner the player who won the round
     */
    private void calculateAndAwardScore(Player winner) {
        int totalScore = 0;

        for (Player player : players) {
            if (player != winner) {
                for (Card card : player.getHand().getCardsList()) {
                    totalScore += getCardScore(card);
                }
            }
        }

        winner.addScore(totalScore);

        GameStateEvent s = exportState();
        s.setStatusMessage(winner.getName() + " wins and scores " + totalScore + " points!");
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
    public GameStateEvent exportState() {
        Player cur = getCurrentPlayer();
        GameStateEvent s = new GameStateEvent(this, cur.getName(), cur.getHand().getCardsList(), getTopCard());

        s.setWildColor(topWild);
        s.setDarkWildColor(this.darkWildColor); // send chosen dark color to UI

        s.setCanDraw(true);
        s.setCanPlay(true);
        s.setCanNext(true);

        s.setNeedsWildColor(false);
        s.setNeedsDarkWildColor(false);

        s.setStatusMessage("");

        return s;
    }

    private void notifyStateChanged() {
        GameStateEvent state = exportState();
        pcs.firePropertyChange("state", null, state);

        for (GameViewInterface view : views) {
            view.render(state);
        }
    }
    /**
     * Makes the current player draw one card and updates state.
     *
     * @return
     */
    public boolean drawCardForCurrentPlayer() {
        Player cur = getCurrentPlayer();
        Card card = drawCard();
        if (card == null) return false;
        cur.getHand().addCard(card);
        notifyStateChanged();
        return true;
    }

    /**
     * Advances to the next player's turn and updates state.
     */
    public void advanceTurn() {
        if (skipEveryoneFinalPlayer != null) {
            currentPlayerIndex = skipEveryoneFinalPlayer;
            skipEveryoneFinalPlayer = null;
            pendingSkips = 0;

            GameStateEvent s = exportState();
            s.setStatusMessage(getCurrentPlayer().getName() + "'s turn!");
            pcs.firePropertyChange("state", null, s);
            return;
        }
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

        GameStateEvent s = exportState();
        s.setStatusMessage(getCurrentPlayer().getName() + "'s turn!");
        // turnComplete is false here; it's a fresh turn
        pcs.firePropertyChange("state", null, s);
    }

    /**
     * Starts a new round while keeping player scores and order.
     * Called after a player wins a round but hasn't reached 500 points yet.
     */
    public void startNewRound() {
        currentRound++;
        
        // Clear all hands
        for (Player p : players) {
            p.getHand().getCardsList().clear();
        }
        
        // Reset game state
        currentSide = Side.LIGHT;
        deck = lightDeck;
        lightDiscard.clear();
        darkDiscard.clear();
        pendingSkips = 0;
        skipEveryoneFinalPlayer = null;
        topWild = null;
        darkWildColor = null;
        clockwise = true;
        currentPlayerIndex = 0;
        
        // Rebuild and shuffle decks
        lightDeck.clear();
        darkDeck.clear();
        buildLightDeck();
        buildDarkDeck();
        deck = lightDeck;
        Collections.shuffle(deck);
        
        // Deal new hands
        for (Player player : players) {
            player.getHand().startCards(deck);
        }
        
        // Pick new top card (not an action card)
        do {
            top = deck.remove(0);
            if (top.isActionCard()) {
                deck.add(top);
            }
        } while (top.isActionCard());
        
        lightDiscard.add(top);
        
        // Notify observers
        GameStateEvent s = exportState();
        s.setStatusMessage("Round " + currentRound + " begins! " + 
            getCurrentPlayer().getName() + "'s turn.");
        pcs.firePropertyChange("state", null, s);
    }

    /**
     * Starts a completely new game, resetting all scores and round count.
     * Called after a player reaches 500 points.
     */
    public void startNewGame() {
        currentRound = 1;
        
        // Reset ALL scores
        for (Player p : players) {
            p.setScore(0);
            p.getHand().getCardsList().clear();
        }
        
        // Reset game state (same as startNewRound)
        currentSide = Side.LIGHT;
        deck = lightDeck;
        lightDiscard.clear();
        darkDiscard.clear();
        pendingSkips = 0;
        skipEveryoneFinalPlayer = null;
        topWild = null;
        darkWildColor = null;
        clockwise = true;
        currentPlayerIndex = 0;
        
        // Rebuild and shuffle decks
        lightDeck.clear();
        darkDeck.clear();
        buildLightDeck();
        buildDarkDeck();
        deck = lightDeck;
        Collections.shuffle(deck);
        
        // Deal new hands
        for (Player player : players) {
            player.getHand().startCards(deck);
        }
        
        // Pick new top card
        do {
            top = deck.remove(0);
            if (top.isActionCard()) {
                deck.add(top);
            }
        } while (top.isActionCard());
        
        lightDiscard.add(top);
        
        // Notify observers
        GameStateEvent s = exportState();
        s.setStatusMessage("NEW GAME! Round 1 begins! " + 
            getCurrentPlayer().getName() + "'s turn.");
        pcs.firePropertyChange("state", null, s);
    }


    public void setTopWildColor(Card.Color color) {
        this.topWild = color;
        GameStateEvent s = exportState();
        s.setStatusMessage("Wild color set to " + color + ". Click Next Player to continue.");
        s.setNeedsWildColor(false); // Without this, playing a wildcard will lock the color to "wildcard" permanently
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

        GameStateEvent s = exportState();
        s.setStatusMessage(players.get(target).getName()
                + " draws until they get " + darkWildColor + "! Click Next Player.");
        s.setNeedsDarkWildColor(false);
        s.setTurnComplete(true);

        pcs.firePropertyChange("state", null, s);
    }


    public boolean playCardFromHand(int handIndex) {
        Player cur = getCurrentPlayer();
        Card played = cur.getHand().removeCard(handIndex);

        // Invalid play
        if (played == null || !isValidPlay(played)) return false;

        // Set new top card
        top = played;
        if (currentSide == Side.LIGHT) {
            lightDiscard.add(played);
        } else {
            darkDiscard.add(played);
        }

        // Clear wild color unless this is a wild card
        if (played.getValue() != Card.Value.WILD && played.getValue() != Card.Value.WILD_DRAW_TWO) {
            topWild = null;
        }

        // ---- WIN CONDITION CHECK BEFORE ACTION CARD ----
        if (cur.getHand().getSize() == 0) {
            calculateAndAwardScore(cur);
            
            // Check if anyone reached 500 points (game over)
            Player gameWinner = null;
            for (Player p : players) {
                if (p.getScore() >= WINNING_SCORE) {
                    gameWinner = p;
                    break;
                }
            }
            
            if (gameWinner != null) {
                // GAME OVER - someone reached 500 points
                GameStateEvent winState = exportState();
                winState.setGameOver(true);
                winState.setStatusMessage(gameWinner.getName() + " WINS THE GAME with " + 
                    gameWinner.getScore() + " points! Click 'New Game' to play again.");
                pcs.firePropertyChange("state", null, winState);
            } else {
                // ROUND OVER - start new round
                GameStateEvent roundState = exportState();
                roundState.setStatusMessage(cur.getName() + " wins round " + currentRound + 
                    " and scores " + cur.getScore() + " points! Click 'New Round' to continue.");
                pcs.firePropertyChange("state", null, roundState);
            }
            
            return true;
        }

        // ---- APPLY ACTION CARD EFFECT FIRST (correct order!) ----
        // If this card has a special effect, handle it now (skip, reverse, flip, draw, etc.)
        if (played.isActionCard()) {
            handleActionCard(played);
            return true; // action cards already fired turnComplete + events
        }

        // ---- REGULAR CARD PLAY: now update state ----
        GameStateEvent state = exportState();
        state.setTurnComplete(true); // player finished their turn
        pcs.firePropertyChange("state", null, state);

        return true;
    }

    /**
     * Saves the current game state to a file using Java serialization.
     * 
     * @param filename the file to save to
     * @return true if save successful, false otherwise
     */
    public boolean saveGame(String filename) {
        FileOutputStream ostream = null;
        ObjectOutputStream p = null;
        
        try {
            // Create output streams
            ostream = new FileOutputStream(filename);
            p = new ObjectOutputStream(ostream);
            
            // Write the entire game object
            p.writeObject(this);
            
            System.out.println("Game saved successfully to " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } finally {
            // Always close streams 
            try {
                if (p != null) p.close();
                if (ostream != null) ostream.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }

    /**
     * Loads a game state from a file using Java deserialization.
     * 
     * @param filename the file to load from
     * @return the loaded Game object, or null if loading failed
     */
    public static Game loadGame(String filename) {
        FileInputStream istream = null;
        ObjectInputStream p = null;
        
        try {
            // Create input streams
            istream = new FileInputStream(filename);
            p = new ObjectInputStream(istream);
            
            // Read the game object
            Game game = (Game) p.readObject();
            
            // Re-initialize transient fields (they weren't serialized)
            game.pcs = new PropertyChangeSupport(game);
            game.views = new ArrayList<>();

            // RECREATE UNDO/REDO STACKS
            game.undoStack = new ArrayList<>();
            game.redoStack = new ArrayList<>();

            // DECK POINTER MUST BE REASSIGNED
            game.deck = (game.currentSide == Side.LIGHT) ? game.lightDeck : game.darkDeck;

            // RESTORE DISCARD PILES CORRECTLY
            game.lightDiscard = new ArrayList<>(game.lightDiscard);
            game.darkDiscard  = new ArrayList<>(game.darkDiscard);

            // FIX TOP CARD (recalculate after restoring discard piles)
            if (game.currentSide == Side.LIGHT && !game.lightDiscard.isEmpty()) {
                game.top = game.lightDiscard.get(game.lightDiscard.size() - 1);
            }
            else if (game.currentSide == Side.DARK && !game.darkDiscard.isEmpty()) {
                game.top = game.darkDiscard.get(game.darkDiscard.size() - 1);
            }



            System.out.println("Game loaded successfully from " + filename);
            return game;
            
        } catch (FileNotFoundException e) {
            System.err.println("Save file not found: " + filename);
            return null;
            
        } catch (InvalidClassException e) {
            System.err.println("Save file is incompatible with this version of the game.");
            return null;
        }
        catch (StreamCorruptedException e) {
            System.err.println("Save file is corrupted or incomplete.");
            return null;
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            return null;
            
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid save file format: " + e.getMessage());
            return null;
            
        } finally {
            // Always close streams
            try {
                if (p != null) p.close();
                if (istream != null) istream.close();
            } catch (IOException e) {
                System.err.println("Error closing streams: " + e.getMessage());
            }
        }
    }

    /**
     * Re-initializes transient fields after deserialization.
     * Called automatically by Java during deserialization.
     * 
     * @param in the ObjectInputStream
     * @throws IOException if I/O error occurs
     * @throws ClassNotFoundException if class not found
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        // Perform default deserialization first
        in.defaultReadObject();

        // Re-initialize transient fields
        initializeTransientFields();
    }


}