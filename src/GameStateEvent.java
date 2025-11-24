import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class GameStateEvent extends EventObject {
    private String curPlayerName;
    private List<Card> curHand = new ArrayList<>();
    private Card topCard;

    private boolean turnComplete = false;
    private String statusMessage = "";

    // Using these to enable or disable controls in the view
    private boolean canPlay;
    private boolean canDraw;
    private boolean canNext;

    private boolean needsWildColor;
    private Card.Color wildColor;

    private boolean needsDarkWildColor;
    private Card.Color darkWildColor;

    private boolean gameOver = false;

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
