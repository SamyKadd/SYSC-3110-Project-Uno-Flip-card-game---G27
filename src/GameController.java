/**
 * Controller for the UNO game (MVC architecture).
 * Listens for user input from the GameView and updates the Game model.
 * Observes model state changes and refreshes the view accordingly.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameController implements GameUIListener {
    private Game model;
    private GameViewInterface view;
    private boolean hasPlayedThisTurn = false;

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
                boolean turnChanged = newState.curPlayerName != null &&
                        !newState.curPlayerName.equals(lastPlayer);
                boolean twoPlayerReverseReplay = newState.statusMessage != null &&
                        newState.statusMessage.contains("Same player goes again!");
                if (turnChanged || twoPlayerReverseReplay) {
                    hasPlayedThisTurn = false;
                }
                lastPlayer = newState.curPlayerName;

                // Always show the model’s status message
                if (newState.statusMessage != null && !newState.statusMessage.isEmpty()) {
                    view.updateStatusMessage(newState.statusMessage);
                }

                view.render(newState);

                // Check if current player is AI and handle their turn
                Player currentPlayer = model.getCurrentPlayer();
                if (currentPlayer instanceof AIPlayer && !hasPlayedThisTurn) {
                    
                    // Disable buttons for AI turn
                    view.getDrawCardButton().setEnabled(false);
                    view.getNextPlayerButton().setEnabled(false);
                    // THESE HAVE TO BE REENABLED IN HANDLEAITURN or else they will not be clickable and the game
                    // will be permanently stuck on the AI turn
                }
            }
        });

// Have to implement a function here to handle the AI turn, haven't figured out
// 100% how yet



    }

    /**
     * Handles the action when a player clicks on a card in their hand
     * Determines if the selected card can be played and updates the model
     *
     * @param handIndex the index of the selected card in the player's hand
     */
    @Override
    public void onPlayCard(int handIndex){
        // Don't allow human interaction during AI turns
        if (model.getCurrentPlayer() instanceof AIPlayer) {
            return;
        }
        attemptPlayCard(handIndex);
    }

    /**
     * Handles the action when a player clicks the "Draw" button
     * Instructs the model to draw a card and updates the game state
     */
    @Override
    public void onDraw(){
        if (model.getCurrentPlayer() instanceof AIPlayer) {
            return;
        }
        attemptDrawCard();
    }

    /**
     * Handles the action when a player clicks the "Next Player" button
     * Advances the game to the next player's turn
     */
    @Override
    public void onNext(){
        model.advanceTurn();
    }

    /**
     * Handles the action when a wild card is played and a color is chosen
     * Updates the model with the selected color and refreshes the view
     *
     * @param color the color selected by the player for the wild card
     */
    @Override
    public void onChooseWildCardCol(Card.Color color){
        applyWildColor(color);
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



    /**
     * Updates the view with the latest state of the model
     * Creates a GameState snapshot and sends it to the view for rendering.
     */
    private void push() {
        view.render(model.exportState());
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
