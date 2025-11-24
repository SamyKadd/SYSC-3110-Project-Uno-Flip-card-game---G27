import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class GameStateEvent extends EventObject {
    private String curPlayerName;
    private List<Card> curHand = new ArrayList<>();
    private Card topCard;

    private boolean turnComplete = false;
    private String statusMessage = "";

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

    //Getters and Setters
    public String getCurPlayerName() { return curPlayerName; }
    public List<Card> getCurHand() { return new ArrayList<>(curHand); }
    public Card getTopCard() { return topCard; }

    public boolean isTurnComplete() { return turnComplete; }
    public void setTurnComplete(boolean turnComplete) { this.turnComplete = turnComplete; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String msg) { this.statusMessage = msg; }

    public boolean isCanPlay() { return canPlay; }
    public void setCanPlay(boolean canPlay) { this.canPlay = canPlay; }

    public boolean isCanDraw() { return canDraw; }
    public void setCanDraw(boolean canDraw) { this.canDraw = canDraw; }

    public boolean isCanNext() { return canNext; }
    public void setCanNext(boolean canNext) { this.canNext = canNext; }

    public boolean isNeedsWildColor() { return needsWildColor; }
    public void setNeedsWildColor(boolean needsWildColor) { this.needsWildColor = needsWildColor; }

    public Card.Color getWildColor() { return wildColor; }
    public void setWildColor(Card.Color wildColor) { this.wildColor = wildColor; }

    public boolean isNeedsDarkWildColor() { return needsDarkWildColor; }
    public void setNeedsDarkWildColor(boolean needsDarkWildColor) { this.needsDarkWildColor = needsDarkWildColor; }

    public Card.Color getDarkWildColor() { return darkWildColor; }
    public void setDarkWildColor(Card.Color darkWildColor) { this.darkWildColor = darkWildColor; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
}
