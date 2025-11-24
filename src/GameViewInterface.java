import javax.swing.*;
public interface GameViewInterface {
    void setListener(GameUIListener listener);
    void updateStatusMessage(String message);
    void render(GameStateEvent state);
    JButton getDrawCardButton();
    JButton getNextPlayerButton();
    void showError(String message);
}
