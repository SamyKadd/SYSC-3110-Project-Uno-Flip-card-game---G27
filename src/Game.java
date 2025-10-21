import java.util.*;

public class Game {
    private List<Player> players;

    public Game() {
        players = new ArrayList<>();
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
}