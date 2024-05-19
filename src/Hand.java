/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards. It has a private instance variable for storing the player who plays this hand
 * @author Tang Tsz Kit
 */
public abstract class Hand extends CardList{
    private CardGamePlayer player;
/**
 * A constructor for building a hand with the specified player and list of cards
 * @param player the player who played the hand
 * @param cards the hand played by the player
 */
    public Hand(CardGamePlayer player,CardList cards){
        this.player = player;
        for(int i = 0;i<cards.size();i++){
            this.addCard(cards.getCard(i));
        }
    }
/**
 * Retrieving the player of this hand
 * @return the player who played the hand
 */
    public CardGamePlayer getPlayer(){
        return this.player;
    }
/**
 * Retrieving the top card of this hand
 * @return the top card of this hand
 */
    public Card getTopCard(){
        this.sort();
        int[] count = new int[13];
        int index = -1;
        int j = 0;
        int highest = -1;
        boolean sw = true;
        for(int i = 0;i<13;i++){
            count[i] = 0;
        }
        for (int i = 0;i<this.size();i++){
            count[this.getCard(i).rank] += 1;
        }
        for(int i = 0;i<13;i++){
            if(count[i]>highest){
                highest = count[i];
                index = i;
            }
            if(count[i]==highest){
                int sw1 = new BigTwoCard(0,i).compareTo(new BigTwoCard(0, index));
                if(sw1 == 1){
                    index = i;
                }
            }
        }

        for (int i = 0;i<this.size();i++){
            if(this.getCard(i).rank == index){
                for(j = i;j<this.size();j++){
                    if(this.getCard(j).rank != index){
                        sw = false;
                        break;
                    }
                }
                j = j -1;
                return this.getCard(j);
            }
        }
        return null;

    }
/**
 * Checking if this hand beats a specified hand
 * @param hand the hand played by the player
 * @return whether this hand beats a specified hand
 */
    public boolean beats(Hand hand){
        int d;
        int tidx = -1;
        int hidx = -1;
        String[] count = {"Straight","Flush","FullHouse","Quad","StraightFlush"};
        if(this.player == hand.getPlayer()){
            return true;
        }
        if(this.getType() == hand.getType()){
            BigTwoCard c = new BigTwoCard(this.getTopCard().suit, this.getTopCard().rank);
            d = c.compareTo(hand.getTopCard());
            if(d == 1){
            return true;
            }else if(d == -1){
            return false;
            }
        }else if(this.size() == 5){
            for(int i = 0;i<5;i++){
                if(this.getType() == count[i]){
                    tidx = i;
                }
                if(hand.getType() == count[i]){
                    hidx = i;
                }
            }
            if(tidx > hidx){
                return true;
            }else{
                return false;
            }
        }

        return false;
    }
/**
 * Checking if this is a valid hand
 * @return whether this is a valid hand
 */
    public abstract boolean isValid();
/**
 * Returning a string specifying the type of this hand
 * @return the type of this hand
 */
    public abstract String getType();


}
