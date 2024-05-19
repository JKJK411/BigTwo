import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * This class is the GUI for the bigtwo game which implement CardgameUI
 * 
 * @author Tang Tsz Kit
 */
public class BigTwoGUI implements CardGameUI{
    private BigTwo game;
    private boolean[] selected = new boolean[52];
    private int activePlayer;
    private ArrayList<JLabel> playerprofile;
    private ArrayList<JLabel> handcard;
    private JFrame frame;
    private JLabel msglabel;
    private JButton playButton;
    private JButton passButton;
    private JButton connectButton;
    private JButton quitButton;
    private JTextArea msgArea;
    private JTextArea chatArea;
    private JTextField chatInput;
    private BigTwoPanel btp = new BigTwoPanel();
    private JPanel board ;
    private JPanel p1 ;
    private JPanel p2;
    private JPanel p3 ;
    private JPanel p0 ;
    private JPanel outside;
    private JPanel msg;
    private JPanel top ;
    private JPanel bot ;
    private JPanel botmid ;
    private boolean active;
    private int localplayerid;
/**
 * This is a constructor of BigTwoGUI using a BigTwo game, it will create JPanel of four area, top, board, bot and msg and a main frame
 * , alse create a inner class BigTwoPanel btp for GUI desige and hold all the other JPanel. There are also a JPanel for each player, and two JTestArea for the message and chatroom. 
 * 
 * @param game the Bigtwo game
 */
    public BigTwoGUI(BigTwo game){
        this.game = game;
        activePlayer = game.getCurrentPlayerIdx();
        this.board = new JPanel();
        this.p1 = new JPanel();
        this.p2 = new JPanel();
        this.p3 = new JPanel();
        this.p0 = new JPanel();
        this.outside = new JPanel();
        this.msg = new JPanel();
        this.top = new JPanel();
        this.bot = new JPanel();
        this.botmid = new JPanel();
        this.frame = new JFrame("BigTwo");
        chatArea = new JTextArea("Chat: \n",10,25);
        msgArea = new JTextArea("Message: \n",10,25);
        msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
        bot.setLayout(new BorderLayout());
        botmid.setLayout(new BorderLayout());
        board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
        p0.setLayout(new GridBagLayout());
        p1.setLayout(new GridBagLayout());
        p2.setLayout(new GridBagLayout());
        p3.setLayout(new GridBagLayout());
        outside.setLayout(new FlowLayout(FlowLayout.LEFT));
        top.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.btp.setLayout(new BorderLayout());
        resetSelected();
        this.active = true;
    }
    /**
     * The setter of active player
     */

    public void setActivePlayer(int activePlayer){
		if (activePlayer < 0 || activePlayer >= 4) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
    }

