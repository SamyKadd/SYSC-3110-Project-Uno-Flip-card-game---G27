/**
 * Controller for the UNO game (MVC architecture).
 * Listens for user input from the GameView and updates the Game model.
 * Observes model state changes and refreshes the view accordingly.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class GameController implements GameUIListener, ActionListener {
    private final Game model;
    private final GameViewInterface view;
    private boolean hasPlayedThisTurn = false;


    @Override
    public void actionPerformed(ActionEvent e) {
        // unused or route events here if desired
    }
    /**
     * Constructs a new GameController and connects the model and view
     *
     * @param model the game model that contains the logic and data
     * @param view the game view responsible for the GUI
     */
    public GameController(Game model, GameViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setListener(this);

        this.model.addPropertyChangeListener(new PropertyChangeListener() {
            private String lastPlayer = null;

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!"state".equals(evt.getPropertyName())) return;

                GameStateEvent newState = (GameStateEvent) evt.getNewValue();

                // Unlock only when the player changes or a 2-player reverse replay occurs
                boolean turnChanged = newState.getCurPlayerName() != null &&
                        !newState.getCurPlayerName().equals(lastPlayer);
                boolean twoPlayerReverseReplay = newState.getStatusMessage() != null &&
                        newState.getStatusMessage().contains("Same player goes again!");
                if (turnChanged || twoPlayerReverseReplay) {
                    hasPlayedThisTurn = false;
                }
                lastPlayer = newState.getCurPlayerName();

                // Always show the model’s status message
                if (newState.getStatusMessage() != null && !newState.getStatusMessage().isEmpty()) {
                    view.updateStatusMessage(newState.getStatusMessage());
                }

                view.render(newState);

                // Check if current player is AI and handle their turn
                Player currentPlayer = model.getCurrentPlayer();
                if (currentPlayer instanceof AIPlayer && !hasPlayedThisTurn) {
                    
                    // Disable buttons for AI turn
                    view.getDrawCardButton().setEnabled(false);
                    view.getNextPlayerButton().setEnabled(false);

                    handleAITurn();

                }
            }
        });

    }

    /**
     * Handles the AI player's entire turn:
     * - Chooses a legal card with a simple strategy, or draws if none
     * - Plays the card (or draws)
     * - Advances to the next player
     * - Re-enables buttons for human players
     */
    private void handleAITurn() {
        hasPlayedThisTurn = true; // lock out extra actions this turn

        Player currentPlayer = model.getCurrentPlayer();
        if (!(currentPlayer instanceof AIPlayer)) {
            // Safety check – should not happen, but just in case
            view.getDrawCardButton().setEnabled(false);
            view.getNextPlayerButton().setEnabled(false);
            hasPlayedThisTurn = false;
            return;
        }

        // Choose what card to play, if any
        Hand hand = currentPlayer.getHand();
        int chosenIndex = chooseAIPlayIndex(hand);

        if (chosenIndex >= 0) {
            // Get the card before playing it to check if it's a wild
            Card chosenCard = hand.getCard(chosenIndex);
            boolean isLightWild = (chosenCard.getValue() == Card.Value.WILD || 
                                   chosenCard.getValue() == Card.Value.WILD_DRAW_TWO);
            boolean isDarkWild = (chosenCard.getValue() == Card.Value.WILD_DRAW_COLOR);
            
            // Try to play the chosen card
            boolean success = model.playCardFromHand(chosenIndex);
            
            if (success && (isLightWild || isDarkWild)) {
                // AI needs to choose a color for the wild card
                Card.Color chosenColor;
                
                if (isDarkWild) {
                    chosenColor = chooseAIDarkWildColor(hand);
                    model.setDarkWildColor(chosenColor);
                } else {
                    chosenColor = chooseAIWildColor(hand);
                    model.setTopWildColor(chosenColor);
                }
            } else if (!success) {
                // If somehow invalid, just draw instead
                model.drawCardForCurrentPlayer();
            }
        } else {
            // No playable card, draw one
            model.drawCardForCurrentPlayer();
        }

        // After action or draw, advance to next player
        model.advanceTurn();

        // Re-enable buttons (in case the next player is human)
        view.getDrawCardButton().setEnabled(true);
        view.getNextPlayerButton().setEnabled(true);

        // New player's turn starts fresh
        hasPlayedThisTurn = false;

    }

    /**
     * Simple AI strategy:
     * 1. Find all playable cards.
     * 2. Prefer a non-wild card that matches the current color.
     * 3. Otherwise, play the first playable card.
     * 4. If none are playable, return -1.
     */
    private int chooseAIPlayIndex(Hand hand) {
        List<Integer> playable = new ArrayList<>();

        for (int i = 0; i < hand.getSize(); i++) {
            Card c = hand.getCard(i);
            if (model.isValidPlay(c)) {
                playable.add(i);
            }
        }

        if (playable.isEmpty()) {
            return -1; // nothing legal, must draw
        }

        // Determine "current color": wild color if active, otherwise top card's color
        Card top = model.getTopCard();
        Card.Color activeColor = (top != null) ? top.getColor() : null;

        // 1) Try to play a non-wild card that matches the active color
        if (activeColor != null) {
            for (int idx : playable) {
                Card c = hand.getCard(idx);
                if (c.getColor() != null && c.getColor() == activeColor) {
                    return idx;
                }
            }
        }

        // 2) Otherwise just play the first playable card
        return playable.get(0);
    }

    /**
     * AI strategy for choosing a light-side wild card color.
     * Selects the color that appears most frequently in the AI's remaining hand.
     * This maximizes the chance of playing more cards on the next turn.
     * 
     * @param hand the AI player's hand
     * @return the chosen light color (RED, BLUE, GREEN, or YELLOW)
     */
    private Card.Color chooseAIWildColor(Hand hand) {
        // Count occurrences of each light color in AI's hand
        int red = 0, blue = 0, green = 0, yellow = 0;
        
        for (int i = 0; i < hand.getSize(); i++) {
            Card c = hand.getCard(i);
            if (c.getColor() == null) continue; // Skip wild cards
            
            switch (c.getColor()) {
                case RED: red++; break;
                case BLUE: blue++; break;
                case GREEN: green++; break;
                case YELLOW: yellow++; break;
            }
        }
        
        // Choose the color with most cards
        int max = Math.max(Math.max(red, blue), Math.max(green, yellow));
        
        if (red == max) return Card.Color.RED;
        if (blue == max) return Card.Color.BLUE;
        if (green == max) return Card.Color.GREEN;
        return Card.Color.YELLOW;
    }

    /**
     * AI strategy for choosing a dark-side wild card color.
     * Selects the color that appears most frequently in the AI's remaining hand.
     * This maximizes the chance of playing more cards on the next turn.
     * 
     * @param hand the AI player's hand
     * @return the chosen dark color (TEAL, PURPLE, PINK, or ORANGE)
     */
    private Card.Color chooseAIDarkWildColor(Hand hand) {
        // Count occurrences of each dark color in AI's hand
        int teal = 0, purple = 0, pink = 0, orange = 0;
        
        for (int i = 0; i < hand.getSize(); i++) {
            Card c = hand.getCard(i);
            if (c.getColor() == null) continue; // Skip wild cards
            
            switch (c.getColor()) {
                case TEAL: teal++; break;
                case PURPLE: purple++; break;
                case PINK: pink++; break;
                case ORANGE: orange++; break;
            }
        }
        
        // Choose the color with most cards
        int max = Math.max(Math.max(teal, purple), Math.max(pink, orange));
        
        if (teal == max) return Card.Color.TEAL;
        if (purple == max) return Card.Color.PURPLE;
        if (pink == max) return Card.Color.PINK;
        return Card.Color.ORANGE;
    }

    /**
     * Handles the action when a player clicks on a card in their hand
     * Determines if the selected card can be played and updates the model
     *
     * @param handIndex the index of the selected card in the player's hand
     */
    @Override
    public void onPlayCard(int handIndex){
        if (model.getCurrentPlayer() instanceof AIPlayer) return;

        if (hasPlayedThisTurn) {
            view.showError("You already played this turn! Click 'Next Player'.");
            return;
        }

        model.saveState();
        boolean valid = model.playCardFromHand(handIndex);
        if (valid){
            hasPlayedThisTurn = true;
        } else{
            view.showError("Invalid card play! Try again.");
        }
    }

    /**
     * Handles the action when a player clicks the "Draw" button
     * Instructs the model to draw a card and updates the game state
     */
    @Override
    public void onDraw(){
        if (model.getCurrentPlayer() instanceof AIPlayer) return;

        if (hasPlayedThisTurn) {
            view.showError("You already acted this turn! Click 'Next Player'.");
            return;
        }

        model.saveState();
        boolean valid = model.drawCardForCurrentPlayer();
        if (valid) hasPlayedThisTurn = true;
        else view.showError("Cannot draw a card right now.");

    }

    /**
     * Handles the action when a player clicks the "Next Player" button
     * Advances the game to the next player's turn
     */
    @Override
    public void onNext(){
        // AI players can always advance (they handle their own logic in handleAITurn)
        if (model.getCurrentPlayer() instanceof AIPlayer) {
            model.advanceTurn();
            return;
        }
        
        // Human players MUST have played or drawn before advancing
        if (!hasPlayedThisTurn) {
            view.showError("You must play a card or draw before ending your turn!");
            return;
        }

        model.saveState();
        // Human has played/drawn, allow turn advance
        model.advanceTurn();
    }

    @Override
    public void onNewRound() {
        model.saveState();
        model.startNewRound();
    }

    @Override
    public void onNewGame() {
        model.saveState();
        model.startNewGame();
    }

    /**
     * Handles the action when a wild card is played and a color is chosen
     * Updates the model with the selected color and refreshes the view
     *
     * @param color the color selected by the player for the wild card
     */
    @Override
    public void onChooseWildCardCol(Card.Color color){
        //applyWildColor(color);
        model.setTopWildColor(color);
    }
    /**
     * Handles the player's selected DARK-side color for the
     * Wild Draw Colour card. This method is called by the view
     * after the user chooses one of the dark colors
     * (TEAL, PURPLE, PINK, or ORANGE).
     * The chosen color is passed to the Game model, which stores
     * it and will later trigger the draw-until-matching-color logic.
     *
     * @param color the DARK-side color selected for the Wild Draw Colour card
     */
    @Override
    public void onChooseDarkWildColor(Card.Color color) {
        model.setDarkWildColor(color);
    }

    @Override
    public void onUndo() {
        model.undo();
        hasPlayedThisTurn = false;
    }

    @Override
    public void onRedo() {
        model.redo();
        hasPlayedThisTurn = false;
    }


    /**
     * Updates the view with the latest state of the model
     * Creates a GameState snapshot and sends it to the view for rendering.
     */
    private void push() {
        GameStateEvent s = model.exportState();
        view.render(s);
    }
    /**
     * Attempts to play a card from the player's hand
     * Calls the appropriate method in the Game model
     *
     * @param index the index of the card to be played
     * @return true if the play was successful; false otherwise
     */
    private boolean attemptPlayCard(int index){
        if (hasPlayedThisTurn) {
            view.showError("You already played this turn! Click 'Next Player'.");
            return false;
        }

        Player current = model.getCurrentPlayer();
        Card card = current.getHand().getCard(index);

        if(!model.isValidPlay(card)){
            view.showError("Invalid card play! Try again");
            return false;
        }

        model.playCardFromHand(index);
        hasPlayedThisTurn = true; // Lock playing again
        return true;
    }




    /**
     * Attempts to draw a card for the current player
     * Calls the model to perform the draw operation
     *
     * @return true if the draw was successful; false otherwise
     */
    private boolean attemptDrawCard(){
        if (hasPlayedThisTurn) {
            view.showError("You already acted this turn! Click 'Next Player'.");
            return false;
        }
        model.drawCardForCurrentPlayer();
        hasPlayedThisTurn = true;
        return true;
    }

    /**
     * Applies the chosen color for a played wild card
     * Updates the game state to reflect the chosen color
     *
     * @param color the color selected by the player
     */
    private void applyWildColor(Card.Color color) {
        model.setTopWildColor(color);
        view.updateStatusMessage("Wild card color set to " + color);
    }


}
