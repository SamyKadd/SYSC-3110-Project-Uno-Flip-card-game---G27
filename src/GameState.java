import java.util.ArrayList;
import java.util.List;

public class GameState {
    public String curPlayerName;
    public List<Card> curHand = new ArrayList<>();
    public Card topCard;
    public boolean turnComplete = false;


    public String statusMessage = "";

    //Using the following to enable or disable controls in view
    public boolean canPlay;
    public boolean canDraw;
    public boolean canNext;
    public boolean needsWildColor;
    public Card.Color wildColor;
    public boolean gameOver = false;

}
