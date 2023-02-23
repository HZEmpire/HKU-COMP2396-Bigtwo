import javax.swing.*;
import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card 
 * game. It has private instance variables for storing the number of players, a deck of cards, 
 * a list of players, a list of hands played on the table, an index of the current player,
 * and a user interface.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class BigTwo implements CardGame{

    /**
     * Constructor for creating a Big Two card game. which create 4 players and
     * add them to the player list; and it create a BigTwoGUI object for providing the user interface.
     * 
     */
    public BigTwo(){
        this.numOfPlayers = 4;
        for (int i = 0; i < this.numOfPlayers; i++)
        {
            this.playerList.add(new CardGamePlayer(""));
        }

        this.ui = new BigTwoGUI(this);
        this.client = new BigTwoClient(this, this.ui);
        this.ui.setClient(this.client);
    }

    private int numOfPlayers; //The number of players.
    private Deck deck; //The shuffled deck used for this game.
    private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>(); //The list of game players.
    private ArrayList<Hand> handsOnTable = new ArrayList<Hand>(); //The list of played hands.
    private int currentPlayerIdx; //The index of current player.
    private BigTwoGUI ui; //The UI linked to this game.
    private BigTwoClient client; //The client of this game.


    /**
     * Method for get the client of this game.
     *
     * @return The client of current game.
     */
    public BigTwoClient getBigTwoclient(){
        return this.client;
    }

    /**
     * Method for getting the number of players.
     * 
     * @return The number of players.
     */
    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    /**
     * Method for retrieving the deck of cards being used.
     * 
     * @return The deck used in this game.
     */
    public Deck getDeck(){
        return this.deck;
    }

    /**
     * Method for retrieving the list of players.
     * 
     * @return The list of players with their information.
     */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return this.playerList;
    }

    /**
     * Method for retrieving the list of hands played on the table.
     * 
     * @return The list of played hands.
     */
    public ArrayList<Hand> getHandsOnTable(){
        return this.handsOnTable;
    }

    /**
     * Method for retrieving the index of the current player.
     * 
     * @return The index of current player.
     */
    public int getCurrentPlayerIdx(){
        return this.currentPlayerIdx;
    }

    /**
     * Method for starting/restarting the game with a given shuffled deck of cards. 
     * It remove all the cards from the players as well as from the table; 
     * distribute the cards to the players; identify the player who holds the Three of Diamonds; 
     * set both the currentPlayerIdx of the BigTwo object and the activePlayer of the BigTwoGUI object 
     * to the index of the player who holds the Three of Diamonds; call the repaint() method of the 
     * BigTwoGUI object to show the cards on the table; and call the promptActivePlayer() method 
     * of the BigTwoGUI object to prompt user to select cards and make his/her move.
     * 
     * @param deck The shuffled deck used for the game.
     */
    public void start(Deck deck){
        this.deck = deck;
        this.handsOnTable = new ArrayList<Hand>();
    
        for (int i = 0; i < this.numOfPlayers; i++)
        {
            CardGamePlayer current = this.playerList.get(i);
            current.removeAllCards();
            playerList.set(i, current);
        }//Reset players.


        for (int i = 0; i < 4; i++)
        {
            CardGamePlayer current = this.playerList.get(i);
            int [] culist = new int[52];
            for(int j = 0; j < 52; j++)
            {
                culist[j] = 0;
            }
            for(int j = 0; j < 13; j++)
            {
                culist[(deck.getCard(13*i + j).getSuit() + 4 * deck.getCard(13*i + j).getRank() + 44) % 52] = 1;
                if((deck.getCard(13*i + j).getRank() == 2) && (deck.getCard(13*i + j).getSuit() == 0))
                {
                    this.currentPlayerIdx = i;
                    this.ui.setActivePlayer(i);
                }    
            }//Sort the card of one player in BigTwo order.
            for(int j = 0; j < 52; j++)
            {
                if(culist[j] == 1)
                {
                    int num = (j + 8) % 52;
                    current.addCard(new Card(num % 4, num / 4));
                }
            }//Input to the player after sort.
            playerList.set(i, current);
        }
        this.handsOnTable.clear();
        this.ui.promptActivePlayer();
    }

    /**
     * Method for making a move by a player with the specified index using the cards specified
     * by the list of indices. It checked if the input is valid and reset the variables.
     *
     * @param playerIdx The index of the player who makes the move.
     * @param cardIdx The list of the indices of the cards selected by the player.
     */
    public void makeMove(int playerIdx, int[] cardIdx){
        checkMove(playerIdx, cardIdx);
    }

    /**
	 * Method used to check the validness of the input and direct to 
     * certain actions. Reset certain variables of the current user to continue
     * following procedures. Also check if a game if over.
	 * 
	 * @param playerIdx The index of the player who makes the move.
	 * @param cardIdx The list of the indices of the cards selected by the player.
	 */
    public void checkMove(int playerIdx, int[] cardIdx){
        CardGamePlayer current = playerList.get(playerIdx); //get the current player

        boolean pass = false; //If input is empty.
        boolean alter = false; //Input any valid cards but cannot pass.(Other guys pass or start the game)
        if(cardIdx == null){
            pass = true;
        }
        Hand nowhand =null; //Hand the user plays.
        Hand beforehand = null; //Hand on the table.
        if(pass == false)
            nowhand = composeHand(current, current.play(cardIdx));
        
        if(this.handsOnTable.isEmpty()){
            alter = true; //The game starts
            if(pass || cardIdx[0] != 0){
                this.ui.printMsg("Not a legal move!!!\nYou must play a valid hand with Diamond 3.\n");
                this.ui.promptActivePlayer();
                return;
            }//The first one doesn't play Diamond 3.
        }
        
        if(this.handsOnTable.size() > 0){
            beforehand = this.handsOnTable.get(this.handsOnTable.size() - 1);
            if(beforehand.getPlayer() == current)
                alter = true; //All other players skipped.
        }


        //Wrong input or pass for a round which can't pass. 
        if (alter && (pass || nowhand == null))
        {
            this.ui.printMsg("Not a legal move!!!\nYou must play a valid hand.\n");
            this.ui.promptActivePlayer();
            return;
        }

        //Can input any valid hands.
        else if(alter && !pass && nowhand != null)
        {
            this.ui.printMsg("{" + nowhand.getType() + "} ");
            for(int i = 0; i < nowhand.size(); i++){
                this.ui.printMsg("[" + nowhand.getCard(i) + "]");
            }//Print the card in text.
            this.ui.printMsg("\n\n");
            handsOnTable.add(nowhand);
            playerList.get(currentPlayerIdx).removeCards(nowhand);
        }

        //Pass the round that can pass. 
        else if(pass)
        {
            this.ui.printMsg("{Pass}\n\n");
        }

        //Input hand is not larger than before.
        else if(nowhand == null || !nowhand.beats(beforehand))
        {
            this.ui.printMsg("Not a legal move!!!\nYou hand is not greater.\n");
            this.ui.promptActivePlayer();
            return;
        }

        //Input hands beats previous one.
        else if(nowhand.beats(beforehand)){
            this.ui.printMsg("{" + nowhand.getType() + "} ");
            for(int i = 0; i < nowhand.size(); i++){
                this.ui.printMsg("[" + nowhand.getCard(i) + "]");
            }//Print the card in text.
            this.ui.printMsg("\n\n");
            handsOnTable.add(nowhand);
            playerList.get(currentPlayerIdx).removeCards(nowhand);
        }

        //Check if the game ends.
        if (this.endOfGame())
        {
            this.ui.setActivePlayer(-1);

            this.ui.promptActivePlayer();

            this.ui.printMsg("\n");
            String ms = "";
            ms += "Game ends\n";

            for(int i = 0; i < 4; i++)
            {
                if(i != currentPlayerIdx)
                    ms = ms + this.getPlayerList().get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " cards in hand.\n";
                else
                    ms = ms + this.getPlayerList().get(i).getName() + " wins the game.\n";
            }
            this.ui.printMsg(ms);
            JOptionPane.showMessageDialog(null, ms);
            this.client.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
            return;
        }

        //For case the game doesn't end.
        //Update the information.
        else {
            currentPlayerIdx = (currentPlayerIdx + 1) % 4;
            this.ui.setActivePlayer(currentPlayerIdx);
            this.ui.promptActivePlayer();
        }
        return;
    }

    /**
     * Method for checking if the game is end.
     * 
     * @return True for end and False otherwise.
     */
    public boolean endOfGame(){
        if(playerList.get(currentPlayerIdx).getNumOfCards() > 0)
            return false;
        return true;
    }

    /**
     * The main method for this game. It creats a BigTwo object and
     * a BigTwoDeck object which has shuffled 52 for this game. By 
     * calling the start function in BigTwo we start the game and
     * run the game untill it is end.
     * 
     * @param args This argument will not be used in this project.
     */
    public static void main(String[] args) {
        BigTwo bigtwo = new BigTwo();
        return;
    }


    /**
     * This method is to convert the cards users played into a valid hand.
     * If the convert can't be done it will return null.
     * 
     * @param player The index of the player who makes the move.
     * @param cards The cards this player played.
     * @return A valid hand if it can be generated or null otherwise.
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
        Hand result = null;

        if(cards == null)
        {
            return null;
        }

        if(cards.size() == 1)
        {
            result = new Single(player,cards);
            if (!result.isValid())
                return null;
        }
        else if(cards.size() == 2)
        {
            result = new Pair(player,cards);
            if (!result.isValid())
                return null;
        }
        else if(cards.size() == 3)
        {
            result = new Triple(player,cards);
            if (!result.isValid())
                return null;
        }
        else if(cards.size() == 5)
        {
            result = new StraightFlush(player,cards); //Must check StraightFlush before Straight and Flush.
            if (!result.isValid())
            {
                result = new Flush(player,cards);
                if (!result.isValid())
                {
                    result = new Straight(player,cards);
                    if (!result.isValid())
                    {
                        result = new Quad(player,cards);
                        if (!result.isValid())
                        {
                            result = new FullHouse(player,cards);
                            if (!result.isValid())
                                return null;
                        }
                    }
                }
            }
        }
        return result;
    }
}