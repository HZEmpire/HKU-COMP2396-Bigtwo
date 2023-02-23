import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to model 
 * a Big Two game client that is responsible for establishing a connection and 
 * communicating with the Big Two game server.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class BigTwoClient implements NetworkGame{

    /**
     *  Constructor for creating a Big Two client. The first parameter is a reference to a
     *  BigTwo object associated with this client
     *  and the second parameter is a reference to a BigTwoGUI object associated the BigTwo object.
     *
     * @param game The game linked to the Client.
     * @param gui The GUI linked to the Client.
     */
    public BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
        this.gui.setClient(this);
        this.setPlayerID(-1);
        String nameget = JOptionPane.showInputDialog(null, "Please enter your username: ", "");
        if(nameget == null || nameget.isEmpty() || nameget.equals("")){
            //Default name when input is empty.
            String [] defaultname = {"Van Darkholme", "Paratrooper One", "Ling God", "Brother Wuying"};
            String defaultnameused = defaultname[(int) (Math.random() * defaultname.length)] + " " + (int)(Math.random()*20);
            setPlayerName(defaultnameused);
        }
        else
            setPlayerName(nameget);

        setServerIP("127.0.0.1");
        setServerPort(2396);

        //Below procedures are used for connecting the server.
        connect();
    }

    private BigTwo game; // a BigTwo object for the Big Two card game.
    private BigTwoGUI gui; // a BigTwoGUI object for the Big Two card game.
    private Socket sock; // a socket connection to the game server.
    private ObjectOutputStream oos; // an ObjectOutputStream for sending messages to the server.
    private ObjectInputStream ois; // an ObjectInputStream for receiving messages from the server.
    private int playerID; // an integer specifying the playerID (i.e., index) of the local player.
    private String playerName; // a string specifying the name of the local player.
    private String serverIP; // a string specifying the IP address of the game server.
    private int serverPort; // an integer specifying the TCP port of the game server.

    /**
     * Method for judging if this client is already connected server.
     *
     * @return True for connect and false otherwise.
     */
    public boolean connectStatus(){
        if(sock == null || sock.isClosed())
            return false;
        else
            return true;
    }

    /**
     * Method for getting the playerID (i.e., index) of the local player.
     *
     * @return The ID of the current player.
     */
    @Override
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Method for setting the playerID (i.e., index) of
     * the local player. This method should be called from the parseMessage() method when a
     * message of the type PLAYER_LIST is received from the game server.
     *
     * @param playerID The ID of the current player.
     */
    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
        this.gui.setUiplayer(playerID);
    }


    /**
     * Method for getting the name of the local player.
     *
     * @return The username.
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Method for setting the name of the local player.
     *
     * @param playerName The username.
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Method for getting the IP address of the game server.
     *
     * @return The server IP address.
     */
    @Override
    public String getServerIP() {
        return this.serverIP;
    }

    /**
     * Method for setting the IP address of the game server.
     *
     * @param serverIP The server IP address.
     */
    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * Method for getting the TCP port of the game server.
     *
     * @return The TCP port of the server.
     */
    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * Method for setting the TCP port of the game server.
     *
     * @param serverPort The TCP port of the server.
     */
    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Method for making a socket connection with the game server.
     * Upon successful connection,
     * (i) create an ObjectOutputStream for sending messages to the game server;
     * (ii) create a new thread for receiving messages from the game server.
     *
     */
    @Override
    public void connect() {
        try {
            sock = new Socket(getServerIP(), getServerPort());
            oos = new ObjectOutputStream(sock.getOutputStream());

            Runnable connection = new ServerHandler();
            Thread connectingthread = new Thread(connection);
            connectingthread.start();

            sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, getPlayerName()));

        }catch (Exception ex){
            gui.printMsg("The Server is not running now.\nPlease try again when the server is ready.\n\n");
            sock = null;
            oos = null;
            gui.repaint();
            ex.printStackTrace();
        }

    }

    /**
     * Method for parsing the messages received from the game server.
     * This method should be called from the thread responsible for receiving messages from the game server.
     * Based on the message type, different actions will be carried out
     *
     * @param message The message need to be handled.
     */
    @Override
    public void parseMessage(GameMessage message) {
        switch (message.getType()){
            case CardGameMessage.PLAYER_LIST:
                //Set the list of players
                setPlayerID(message.getPlayerID());
                for(int i = 0; i < game.getNumOfPlayers(); i++){
                    if ( ( (String[])message.getData() )[i] != null){
                        game.getPlayerList().get(i).setName( ( (String[])message.getData() )[i] );
                    }
                }
                gui.repaint();
                break;

            case CardGameMessage.JOIN:
                //Set one certain player
                if (  (String)message.getData()  != null)
                    game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
                gui.printMsg("Player " + message.getPlayerID() + " " + game.getPlayerList().get(message.getPlayerID()).getName() + " join the game.\n");
                if(message.getPlayerID() == this.playerID)
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                gui.repaint();
                break;

            case CardGameMessage.FULL:
                //When the server is full
                gui.printMsg("\nCurrently the server is full.\nPlease connect later.\n\n");
                this.setPlayerID(-1);
                try {
                    sock.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                gui.repaint();
                break;

            case CardGameMessage.QUIT:
                //When someone quit the game.
                gui.printMsg("Player " + message.getPlayerID() + " " + game.getPlayerList().get(message.getPlayerID()).getName() + " left the game.\n");
                game.getPlayerList().get(message.getPlayerID()).setName("");
                for(int i = 0; i < 4; i++)
                    game.getPlayerList().get(i).removeAllCards();
                if(!game.endOfGame())
                    gui.disable();
                sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                gui.repaint();
                break;

            case CardGameMessage.READY:
                //When someone is ready for game.
                gui.printMsg("Player " + message.getPlayerID() + " " + game.getPlayerList().get(message.getPlayerID()).getName() + " is Ready for game.\n");
                break;

            case CardGameMessage.START:
                //When the game is ready for play.
                gui.printMsg("Game starts\n\n");
                gui.enable();
                game.start((Deck)message.getData());
                break;

            case CardGameMessage.MOVE:
                //When someone made move.
                game.checkMove(message.getPlayerID(), (int[]) message.getData());
                break;

            case CardGameMessage.MSG:
                //When someone input the message.
                gui.printChat((String)message.getData() );
                break;

            default:
                //Wrong message.
                break;
        }
    }

    /**
     * Method for sending the specified message to the game server. This method should be called
     * whenever the client wants to communicate with the game server or other clients.
     *
     * @param message The message need to send.
     */
    @Override
    public void sendMessage(GameMessage message) {
        try{
            oos.writeObject(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * An inner class that implements the Runnable interface. Implementing the run() method from the
     * Runnable interface and create a thread with an instance of this class as its job in connect() method
     * from the NetworkGame interface for receiving messages from the game server.
     * Upon receiving a message, the parseMessage() method from the NetworkGame interface will be called to parse the
     * messages accordingly.
     *
     */
    class ServerHandler implements Runnable{
        @Override
        public void run() {
            try {
                ois = new ObjectInputStream(sock.getInputStream());
                while (! sock.isClosed()){
                    CardGameMessage received = (CardGameMessage) (ois.readObject());
                    if(received != null)
                        parseMessage(received);
                }
                ois.close();
            }catch (Exception ex){
                ois = null;
                ex.printStackTrace();
            }
        }
    }
}
