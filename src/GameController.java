/**
 * Controller for the UNO game (MVC architecture).
 * Listens for user input from the GameView and updates the Game model.
 * Observes model state changes and refreshes the view accordingly.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
public class GameController implements GameUIListener {
    private Game model;
    private GameView view;


    /**
     * Constructs a new GameController and connects the model and view
     *
     * @param model the game model that contains the logic and data
     * @param view the game view responsible for the GUI
     */
    public GameController(Game model, GameView view) {
        this.model = model;
        this.view = view;
        this.view.setListener(this);

        this.model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    GameState newState = (GameState) evt.getNewValue();
                    view.render(newState);
                }
            }
        });
    }

    /**
     * Handles the action when a player clicks on a card in their hand
     * Determines if the selected card can be played and updates the model
     *
     * @param handIndex the index of the selected card in the player's hand
     */
    @Override
    public void onPlayCard(int handIndex){
        attemptPlayCard(handIndex);
    }

    /**
     * Handles the action when a player clicks the "Draw" button
     * Instructs the model to draw a card and updates the game state
     */
    @Override
    public void onDraw(){
        attemptDrawCard();
    }

    /**
     * Handles the action when a player clicks the "Next Player" button
     * Advances the game to the next player's turn
     */
    @Override
    public void onNext(){
        advanceTurn();
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
        Player current = model.getCurrentPlayer();
        Card card = current.getHand().getCard(index);

        if(!model.isValidPlay(card)){
            view.showError("Invalid card play! Try again");
            return false;
        }
        current.getHand().removeCard(index);
        model.handleActionCard(card);
        view.updateStatusMessage(current.getName() + " played " + card);
        return true;
    }

    /**
     * Attempts to draw a card for the current player
     * Calls the model to perform the draw operation
     *
     * @return true if the draw was successful; false otherwise
     */
    private boolean attemptDrawCard(){
        model.drawCardForCurrentPlayer();
        view.updateStatusMessage("You drew a card.");
        return true;
    }

    /**
     * Advances the game to the next player's turn
     * Delegates turn progression to the model
     */
    private void advanceTurn(){
        model.advanceTurn();
        view.updateStatusMessage("Next player's turn!");

    }

    /**
     * Applies the chosen color for a played wild card
     * Updates the game state to reflect the chosen color
     *
     * @param color the color selected by the player
     */
    private void applyWildColor(Card.Color color){

        view.updateStatusMessage("The wild card color is now " + color);
    }

}
