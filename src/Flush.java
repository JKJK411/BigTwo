public class Flush extends Hand{

    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType(){
        if(this.isValid()){
            return "Flush";
        }else{
            return null;
        }
    }
/**
 * Check whether it is a flush
 * 
 * @return whether it is a flush
 */
    public boolean isValid(){
        if(this.size() != 5){
            return false;
        }
        int temp = this.getCard(0).suit;
        for(int i = 0;i<5;i++){
            if(temp != this.getCard(i).suit){
                return false;
            }
        }
        return true;
    }
    
}
