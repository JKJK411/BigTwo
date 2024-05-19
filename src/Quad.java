public class Quad extends Hand{

    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
/**
 * Retrieve the type of the hand
 * 
 * @return the type of hand
 */
    public String getType(){
        if(this.isValid()){
            return "Quad";
        }else{
            return null;
        }
    }
/**
 * Check whether it is a quad
 * 
 * @return whether it is a quad
 */
    public boolean isValid(){
        if(this.size() != 5){
            return false;
        }
        int[] count = new int[13];
        boolean s1 = false;
        boolean s2 = false;
        for(int i =0;i<13;i++){
            count[i] = 0;
        }
        for(int i = 0;i<5;i++){
            count[this.getCard(i).rank] += 1;
        }
        for(int i =0;i<13;i++){
            if(count[i] == 4){
                s1 = true;
            }
            if(count[i] == 1){
                s2 = true;
            }
        }
        if(s1 && s2){
            return true;
        }else{
            return false;
        }
    }
    
}
