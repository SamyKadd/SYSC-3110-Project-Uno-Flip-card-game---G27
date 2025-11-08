import java.util.ArrayList;
import java.util.List;
public class GameController implements GameUIListener {
    private Game model;
    private GameView view;

    /**
     * Handles the action when a player clicks on a card in their hand
     * Determines if the selected card can be played and updates the model
     *
     * @param handIndex the index of the selected card in the player's hand
     */
    @Override
    public void onPlayCard(int handIndex){
        attemptPlayCard(handIndex);
        push();
    }

    /**
     * Handles the action when a player clicks the "Draw" button
     * Instructs the model to draw a card and updates the game state
     */
    @Override
    public void onDraw(){
        attemptDrawCard();
        push();
    }

    /**
     * Handles the action when a player clicks the "Next Player" button
     * Advances the game to the next player's turn
     */
    @Override
    public void onNext(){
        advanceTurn();
        push();
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
        push();
    }

    /**
     * Constructs a new GameController and connects the model and view
     *
     * @param model the game model that contains the logic and data
     * @param view the game view responsible for the GUI
     */
    public GameController(Game model, GameView view){
        this.model = model;
        this.view = view;
        this.view.setListener(this);
    }

    /**
     * Updates the view with the latest state of the model
     * Creates a GameState snapshot and sends it to the view for rendering.
     */
    private void push(){
        GameState state = new GameState();
        Player current = model.getPlayer(0);
        state.curPlayerName = current.getName();
        state.curHand = getHandCards(current);
        state.topCard = null;
        state.statusMessage = getStatusMessage();
        state.canDraw = true;
        state.canPlay = true;
        state.canNext = true;
        view.render(state);
    }

    /**
     * Retrieves the list of cards currently held by a player
     *
     * @param player the player whose hand will be retrieved
     * @return a list of Card objects representing the player's hand
     */
    private List<Card> getHandCards(Player player){
        return player.getHand().getCardsList();
    }

    /**
     * Retrieves the current status message from the model, if available
     *
     * @return the current game status message or an empty string if none
     */
    private String getStatusMessage(){
        return "";
    }


    /**
     * Attempts to play a card from the player's hand
     * Calls the appropriate method in the Game model
     *
     * @param index the index of the card to be played
     * @return true if the play was successful; false otherwise
     */
    private boolean attemptPlayCard(int index){
        Player current = model.getPlayer(0);
        Card card = current.getHand().getCard(index);
        if(!model.isValidPlay(card)){
            view.showError("Invalid card play");
            return false;
        }
        current.getHand().removeCard(index);
        setStatus("Played"+ card);
        return true;
    }

    /**
     * Attempts to draw a card for the current player
     * Calls the model to perform the draw operation
     *
     * @return true if the draw was successful; false otherwise
     */
    private boolean attemptDrawCard(){
        setStatus("You drew a card.");
        return true;
    }

    /**
     * Advances the game to the next player's turn
     * Delegates turn progression to the model
     */
    private void advanceTurn(){
        setStatus("Next player's turn!");

    }

    /**
     * Applies the chosen color for a played wild card
     * Updates the game state to reflect the chosen color
     *
     * @param color the color selected by the player
     */
    private void applyWildColor(Card.Color color){
        setStatus("The Wild card color chosen is:" + color);
    }

    /**
     * Updates the model with a new status message to display on the view
     *
     * @param message the message to display in the game's status area
     */
    private void setStatus(String message){
        view.updateStatusMessage(message);
    }

    /**
     * Determines if a given card is a wild card
     *
     * @param card the card to check
     * @return true if the card is a wild card; false otherwise
     */
    private boolean isWild(Card card){
        return card.getValue() == Card.Value.WILD || card.getValue() == Card.Value.WILD_DRAW_TWO;
    }
}
