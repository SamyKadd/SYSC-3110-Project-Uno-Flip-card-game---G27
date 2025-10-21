public class Player {
    private String name;
    private int score;
    private Hand hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }
    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // We'll need this later to sum up scores at the end of rounds, but game class will 
    // have to call this function when a player wins. Sort of like: winningplayer.addScore(5);
    // public void addScore(int points) {
    //     this.score += points;
    // }

    public int getScore() {
        return score;
    }

    public Hand getHand(){
        return hand;
    }

    public void displayHand(){
        //System.out.println(hand.toString());
        System.out.println(hand.getCards());;
    }

}
