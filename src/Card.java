/**
 * Represents a card in the UNO game.
 * Each card has a color and a value, which can be either a number or an action.
 *
 * @author G27
 * @version 1.0
 */

public class Card {
    public enum Color {RED, BLUE, YELLOW, GREEN};
    public enum Value {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, DRAW_ONE, REVERSE, WILD, WILD_DRAW_TWO, DRAW_FIVE, SKIP_EVERYONE};
    private Color color;
    private Value value;


    /**
     * Constructs a new Card with the specified color and value.
     *
     * @param color the color of the card
     * @param value the value of the card
     */
    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    /**
     * Checks if the given value represents an action card.
     * Action cards include SKIP, DRAW_ONE, REVERSE, WILD, and WILD_DRAW_TWO.
     *
     * @param value the value to check
     * @return true if the value is an action card, false otherwise
     */
    private boolean checkIfActionCard(Value value) {
        return value.equals(Value.SKIP) || value.equals(Value.DRAW_ONE) ||  value.equals(Value.REVERSE) || 
            value.equals(Value.WILD) || value.equals(Value.WILD_DRAW_TWO) || value.equals(Value.DRAW_FIVE) || value.equals(Value.SKIP_EVERYONE);
    }

    /**
     * @return the color of the card
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the value of the card
     */
    public Value getValue() {
        return value;
    }

    /**
     * Determines whether this card is an action card.
     *
     * @return true if this is an action card, false if it's a number card
     */

    public boolean isActionCard() {
        return checkIfActionCard(value);
    }

    /**
     * Returns a string representation of the card.
     * Format: "COLOR VALUE" (e.g., "RED FIVE" or "BLUE SKIP")
     *
     * @return a string describing the card's color and value
     */
    @Override
    public String toString() {
        if (color == null) {
            // Wild cards have no color
            return getValue().toString();
        }
        return getColor() + " " + getValue();
    }
}