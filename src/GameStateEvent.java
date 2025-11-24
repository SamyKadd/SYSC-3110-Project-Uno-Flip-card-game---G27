import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class GameStateEvent extends EventObject {
    private final String curPlayerName;
    private final List<Card> curHand;
    private final Card topCard;

    private boolean turnComplete = false;
    private String statusMessage = "";

    // Control flags for the view
    private boolean canPlay = false;
    private boolean canDraw = false;
    private boolean canNext = false;
    private boolean needsWildColor = false;
    private Card.Color wildColor;

    private boolean gameOver = false;
    private boolean needsDarkWildColor = false;
    private Card.Color darkWildColor;

    public GameStateEvent(Object source, String curPlayerName, List<Card> curHand, Card topCard) {
        super(source);
        this.curPlayerName = curPlayerName;
        this.curHand = new ArrayList<>(curHand);
        this.topCard = topCard;
    }

    //Getters
    public String getCurPlayerName() { return curPlayerName; }
    public List<Card> getCurHand() { return new ArrayList<>(curHand); }
    public Card getTopCard() { return topCard; }

    public boolean isTurnComplete() { return turnComplete; }
    public void setTurnComplete(boolean turnComplete) { this.turnComplete = turnComplete; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String msg) { this.statusMessage = msg; }
}
