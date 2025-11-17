/**
 * Represents an AI player in the UNO game.
 * Extends Player class and adds AI decision-making capabilities.
 *
 * @author G27
 * @version 2.0
 */
public class AIPlayer extends Player {
    
    /**
     * Constructs a new AI Player with the specified name.
     *
     * @param name the name of the AI player
     */
    public AIPlayer(String name) {
        super(name + " (AI)");
    }
}