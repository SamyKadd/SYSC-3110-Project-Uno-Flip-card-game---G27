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

    // Getters and setters
    public boolean isTurnComplete() { return turnComplete; }
    public void setTurnComplete(boolean turnComplete) { this.turnComplete = turnComplete; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public boolean canPlay() { return canPlay; }
    public void setCanPlay(boolean canPlay) { this.canPlay = canPlay; }

    public boolean canDraw() { return canDraw; }
    public void setCanDraw(boolean canDraw) { this.canDraw = canDraw; }

    public boolean canNext() { return canNext; }
    public void setCanNext(boolean canNext) { this.canNext = canNext; }

    public boolean needsWildColor() { return needsWildColor; }
    public void setNeedsWildColor(boolean needsWildColor) { this.needsWildColor = needsWildColor; }

    public Card.Color getWildColor() { return wildColor; }
    public void setWildColor(Card.Color wildColor) { this.wildColor = wildColor; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public boolean needsDarkWildColor() { return needsDarkWildColor; }
    public void setNeedsDarkWildColor(boolean needsDarkWildColor) { this.needsDarkWildColor = needsDarkWildColor; }

    public Card.Color getDarkWildColor() { return darkWildColor; }
    public void setDarkWildColor(Card.Color darkWildColor) { this.darkWildColor = darkWildColor; }
}
