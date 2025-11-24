import javax.swing.*;

/**
 * Interface defining the contract for the game's View component.
 * This interface is implemented by GameView and defines all methods
 * that the Controller needs to interact with the View.
 * 
 * Part of the MVC architecture, this interface allows the Controller
 * to work with any View implementation that conforms to this contract.
 * 
 * @author G27
 * @version 3.0
 */
public interface GameViewInterface {
    void setListener(GameUIListener listener);
    void updateStatusMessage(String message);
    void render(GameStateEvent state);
    JButton getDrawCardButton();
    JButton getNextPlayerButton();
    void showError(String message);
}
