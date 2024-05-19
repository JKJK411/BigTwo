public class Triple extends Hand{
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType(){
        if(this.isValid()){
            return "Triple";
        }else{
            return null;
        }
    }
/**
 * Check whether it is a triple
 * 
 * @return whether it is a triple
 */
    public boolean isValid(){
        if(this.size() != 3){
            return false;
        }
        int hold = this.getCard(0).rank;
        for(int i = 0;i<3;i++){
            if(this.getCard(i).rank != hold){
                return false;
            }
        }
        return true;
    }
}
