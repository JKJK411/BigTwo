public class Single extends Hand{

    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType(){
        if(this.isValid() == true){
            return "Single";
        }else{
            return null;
        }
    }
/**
 * Check whether it is a single
 * 
 * @return whether it is a single
 */
     public boolean isValid(){
        if (this.size() == 1){
            return true;
        }else{
            return false;
        }
     }
}
