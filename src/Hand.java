import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's hand in the UNO game.
 * A hand contains a collection of cards that can be added, removed, and viewed.
 *
 * @author G27
 * @version 1.0
 */
public class Hand {
    private List<Card> cards;

    /**
     * Constructs a new empty Hand.
     * Initializes the internal card list.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to this hand.
     * The card is added to the end of the hand.
     *
     * @param card the card to add to the hand
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes and returns a card at the specified index from this hand.
     *
     * @param index the position of the card to remove (0-based)
     * @return the card that was removed, or null if the index is invalid
     */
    public Card removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.remove(index);
        }
        return null;
    }

    /**
     * Retrieves a card at the specified index without removing it from the hand.
     *
     * @param index the position of the card to retrieve (0-based)
     * @return the card at the specified index, or null if the index is invalid
     */

    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    /**
     * Initializes the hand with 7 cards from the top of the deck.
     * This method is typically called at the start of the game.
     * Cards are removed from the beginning of the deck and added to this hand.
     *
     * @param deck the deck of cards to draw from (cards will be removed from index 0)
     */
    public void startCards(ArrayList<Card>deck){
        for (int i = 0; i<7; i++){
            this.addCard(deck.remove(0));
        }
    }

    /**
     * Gets the number of cards currently in this hand.
     *
     * @return the size of the hand
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Gets a formatted string listing all cards in this hand.
     * Each card is displayed on a separate line.
     *
     * @return a multi-line string representation of all cards in the hand
     */
    public List<Card> getCards() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(cards.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this hand.
     *
     * @return a formatted string showing all cards in the hand
     */
    @Override
    public String toString() {
        return " " + getCards();
    }
}