public class Card {
    private String color;
    private String value;
    // Variable for cards that are +4, +2, etc...
    // private boolean isActionCard;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
        // this.isActionCard = ___; // Logic to determine if it's an action card
    }

    // Need to add the function that will check if it is an Action Card here

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    // public boolean isActionCard() {
    //     return isActionCard;
    // }

    @Override
    public String toString() {
        return color + " " + value;
    }
}
