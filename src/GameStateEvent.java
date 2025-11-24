import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class GameStateEvent extends EventObject {
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
    public boolean needsDarkWildColor = false;
    public Card.Color darkWildColor;

    public GameStateEvent(Object source, String curPlayerName, List<Card> curHand, Card topCard) {
        super(source);
        this.curPlayerName = curPlayerName;
        this.curHand = new ArrayList<>(curHand);
        this.topCard = topCard;
    }

    public String getCurPlayerName() { return curPlayerName; }
    public List<Card> getCurHand() { return new ArrayList<>(curHand); }
    public Card getTopCard() { return topCard; }

    public boolean isTurnComplete() { return turnComplete; }
    public void setTurnComplete(boolean turnComplete) { this.turnComplete = turnComplete; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String msg) { this.statusMessage = msg; }
}
