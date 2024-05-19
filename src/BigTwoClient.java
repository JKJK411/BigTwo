import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.JOptionPane;
/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to model a Big Two game client that is responsible for establishing a connection and communicating with the Big Two game server. Below is a detailed description for the BigTwoClient class.
 * 
 * @author Tang Tsz Kit 
 */
public class BigTwoClient implements NetworkGame{
    private ArrayList<CardGamePlayer> playerList;
    private String serverIP = "127.0.0.1";
    private Socket sock = null;
    private int serverPort = 2396;
    private int playerID;
    private String playerName;
    private BigTwoGUI gui;
    private BigTwo game;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
/**
 * A constructor for creating a Big Two client. The first parameter is a reference to a BigTwo object associated with this client and the second parameter is a reference to a BigTwoGUI object associated the BigTwo object.
 */
    public BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
        String name = JOptionPane.showInputDialog("Enter your name");
        setPlayerName(name);
        connect();
    }
/**
 * A method for setting the playerID (i.e., index) of the local player
 */
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }
/**
 * A method for getting the playerID (i.e., index) of the local player
 */
    public int getPlayerID(){
        return this.playerID;
    }

/**
 * A method for getting the name of the local player.
 */
	public String getPlayerName(){
        return this.playerName;
    }

/**
 * A method for setting the name of the local player.
 */
	public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

/**
 * A method for getting the IP of the game server
 */
	public String getServerIP(){
        return this.serverIP;
    }

/**
 * A method for setting the IP of the game server
 */
	public void setServerIP(String serverIP){
        this.serverIP = serverIP;
    }
/**
 * A method for getting the TCP port of the game server
 */
	public int getServerPort(){
        return this.serverPort;
    }
/**
 * A method for setting the TCP port of the game server
 */
	public void setServerPort(int serverPort){
        this.serverPort = serverPort;
    }
/**
 * A method for making a socket connection with the game server.
 * (i) create an ObjectOutputStream for sending messages to the game server; 
 * (ii) create a new thread for receiving messages from the game server.
 */
    public void connect() {
        try {
            sock = new Socket(getServerIP(), getServerPort());
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());

            Thread thread = new Thread(new ServerHandler());
            thread.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.gui.createnewpanel();
    }
/**
 * A method for parsing the messages received from the game server. This method should be called from the thread responsible for receiving messages from the game server based on the message type.
 */
	public void parseMessage(GameMessage message){
        GameMessage msg;
        switch (message.getType()){
            case 0:
                String[] playerlist = (String[]) message.getData();
                this.game.setnumofplayer(0);
                for(int i = 0;i<4;i++){
                    this.game.getPlayerList().get(i).setName(playerlist[i]);
                    if(playerlist[i] != null){
                        this.game.addoneplayer();
                    }
                }
                setPlayerID(message.getPlayerID());
                this.game.getPlayerList().get(playerID).setName(playerName);
                this.gui.setLocalPlayerID(this.playerID);
                msg = new CardGameMessage(1, -1, this.playerName);
                this.game.addoneplayer();
                sendMessage(msg);
                this.gui.printboard();
                this.gui.repaint();
                break;
            
            case 1:
                if(message.getPlayerID() == getPlayerID()){
                    msg = new CardGameMessage(4, -1, null);
                    sendMessage(msg);
                }else{
                    this.game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
                    this.game.addoneplayer();
                }
                this.gui.printboard();
                this.gui.repaint();
                break;

            case 2:
                this.gui.printMsg("The server is full");
                this.gui.printboard();
                this.gui.repaint();
                break;

            case 3:
                if(game.endOfGame() == false){
                    this.gui.disable();
                }else{
                    this.game.getPlayerList().get(message.getPlayerID()).setName("");
                    this.game.minusoneplayer();
                }
                this.gui.printboard();
                this.gui.repaint();
                break;
            
            case 4:
                this.gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName()+" is Ready!");
                break;

            case 5:
                this.game.start((BigTwoDeck)message.getData());
                this.gui.printboard();
                this.gui.repaint();
                break;
            
            case 6:
                this.game.makeMove(message.getPlayerID(), (int[])message.getData());
                this.gui.printboard();
                this.gui.repaint();
                break;
            
            case 7:
                this.gui.printMsg((String)message.getData());
                break;
                
        }


        setPlayerID(message.getPlayerID());
    }
/**
 * A method for sending the specifiedmessage to the game server
 */
	public void sendMessage(GameMessage message){
        try{
            this.oos.writeObject(message);
            this.oos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
/**
 * An inner class that implements the Runnable interface used for receiving message from the game server
 */
    class ServerHandler implements Runnable{
    @Override
        public void run(){
            CardGameMessage message;
            try{
                while((message =  (CardGameMessage)ois.readObject())!= null){
                    parseMessage(message);
                }
            }catch(IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
