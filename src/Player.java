/**
 * Represents a player in the UNO game.
 * Each player has a name, score, and hand of cards.
 *
 * @author G27
 * @version 1.0
 */
public class Player {
    private String name;
    private int score;
    private Hand hand;

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player with an empty hand and a score of 0.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    /**
     * Gets the name of this player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the score for this player.
     *
     * @param score the new score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    // We'll need this later to sum up scores at the end of rounds, but game class will 
    // have to call this function when a player wins. Sort of like: winningplayer.addScore(5);
    /**
     * Adds points to the player's current score.
     * This method is used at the end of rounds to accumulate points. 
     * @param points the number of points to add to the score
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Gets the current score of this player.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the hand of cards held by this player.
     *
     * @return the player's hand
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * Displays all cards in this player's hand.
     * Prints each card on a separate line to the console.
     */
    public void displayHand(){
        System.out.println(hand.getCards());
    }

    /**
     * Returns a string representation of this player,
     * including their name, current score, and number of cards in hand.
     * @return a formatted String summarizing the player's state
     */
    @Override
    public String toString() {
        return "Player " + getName() + " (Score: " + getScore() + ", Cards: " + hand.getSize() + ")";
    }

}