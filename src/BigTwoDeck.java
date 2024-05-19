/**
 * The BigTwoDeck class is a subclass of the Deck class and is used to model a deck of cards used in a Big Two card game
 * 
 * @author Tang Tsz Kit
 */
public class BigTwoDeck extends Deck {
/**
 * Initializing a deck of Big Two cards and shuffle
 */
    public void initialize(){
        this.removeAllCards();
        if (isEmpty() == true){
            for(int i = 0;i<4;i++){
                for(int j = 0;j<13;j++){
                    Card card = new Card(i,j);
                    addCard(card);
                }
            }
        }
        shuffle();
    }
}
