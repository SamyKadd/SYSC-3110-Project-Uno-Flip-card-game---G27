import java.util.*;

public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;

    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        // Initialize deck with cards
        // initializeDeck();
    }

    // private void initializeDeck() {
    // }

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

}