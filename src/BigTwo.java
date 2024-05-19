import java.util.*;

import javax.swing.JOptionPane;
/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game.
 * 
 * @author Tang Tsz Kit 
 */
public class BigTwo implements CardGame{
    private int numOfPlayers;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private BigTwoClient client;
    private int maxplayer = 4;
/** 
 * This is a constructor for creating a Big Two card game. Create 4 players and add them to the player list, a BigTwoUI and the client.
*/
    public BigTwo(){
        this.playerList = new ArrayList<CardGamePlayer>();
        this.numOfPlayers = 0;
        for(int i = 0;i<4;i++){
            this.playerList.add(new CardGamePlayer(""));
        }
        this.handsOnTable = new ArrayList<Hand>();
        this.ui = new BigTwoGUI(this);
        this.client = new BigTwoClient(this, this.ui);
    }
/**
 * Getter of number of players playing the game
 * 
 * @return The number of players playing the game
 */
    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }
/**
 * Retrieving the deck of cards being used
 * 
 * @return the deck of cards being used
 */
    public Deck getDeck(){
        return this.deck;
    }
/**
 * Retrieving the list of players
 * 
 * @return the list of players
 */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return this.playerList;
    }
/**
 * Retrieving the list of hands played on the table
 * 
 * @return the list of hands played on the table
 */
    public ArrayList<Hand> getHandsOnTable(){
        return this.handsOnTable;
    }
/**
 * Retrieving the index of the current player
 * 
 * @return the index of the current player
 */
    public int getCurrentPlayerIdx(){
        return this.currentPlayerIdx;
    }
/**
 * (i) distribute the cards to the players; (ii) identify the player who holds the Three of Diamonds; 
 * (iii) set both the currentPlayerIdx of the BigTwo object and the activePlayer of the BigTwoGUI object to the index of the player who holds the Three of Diamonds; (iv) start game and refresh GUI
 */
    public void start(Deck deck){
        this.handsOnTable.clear();
        this.deck = deck;
        for(int i = 0;i<getNumOfPlayers();i++){
            this.playerList.get(i).removeAllCards();
        }
        for(int i = 0;i<52;i++){
            this.playerList.get(i%4).addCard(this.deck.getCard(i));
            if(this.deck.getCard(i).suit == 0 && this.deck.getCard(i).rank == 2){
                this.currentPlayerIdx = i%4;
            }
        }
        for(int i = 0;i<4;i++){
            this.playerList.get(i).sortCardsInHand();
            for(int j = 0;j<8;j++){
                if(this.playerList.get(i).getCardsInHand().getCard(0).rank < 2){
                    Card temp = this.playerList.get(i).getCardsInHand().getCard(0);
                    this.playerList.get(i).getCardsInHand().removeCard(temp);
                    this.playerList.get(i).getCardsInHand().addCard(temp);
                }
            } 
        }
        this.ui.setActivePlayer(this.currentPlayerIdx);
        this.ui.printboard();
        if(this.currentPlayerIdx != this.ui.getLocalPlayID()){
            this.ui.disable();
        }else{
            this.ui.enable();
        }
    }
/**
 * Making a move by a player with the specified index using the cards specified by the list of indices
 * When a player play a move, a MOVE message will be sent
 * @param playerIdx the player making the move
 * @param cardIdx the hand of the player played
 */
    public void makeMove(int playerIdx, int[] cardIdx){
        GameMessage msg;
        if (cardIdx == null && this.handsOnTable.size() != 0){
            if(this.handsOnTable.get(this.handsOnTable.size()-1).getPlayer().equals(this.playerList.get(playerIdx))){
                this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" Not a legal move!!!");
            }else{
                this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" has Passed");
                msg = new CardGameMessage(6, -1, cardIdx);
                this.client.sendMessage(msg);
                this.currentPlayerIdx = (currentPlayerIdx +1)%4;
                if(this.currentPlayerIdx != this.ui.getLocalPlayID()){
                    this.ui.disable();
                }else{
                    this.ui.enable();
                }
            }
        }else if(cardIdx != null){
            CardList list = this.playerList.get(playerIdx).play(cardIdx);
            if(composeHand(this.playerList.get(playerIdx), list) != null){
                if(handsOnTable.size() == 0){
                    Card d3 = new Card(0,2);
                    if(!list.contains(d3)){
                        this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" Not a legal move!!!");
                    }else{
                        checkMove(playerIdx, cardIdx);
                        this.handsOnTable.add(composeHand(this.playerList.get(playerIdx), list));
                        this.playerList.get(playerIdx).removeCards(list);
                        this.currentPlayerIdx = (currentPlayerIdx +1)%4;
                        msg = new CardGameMessage(6, -1, cardIdx);
                        this.client.sendMessage(msg);
                        if(this.currentPlayerIdx != this.ui.getLocalPlayID()){
                            this.ui.disable();
                        }else{
                            this.ui.enable();
                        }
                    }
                }else{
                    checkMove(playerIdx, cardIdx);
                    if(composeHand(this.playerList.get(playerIdx), list).beats(this.handsOnTable.get(this.handsOnTable.size()-1))){
                        this.handsOnTable.add(composeHand(this.playerList.get(playerIdx), list));
                        this.playerList.get(playerIdx).removeCards(list);
                        this.currentPlayerIdx = (currentPlayerIdx +1)%4;
                        msg = new CardGameMessage(6, -1, cardIdx);
                        this.client.sendMessage(msg);
                        if(this.currentPlayerIdx != this.ui.getLocalPlayID()){
                            this.ui.disable();
                        }else{
                            this.ui.enable();
                        }
                    }else{
                        this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" Not a legal move!!!");
                    }
                }
            }else{
                    this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" Not a legal move!!!");
            }
        }else{
            this.ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" Not a legal move!!!");
        }

        if(endOfGame() == false){
            this.ui.setActivePlayer(this.currentPlayerIdx);
            this.ui.printboard();
            this.ui.repaint();
        }else{
            this.ui.printMsg("Game ends");
            String line = "";
            line += "Game ends"+'\n';
            for(int i = 0;i<4;i++){
                if(this.playerList.get(i).getCardsInHand().size() != 0){
                    this.ui.printMsg("Player "+i+" has "+this.playerList.get(i).getCardsInHand().size()+" cards in hand.");
                    line += "Player "+i+" has "+this.playerList.get(i).getCardsInHand().size()+" cards in hand."+'\n';
                }else{
                    this.ui.printMsg("Player "+i+" wins the game.");
                    line += "Player "+i+" wins the game."+'\n';
                }
            }
            this.ui.disable();
            JOptionPane.showMessageDialog(null,line);
        }

    }
