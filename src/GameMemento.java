import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class GameMemento implements Serializable {
    private static final long serialVersionUID = 1L;

    // Snapshot of all players and their hands/scores.
    private final List<Player> playersSnapshot;

    // Index of the current player whose turn it is.
    private final int currentPlayerIndex;

    //Snapshot of the draw pile at the moment of saving.
    private final ArrayList<Card> drawPileSnapshot;

    //Snapshot of the discard pile at the moment of saving.
    private final ArrayList<Card> discardPileSnapshot;

    // Active wild card color chosen by a LIGHT wild card.
    private final Card.Color wildColor;

    // Active wild card color chosen by a DARK wild card.
    private final Card.Color darkWildColor;

    // Whether the game is currently in reverse turn order.
    private final boolean reverseDirection;

    /**
     * Constructs a GameMemento containing a deep snapshot of all relevant
     * game fields required to restore the game at a later time.
     *
     * @param players   list of all players in the game
     * @param currentPlayerIndex index of the current player
     * @param drawPile  current draw pile
     * @param discardPile current discard pile
     * @param wildColor currently active LIGHT-side wild color
     * @param darkWildColor currently active DARK-side wild color
     * @param reverseDirection current direction of play
     */
    public GameMemento(
            List<Player> players,
            int currentPlayerIndex,
            ArrayList<Card> drawPile,
            ArrayList<Card> discardPile,
            Card.Color wildColor,
            Card.Color darkWildColor,
            boolean reverseDirection
    ) {
        this.playersSnapshot = deepCopyPlayers(players);
        this.currentPlayerIndex = currentPlayerIndex;
        this.drawPileSnapshot = new ArrayList<>(drawPile);
        this.discardPileSnapshot = new ArrayList<>(discardPile);
        this.wildColor = wildColor;
        this.darkWildColor = darkWildColor;
        this.reverseDirection = reverseDirection;
    }

    /**
     * Creates a deep copy of all players, including their hands and scores,
     * using serialization-based cloning (ensures independence from future changes).
     *
     * @param players the list of players to copy
     * @return deep-copied list of players
     */
    private List<Player> deepCopyPlayers(List<Player> players) {
        ArrayList<Player> copy = new ArrayList<>();
        for (Player p : players) {
            copy.add(deepCopy(p));
        }
        return copy;
    }

    /**
     * Deep-copies a single Player instance.
     *
     * @param p Player to clone
     * @return cloned Player
     */
    private Player deepCopy(Player p) {
        return SerializationUtils.clone(p);
    }


    /** @return snapshot of all players */
    public List<Player> getPlayersSnapshot() {
        return playersSnapshot;
    }

    /** @return index of the current player */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /** @return snapshot of the draw pile */
    public ArrayList<Card> getDrawPileSnapshot() {
        return drawPileSnapshot;
    }

    /** @return snapshot of the discard pile */
    public ArrayList<Card> getDiscardPileSnapshot() {
        return discardPileSnapshot;
    }

    /** @return active LIGHT-side wild color */
    public Card.Color getWildColor() {
        return wildColor;
    }

    /** @return active DARK-side wild color */
    public Card.Color getDarkWildColor() {
        return darkWildColor;
    }

    /** @return whether the game was in reverse direction */
    public boolean isReverseDirection() {
        return reverseDirection;
    }

}
