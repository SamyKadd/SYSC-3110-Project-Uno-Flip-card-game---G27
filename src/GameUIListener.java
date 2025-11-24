/**
 * Interface for handling UI events from the GameView.
 * This interface is implemented by the GameController to receive
 * notifications of user actions in the GUI.
 * 
 * Part of the MVC architecture, this interface decouples the View
 * from the Controller implementation.
 * 
 * @author G27
 * @version 3.0
 */
public interface GameUIListener {
    void onPlayCard(int handIndex);
    void onDraw();
    void onNext();
    void onChooseWildCardCol(Card.Color color);
    void onChooseDarkWildColor(Card.Color color);

}