/**
 * Checking a move made by a player
 * 
 * @param playerIdx the player making the move
 * @param cardIdx the hand of the player played
 */
    public void checkMove(int playerIdx, int[] cardIdx){
        CardGamePlayer cplayer = this.playerList.get(playerIdx);
        CardList list = this.playerList.get(playerIdx).play(cardIdx);
        Single sg = new Single(cplayer,list);
        Pair pr = new Pair(cplayer, list);
        Triple tp = new Triple(cplayer, list);
        Straight st = new Straight(cplayer, list);
        Flush fl = new Flush(cplayer, list);
        FullHouse fh = new FullHouse(cplayer, list);
        Quad qd = new Quad(cplayer, list);
        StraightFlush sf = new StraightFlush(cplayer, list);
        Hand[] hand = new Hand[8];
        hand[0] = sg;
        hand[1] = pr;
        hand[2] = tp;
        hand[3] = st;
        hand[4] = fl;
        hand[5] = fh;
        hand[6] = qd;
        hand[7] = sf;
        for(int i = 7;i>=0;i--){
            if(hand[i].isValid()){
                if(this.handsOnTable.size() == 0){
                    ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" {"+hand[i].getType()+"}"+" "+hand[i].toString());
                    break;
                }else{
                    if(hand[i].beats(this.handsOnTable.get(this.handsOnTable.size()-1))){
                        ui.printMsg(this.playerList.get(this.currentPlayerIdx).getName()+" {"+hand[i].getType()+"}"+" "+hand[i].toString());
                        break;
                    }
                }
            }
        }
    }
/**
 * Checking if the game ends
 */
    public boolean endOfGame(){
        boolean sw = false;
        for(int i = 0;i<4;i++){
            if(this.playerList.get(i).getNumOfCards() == 0){
                sw = true;
            }
        }
        return sw;
    }
/**
 * Starting a Big Two card game
 * @param arg not used
 */
    public static void main(String[] arg){
        BigTwo bigtwo = new BigTwo();

    }
/**
 * Returning a valid hand from the specified list of cards of the player
 * @param player player who made the play
 * @param cards the hand the player just played out
 * @return verified hand the player just played out 
 */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
        Single sg = new Single(player,cards);
        Pair pr = new Pair(player,cards);
        Triple tp = new Triple(player, cards);
        Straight st = new Straight(player, cards);
        Flush fl = new Flush(player, cards);
        FullHouse fh = new FullHouse(player, cards);
        Quad qd = new Quad(player, cards);
        StraightFlush sf = new StraightFlush(player, cards);
        Hand[] hand = new Hand[8];
        hand[0] = sg;
        hand[1] = pr;
        hand[2] = tp;
        hand[3] = st;
        hand[4] = fl;
        hand[5] = fh;
        hand[6] = qd;
        hand[7] = sf;
        for(int i = 7;i>=0;i--){
            if(hand[i].isValid()){
                return hand[i];
            }
        }
        return null;
    }
/**
 * To aid the connect button in the GUI
 */
    public void connectbybutton(){
        this.client.connect();
    }
/**
 * getter of the GUI
 * @return gui
 */
    public BigTwoGUI getGUI(){
        return this.ui;
    }
/**
 * Increase the number of player in the game by 1
 */
    public void addoneplayer(){
        this.numOfPlayers += 1;
    }
/**
 * Reduce the number of player in the game by 1
 */
    public void minusoneplayer(){
        this.numOfPlayers -= 1;
    }
/**
 * Setter of number of player
 */
    public void setnumofplayer(int num){
        this.numOfPlayers = num;
    }
}
