public class Pair extends Hand{
    public Pair(CardGamePlayer player, CardList cards){
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType(){
        if(isValid() == true){
            return "Pair";
        }return null;
    }
/**
 * Check whether it is a pair
 * 
 * @return whether it is a pair
 */
    public boolean isValid(){
        if(this.size() == 2){
            if(this.getCard(0).rank == this.getCard(1).rank){
                return true;
            }
        }
        return false;
    }

}
