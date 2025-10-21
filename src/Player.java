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

    public void StartCards(ArrayList<Card>deck){ //to start with 7 cards (initializes)
        for (int i = 0; i<7; i++){
            hand.addCard(deck.remove(0));
        }
    }

    public void displayHand(){
        //System.out.println(hand.toString());
    }

}
