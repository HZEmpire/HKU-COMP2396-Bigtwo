/**
 * The Straight class is a subclass of the Hand class and is used to model a hand of Straight. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * Straight and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class Straight extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    private boolean acardisvalid(int i){
        Card current = this.getCard(i);
        if(0 <= current.getSuit() && current.getSuit() <= 3 && 0 <= current.getRank() && current.getRank() <= 12)
            return true;
        else
            return false;
    }//Check if a single card is valid.


    /**
     * Method for checking if it is a valid Staight hands.
     * 
     * @return True for valid and False otherwise.
     */
    public boolean isValid(){
        if(this.size() != 5)//Wrong lenth.
            return false;
        int [] tempor = new int [13];
        for(int i = 0; i < 13; i++)
            tempor[i] = 0;

        for(int i = 0; i < 5; i++){
            if(acardisvalid(i) == false)
                return false;
            tempor[(this.getCard(i).getRank() + 11) % 13] = 1;
        }//Map the card from normal order to BigTwo order.
        int start = 12;
        for(int i = 0; i < 13; i++)
        {
            if(tempor[i] == 1)
            {
                start = i;
                break;
            }
        }//Get the smallest number of straight.
        if (start > 8)
            return false;//Cannot start with Q.
        for(int i = start + 1; i < start + 5; i++)
        {
            if(tempor[i] == 0)
                return false;
        }//Check consective.
        this.large = start + 4;
        return true;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "Straight".
     */
    public String getType(){
        return "Straight";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in Straight.
     */
    public int getLarge(){
        return this.large;
    }
}
