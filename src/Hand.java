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

    public Card removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.remove(index);
        }
        return null;
    }

    // Will have identical logic to remove, but obviously just getting instead of removing
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    public void StartCards(ArrayList<Card>deck){ //to start with 7 cards (initializes)
        for (int i = 0; i<7; i++){
            this.addCard(deck.remove(0));
        }
    }


    public int getSize() {
        return cards.size();
    }

    public String getCards() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(cards.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return " " + getCards();
    }
}
