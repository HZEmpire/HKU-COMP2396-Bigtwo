/**
 * The StraightFlush class is a subclass of the Hand class and is used to model a hand of StraightFlush. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * StraightFlush and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class StraightFlush extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public StraightFlush(CardGamePlayer player, CardList cards) {
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
     * Method for checking if it is a valid StaightFlush hands.
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
        }//Map the order to BigTwo order.
        int start = 12;
        for(int i = 0; i < 13; i++)
        {
            if(tempor[i] == 1)
            {
                start = i;
                break;
            }
        }
        if (start > 8)
            return false;
        for(int i = start + 1; i < start + 5; i++)
        {
            if(tempor[i] == 0)
                return false;
        }

        int suit = this.getCard(0).getSuit();
        for(int i = 1; i < 5; i++)
        {
            if(this.getCard(i).getSuit() != suit)
                return false;
        }//Check suit.
        this.large = start + 4;//Set the top card.
        return true;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "StraightFlush".
     */
    public String getType(){
        return "StraightFlush";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in StraightFlush.
     */
    public int getLarge(){
        return this.large;
    }
}