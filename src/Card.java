public class Card {
    private String color;
    private String value;
    // Variable for cards that are +4, +2, etc...
    private boolean isActionCard;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
        this.isActionCard = checkIfActionCard(value);
    }

    // Need to add the function that will check if it is an Action Card here
    // This is just a mock implementation, have to adjust this based on how we assign
    // action cards.
    // private boolean checkIfActionCard(String value) {
    //     return value.equals("Skip") || value.equals("Draw Two");
    // }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public boolean isActionCard() {
        return isActionCard;
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}
