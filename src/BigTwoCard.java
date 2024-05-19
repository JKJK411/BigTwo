/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a Big Two card game
 * 
 * @author Tang Tsz Kit
 */
public class BigTwoCard extends Card{
/**
 * A constructor for building a card with the specified suit and rank
 * @param suit the suit of cards (0-3)
 * @param rank the rank of cards (0-12)
 */
    public BigTwoCard(int suit,int rank){
        super(suit,rank);
    }
/**
 * Comparing the order of this card with the specified card
 * 
 * @return a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card
 */
    public int compareTo(Card card){
        if (this.rank > card.rank) {
			if(card.rank >= 2 && this.rank >= 2){
				return 1;
			}else if(card.rank < 2 && this.rank >= 2){
				return -1;
			}else{
				return 1;
			}
		} else if (this.rank < card.rank ) {
			if(this.rank < 2 && card.rank >=2){
				return 1;
			}else{
				return -1;
			}
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
    }
}