    /**
     * Refresh the GUI by removing and adding the board again.
     */
    public void repaint(){
        this.btp.remove(board);
        this.btp.add(board,BorderLayout.WEST);
    }
    /**
     * Print out the message on the msgArea
     */
    public void printMsg(String msg){
        this.msgArea.append(msg+"\n");
    }
    /**
     * Not used in the program
     */
    public void clearMsgArea(){

    }
/**
 * Append the chat into the textbox.
 */
    public void appendChat(){
        String text = chatInput.getText();
        chatArea.append(game.getPlayerList().get(localplayerid).getName()+": "+text);
        chatArea.append("\n");
        chatInput.setText("");
    }
/**
 * A method of setting the player index of local player
 * @param idx the player index of local player
 */
    public void setLocalPlayerID(int idx){
        this.localplayerid = idx;
    }
/**
 * A method of getting the player index of local player
 * @return the player index of local player
 */
    public int getLocalPlayID(){
        return this.localplayerid;
    }
    /**
    * Remove all the element inside the GUI, reset the data in game, and start the game with the brand new GUI and data.
    */
    public void reset(){
        frame.remove(this.btp);
        this.btp.removeAll();
        this.board.removeAll();
        this.p1.removeAll();
        this.p2.removeAll();
        this.p3.removeAll();
        this.p0.removeAll();
        this.outside.removeAll();
        this.msg.removeAll();;
        this.top.removeAll();
        this.bot.removeAll();
        this.botmid.removeAll();
        chatArea.removeAll();
        msgArea.removeAll();
        this.handcard.removeAll(this.handcard);
        BigTwoDeck deck = new BigTwoDeck();
        deck.initialize();
        deck.shuffle();
        this.game.start(deck);
        this.activePlayer = this.game.getCurrentPlayerIdx();
    }
    /**
     * Enable the play and pass button
     */
    public void enable(){
        this.active = true;
    }
    /**
     * Disable the play and pass button
     */
    public void disable(){
        this.active = false;
    }
    /**
    * Reset the selected which indicate which card in the players hand is selected 
    */
    public void resetSelected(){
        for(int i =0;i<this.selected.length;i++){
            selected[i] = false;
        }
    }
    /**
     * Not used in the program
     */
    public void promptActivePlayer(){
    }
    /**
     * Create a brand new JPanel for the GUI and display the GUI, used at the start of the BigTwo game.
     */
    public void createnewpanel() {
        btp.createpanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,800);
        frame.setVisible(true);
    }
    /**
     * Create and print the JPanel board which contain the cards and handsOnTable. This program will first remove all the existing element in the board. and then put the player icon, player name
     * and card image into each player's JPanel, which are p0, p1, p2 and p3. And then adding them into the JPanel board to refresh and display the handcard of the local play and card back of non-local player.
     * The localplayer's card will also be added with actionlistener to let the player choose it by clicking.
     */
    public void printboard(){
        board.removeAll();
        p0.removeAll();
        p1.removeAll();
        p2.removeAll();
        p3.removeAll();
        outside.removeAll();
        p0.setLayout(new GridBagLayout());
        p1.setLayout(new GridBagLayout());
        p2.setLayout(new GridBagLayout());
        p3.setLayout(new GridBagLayout());
        board.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints c = new GridBagConstraints();
            try {
                playerprofile = new ArrayList<>();
                handcard = new ArrayList<JLabel>();
                for(int i = 0;i<game.getNumOfPlayers();i++){
                    c.insets = new Insets(0,0,0,0);
                    String path = "image/"+i+".jpg";
                    BufferedImage image = ImageIO.read(new File(path));
                    Image dimg = image.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                    JLabel pic = new JLabel(new ImageIcon(dimg));
                    playerprofile.add(pic);
                    switch(i){
                        case 0:
                            c.anchor = GridBagConstraints.NORTHWEST;
                            p0.add(new JLabel(game.getPlayerList().get(i).getName()),c);
                            c.anchor = GridBagConstraints.SOUTHWEST;
                            c.insets = new Insets(0,0,0,70);
                            p0.add(pic,c);
                            break;
                        case 1:
                            c.anchor = GridBagConstraints.NORTHWEST;
                            p1.add(new JLabel(game.getPlayerList().get(i).getName()),c);
                            c.anchor = GridBagConstraints.SOUTHWEST;
                            c.insets = new Insets(0,0,0,70);
                            p1.add(pic,c);
                            break;
                        case 2:
                            c.anchor = GridBagConstraints.NORTHWEST;
                            p2.add(new JLabel(game.getPlayerList().get(i).getName()),c);
                            c.anchor = GridBagConstraints.SOUTHWEST;
                            c.insets = new Insets(0,0,0,70);
                            p2.add(pic,c);
                            break;
                        case 3:
                            c.anchor = GridBagConstraints.NORTHWEST;
                            p3.add(new JLabel(game.getPlayerList().get(i).getName()),c);
                            c.anchor = GridBagConstraints.SOUTHWEST;
                            c.insets = new Insets(0,0,0,70);
                            p3.add(pic,c);
                            break;
                    }
                    for (int j = 0;j<game.getPlayerList().get(i).getNumOfCards();j++){
                        c.insets = new Insets(0,-40,0,0);
                        path = "image/back.png";
                        if(i == localplayerid){
                            int suit = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
                            int rank = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
                            path = "image/"+suit+"_"+rank+".png";
                        }
                        image = ImageIO.read(new File(path));
                        dimg = image.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                        pic = new JLabel(new ImageIcon(dimg));
                        if(i == localplayerid){
                            handcard.add(pic);
                        }
                        pic.setOpaque(false);
                        if(i == localplayerid){
                        pic.addMouseListener(new SelectListener());
                        }
                        switch(i){
                            case 0:
                                p0.add(pic,c);
                                break;
                            case 1:
                                p1.add(pic,c);

                                break;
                            case 2:
                                p2.add(pic,c);

                                break;
                            case 3:
                                p3.add(pic,c);
                                break;
                        }
                    }
                }
                if(game.getHandsOnTable().size() == 0){
                    BufferedImage image = ImageIO.read(new File("image/back.png"));
                    Image dimg = image.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                    JLabel pic = new JLabel(new ImageIcon(dimg));
                    outside.add(pic);
                }else{
                    Hand hand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
                    for(int i = 0;i<hand.size();i++){
                        int suit = hand.getCard(i).getSuit();
                        int rank = hand.getCard(i).getRank();
                        String path = "image/"+suit+"_"+rank+".png";
                        BufferedImage image = ImageIO.read(new File(path));
                        Image dimg = image.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                        JLabel pic = new JLabel(new ImageIcon(dimg));
                        outside.add(pic);
                    }
                }
            } catch (IOException e) {
                System.out.print("something wrong");
            }
            board.add(p0);
            board.add(p1);
            board.add(p2);
            board.add(p3);
            board.add(outside);
            board.revalidate();
        }
    /**
     * This inner is the JPanel used for GUI design which contain all of other JPanel, 2 Button is added at the JPanel bot which is at the South for play the card or pass the turn. msgArea and chatArea 
     * is added to the East side. top contains two JButton to connect or quit the game. board is added to the West side to display the cards and game progress.
     */ 
    class BigTwoPanel extends JPanel{
        private void createpanel(){
            playButton = new JButton("Play");
            playButton.addActionListener(new PlayButtonListener());
    
            passButton = new JButton("Pass");
            passButton.addActionListener(new PassButtonListener());
    
            chatInput = new JTextField(25);
            chatInput.addActionListener(new ChatListener());
            msglabel = new JLabel("Message:");

            chatArea.setSize(5,10);
            chatArea.setBackground(Color.GREEN);
            chatArea.setEditable(false);
            msgArea.setEditable(false);
            JScrollPane scroll1 = new JScrollPane(chatArea);
            JScrollPane scroll2 = new JScrollPane(msgArea);
            msgArea.setBackground(Color.YELLOW);
            msg.add(scroll2);
            msg.add(scroll1);

            bot.add(playButton,BorderLayout.WEST);
            bot.add(chatInput,BorderLayout.EAST);
            botmid.add(passButton,BorderLayout.WEST);
            botmid.add(msglabel,BorderLayout.EAST);
            bot.add(botmid,BorderLayout.CENTER);


            connectButton = new JButton("Connect");
            connectButton.addActionListener(new ConnectListener());
            quitButton = new JButton("Quit");
            quitButton.addActionListener(new QuitMenuItemListener());
            top.add(connectButton,BorderLayout.WEST);
            top.add(quitButton,BorderLayout.WEST);

            printboard();
            btp.add(top,BorderLayout.NORTH);
            btp.add(msg,BorderLayout.EAST);
            btp.add(bot,BorderLayout.SOUTH);
            btp.add(board,BorderLayout.WEST);
            frame.pack();
            frame.add(btp);
    
        }
    }
    /**
     * This actionlistener is for the JTextField chatInput which is used for add text from it to the chatArea. The text will add to the chatArea and remove from the JTextField.
     */
    class ChatListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            appendChat();
        }
        
    }
    /**
     * This action listener is for the play button. It will determine which cards is selected by the activeplayer from its hand using selected. And then play the cards, reset the selected card and set new active player.
     */
    class PlayButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(active){
            int count = 0;
            for(int i = 0;i<selected.length;i++){
                if(selected[i] == true){
                    count++;
                }
            }
            if(count != 0){
                int[] cardIdx = new int[count];
                count = 0;
                for(int i = 0;i<selected.length;i++){
                    if(selected[i] == true){
                        cardIdx[count] = i;
                        count++;
                    }
                }
                game.makeMove(activePlayer,cardIdx);
                resetSelected();
                activePlayer = game.getCurrentPlayerIdx();
            }else{
                int[] cardIdx = null;
                game.makeMove(activePlayer,cardIdx);
                resetSelected();
                activePlayer = game.getCurrentPlayerIdx();
            }
            }
        }

    }
    /**
     * This action listener ables player to choose the card by clicking the card. The card will rise for a little bit to indicate that card is choosen, 
     * clicking the rised card will cancel it and move it back to its original height.
     * 
     */
    class SelectListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e){
            for(int i = 0;i<handcard.size();i++){
                if((JLabel)e.getSource() == handcard.get(i)){
                    if(selected[i] == false){
                        selected[i] = true;
                    }else{
                        selected[i] = false;
                    }
                    break;
                }
            }
            GridBagConstraints d = new GridBagConstraints();
            d.anchor = GridBagConstraints.NORTHWEST;
            d.insets = new Insets(0,0,0,0);
            switch (localplayerid){
                case 0:
                    p0.removeAll();
                    p0.add(new JLabel(game.getPlayerList().get(localplayerid).getName()),d);
                    d.anchor = GridBagConstraints.WEST;
                    d.insets = new Insets(0,0,0,70);
                    p0.add(playerprofile.get(localplayerid),d);
                    for(int i = 0;i<handcard.size();i++){
                       d.insets = new Insets(0,-40,0,0);
                        if(selected[i] == true){
                            d.insets = new Insets(0,-40,50,0);
                        }
                        p0.add(handcard.get(i),d);
                    }
                    p0.revalidate();
                    break;
                case 1:
                    p1.removeAll();
                    p1.add(new JLabel(game.getPlayerList().get(localplayerid).getName()),d);
                    d.anchor = GridBagConstraints.WEST;
                    d.insets = new Insets(0,0,0,70);
                    p1.add(playerprofile.get(localplayerid),d);
                    for(int i = 0;i<handcard.size();i++){
                        d.insets = new Insets(0,-40,0,0);
                        if(selected[i] == true){
                            d.insets = new Insets(0,-40,50,0);
                        }
                        p1.add(handcard.get(i),d);
                    }
                    p1.revalidate();
                    break;
                case 2:
                    p2.removeAll();
                    p2.add(new JLabel(game.getPlayerList().get(localplayerid).getName()),d);
                    d.anchor = GridBagConstraints.WEST;
                    d.insets = new Insets(0,0,0,70);
                    p2.add(playerprofile.get(localplayerid),d);
                    for(int i = 0;i<handcard.size();i++){
                    d.insets = new Insets(0,-40,0,0);
                    if(selected[i] == true){
                        d.insets = new Insets(0,-40,50,0);
                    }
                    p2.add(handcard.get(i),d);
                    }
                    p2.revalidate();
                    break;
                case 3:
                    p3.removeAll();
                    p3.add(new JLabel(game.getPlayerList().get(localplayerid).getName()),d);
                    d.anchor = GridBagConstraints.WEST;
                    d.insets = new Insets(0,0,0,70);
                    p3.add(playerprofile.get(localplayerid),d);
                    for(int i = 0;i<handcard.size();i++){
                    d.insets = new Insets(0,-40,0,0);
                    if(selected[i] == true){
                        d.insets = new Insets(0,-40,50,0);
                    }
                    p3.add(handcard.get(i),d);
                    }
                    p3.revalidate();
                    break;
            }
            board.removeAll();
            board.add(p0);
            board.add(p1);
            board.add(p2);
            board.add(p3);
            board.add(outside);
            repaint();

        }

    /**
     * Not used in the program
     */
        @Override
        public void mousePressed(MouseEvent e) {
            
        }
    /**
     * Not used in the program
     */
        @Override
        public void mouseReleased(MouseEvent e) {

        }
    /**
     * Not used in the program
     */
        @Override
        public void mouseEntered(MouseEvent e) {
            
        }
    /**
     * Not used in the program
     */
        @Override
        public void mouseExited(MouseEvent e) {
            
        }

    }
    /**
     * This action listener is for the pass button, it will input a null array into the makemove to perform a pass of a turn.
     */
    class PassButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(active){
                int[] cardIdx = null;
                game.makeMove(activePlayer, cardIdx);
                resetSelected();
                activePlayer = game.getCurrentPlayerIdx();
            }
        }
    }
    /**
     * This action listener is for the reset button, which will call the reset function once it is pressed, it will refresh GUI and start a new game.
     */
    class ConnectListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            game.connectbybutton();
        }
    }
    /**
     * This action listener is for the quit button, which will quit the program and close the GUI.
     */
    class QuitMenuItemListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            System.exit(1);
        }
    }


    
}
