import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    // public Card removeCard(int index) {
    // }

    // Will have identical logic to remove, but obviously just getting instead of removing
    // public Card getCard(int index) {
    // }

    public int getSize() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }

    // @Override
    // public String toString() {
    // }
}
