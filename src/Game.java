import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;
    private int currentPlayerIndex;
    private boolean clockwise; //+1 forward and -1 reverse order
    private Card top; //The card thats on the top of the discard pile
    private Card.Color topWild = null; //If the top card on discard pile is wild card

    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
        // Initialize deck with cards
        initializeDeck();
    }

    // Not 100% sure if this is correct/works, have to test this later
    private void initializeDeck() {
        // Adding numbered cards (0-9)
        for (Card.Color color : Card.Color.values()) {
            // According to the UNO website, there is only one 0 card per color
            deck.add(new Card(color, Card.Value.ZERO));

            // Every other number has two per color
            deck.add(new Card(color, Card.Value.ONE));
            deck.add(new Card(color, Card.Value.ONE));
            deck.add(new Card(color, Card.Value.TWO));
            deck.add(new Card(color, Card.Value.TWO));
            deck.add(new Card(color, Card.Value.THREE));
            deck.add(new Card(color, Card.Value.THREE));
            deck.add(new Card(color, Card.Value.FOUR));
            deck.add(new Card(color, Card.Value.FOUR));
            deck.add(new Card(color, Card.Value.FIVE));
            deck.add(new Card(color, Card.Value.FIVE));
            deck.add(new Card(color, Card.Value.SIX));
            deck.add(new Card(color, Card.Value.SIX));
            deck.add(new Card(color, Card.Value.SEVEN));
            deck.add(new Card(color, Card.Value.SEVEN));
            deck.add(new Card(color, Card.Value.EIGHT));
            deck.add(new Card(color, Card.Value.EIGHT));
            deck.add(new Card(color, Card.Value.NINE));
            deck.add(new Card(color, Card.Value.NINE));

            // Adding two of each action card per color
            deck.add(new Card(color, Card.Value.SKIP));
            deck.add(new Card(color, Card.Value.SKIP));
            deck.add(new Card(color, Card.Value.DRAW_ONE));
            deck.add(new Card(color, Card.Value.DRAW_ONE));
            deck.add(new Card(color, Card.Value.REVERSE));
            deck.add(new Card(color, Card.Value.REVERSE));
        }

        // Wild cards (4 of each type, no color)
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(null, Card.Value.WILD));
            deck.add(new Card(null, Card.Value.WILD_DRAW_TWO));
        }

        // Shuffle the deck
        Collections.shuffle(deck);
    }

    public void addPlayer(Player p){
        if (players.size() >= 4) {
            System.out.println("Cannot add more players. Maximum is 4 players.");
            return;
        }
        players.add(p);
    }
    public void removePlayer(Player p){
        players.remove(p);
    }
    public Player getPlayer(int index){
        return players.get(index);
    }

    public void startGame(){

        if (players.size() < 2) {
            System.out.println("Need at least 2 players to start the game.");
            return;
        }

        if (players.size() > 4) {
            System.out.println("Maximum 4 players allowed.");
            return;
        }

        for (Player player: players){
            player.getHand().StartCards(deck);
            System.out.println(player.getName() + " hand:");
            player.displayHand();

        }

        //
        top = deck.remove(0);
        System.out.println("Starting card: " + top);
        System.out.println("\nGame starting!\n");

        playGame();
    }

    private void playGame() {
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // Display game state
            System.out.println("===========================================");
            System.out.println(currentPlayer.getName() + "'s turn");
            System.out.println("Top card on discard pile: " + (topWild != null ? topWild : top));
            System.out.println("-------------------------------------------");
            
            // Show current player's hand with numbers
            System.out.println("Your cards:");
            Hand hand = currentPlayer.getHand();
            for (int i = 0; i < hand.getSize(); i++) {
                System.out.println(i + ": " + hand.getCard(i));
            }

            // More remaining
    }

    public boolean isValidPlay(Card cardToPlay) {

        if (cardToPlay.getValue() == Card.Value.WILD || cardToPlay.getValue() == Card.Value.WILD_DRAW_TWO) {
            return true;
        }
        
        if (topWild != null) {
            return cardToPlay.getColor() == topWild;
        }
        
        if (top != null) {
            return cardToPlay.getColor() == top.getColor() || cardToPlay.getValue() == top.getValue();
        }
        
        return true;
    }

    public void handleActionCard(Card card){
        if(card.isActionCard()) {
            switch (card.getValue()) {
                case SKIP:
                    currentPlayerIndex = nextPlayer(nextPlayer(currentPlayerIndex));
                    System.out.println("Skipping player. Next Turn: " + players.get(currentPlayerIndex).getName());
                    break;

                case WILD:
                    topWild = askColorSwitch();
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Wild has been played, color is set to " + topWild + ". Next Turn: "  + players.get(currentPlayerIndex).getName());
                    break;

                case WILD_DRAW_TWO:
                    topWild = askColorSwitch();
                    drawCards(nextPlayer(currentPlayerIndex), 2);
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Wild +2 has been played, color is set to " + topWild + ". " + players.get(currentPlayerIndex).getName() + " Drew 2 card, it is now their turn.");
                    break;

                case DRAW_ONE:
                    drawCards(nextPlayer(currentPlayerIndex), 1);
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println(players.get(currentPlayerIndex).getName() + " Drew 1 card, it is now their turn.");
                    break;

                case REVERSE:
                    clockwise = !clockwise;
                    currentPlayerIndex = nextPlayer(currentPlayerIndex);
                    System.out.println("Reversing direction. Next Turn: " + players.get(currentPlayerIndex).getName());
                    break;
            }
        }
    }

    //This class is desgined to return the next player
    private int nextPlayer(int index){
        if (clockwise) {
            return (index + 1) % players.size();
        } else {
            return (index - 1 + players.size()) % players.size();
        }
    }
    //Taking a card from the top of the deck and returning it
    private Card drawCard(){
        if (deck.isEmpty()) {
            System.out.println("Deck is empty! (TODO: reshuffle from discard if you add one)");
            return null;
        }
        // draw from top of list; if you prefer, use remove(deck.size()-1)
        return deck.remove(0);
    }
    //Drawing a Card from deck and putting it in players hands
    private void drawCards(int index, int count) {
        for (int i = 0; i < count; i++) {
            Card card = drawCard();
            if (card != null) {
                players.get(index).getHand().addCard(card);
            }
        }
    }
    //Getting user to pick next color
    private Card.Color askColorSwitch(){
        while (true){
            System.out.print("Choose a color (R/G/Y/B:   ");
            Scanner scanner = new Scanner(System.in);
            String colorChose = scanner.nextLine();
            switch (colorChose){
                case "R": return Card.Color.RED;
                case "G": return Card.Color.GREEN;
                case "Y": return Card.Color.YELLOW;
                case "B": return Card.Color.BLUE;
                default:;
                    System.out.println("Invalid option");
            }
        }
    }
}