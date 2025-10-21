public class Card {
   public enum Color {RED, BLUE, YELLOW, GREEN};
   public enum Value {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, DRAW_TWO, REVERSE, WILD, WILD_DRAW_FOUR};
   private Color color;
   private Value value;


   public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
   }

    // Need to add the function that will check if it is an Action Card here
    // This is just a mock implementation, have to adjust this based on how we assign
    // action cards.
    private boolean checkIfActionCard(Value value) {
        return value.equals(Value.SKIP) || value.equals(Value.DRAW_TWO);
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    public boolean isActionCard() {
        return checkIfActionCard(value);
    }

    @Override
    public String toString() {
        return getColor() + " " + getValue();
    }
}
