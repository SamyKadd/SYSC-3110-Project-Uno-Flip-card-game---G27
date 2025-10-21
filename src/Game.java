import java.util.*;

public class Game {
    private List<Player> players;
    private ArrayList<Card>deck;

    public Game() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
    }

    public void addPlayer(Player p){
        players.add(p);
    }
    public void removePlayer(Player p){
        players.remove(p);
    }
    public Player getPlayer(int index){
        return players.get(index);
    }

    public void startGame(){
        for (Player player: players){
            player.getHand().StartCards(deck);
            System.out.println(player.getName() + " hand:");
            player.displayHand();

        }
    }

}