import java.lang.reflect.Array;
import java.util.Arrays;

public class Straight extends Hand{
    public Straight(CardGamePlayer player, CardList cards){
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType() {
        if(this.isValid()){
            return "Straight";
        }
        return null;
    }
/**
 * Check whether it is a straight
 * 
 * @return whether it is a straight
 */
    public boolean isValid() {
        if(this.size() != 5){
            return false;
        }
        this.sort();
        int[] cards = new int[5];
        for(int i = 0;i<5;i++){
            cards[i] = this.getCard(i).rank;
        }
        int[] array= {0,9,10,11,12};
        int[] array1= {0,1,10,11,12};
        if(Arrays.equals(cards, array)||Arrays.equals(cards, array1)){
            return true;
        }
        int idx = this.getCard(0).rank;
        for(int i = 0;i<5;i++){
            if(idx+i != this.getCard(i).rank){
                return false;
            }
        }
        
        return true;
    }
    
}
