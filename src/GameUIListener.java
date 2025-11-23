public interface GameUIListener {
    void onPlayCard(int handIndex);
    void onDraw();
    void onNext();
    void onChooseWildCardCol(Card.Color color);
    void onChooseDarkWildColor(Card.Color color);

}
