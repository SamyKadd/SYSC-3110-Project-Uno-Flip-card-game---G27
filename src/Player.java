import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private Hand hand;

    public Player(String name) {
        this.name = name;
        hand = new Hand();
    }
    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void displayHand(){
        //System.out.println(hand.toString());
    }

}
