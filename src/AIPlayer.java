import java.io.Serializable;

/**
 * Represents an AI player in the UNO game.
 * Extends Player class and adds AI decision-making capabilities.
 *
 * @author G27
 * @version 2.0
 */
public class AIPlayer extends Player {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AI Player with the specified name.
     *
     * @param name the name of the AI player
     */
    public AIPlayer(String name) {
        super(name + " (AI)");
    }
}