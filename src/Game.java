import java.util.*;

public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;
    private int currentPlayerIndex;
    private boolean clockwise;

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
    }
    public void handleActionCard(Card card){
        if(card.isActionCard()) {
            switch (card.getValue()) {
                case SKIP:
                    currentPlayerIndex = (currentPlayerIndex + 2) % players.size();
                    System.out.println("Skipping player. Next Turn: " + players.get(currentPlayerIndex).getName());
                    break;
                case WILD:

                    break;
                case WILD_DRAW_TWO:

                    break;
                case DRAW_ONE:
                    int nextPlayer = (currentPlayerIndex + 1) % players.size();
                    players.get(nextPlayer).getHand().addCard(deck.remove(0));
                    System.out.println(players.get(nextPlayer).getName() + " draws one card.");
                    break;
                case REVERSE:
                    clockwise = !clockwise;
                    System.out.println("Direction reversed!");
                    break;
            }
        }
    }

}