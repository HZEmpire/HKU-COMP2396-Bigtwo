/**
 * The Pair class is a subclass of the Hand class and is used to model a hand of Pair. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * Pair and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class Pair extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public Pair(CardGamePlayer player, CardList cards) {
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
     * Method for checking if it is a valid Pair hands.
     * 
     * @return True for valid and False otherwise.
     */
    public boolean isValid(){
        if (this.size() == 2)
        {
            if(acardisvalid(0) && acardisvalid(1)){
                if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getSuit() != this.getCard(1).getSuit()){
                    this.large = (this.getCard(0).getRank() + 11) % 13;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "Pair".
     */
    public String getType(){
        return "Pair";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in Pair.
     */
    public int getLarge(){
        return this.large;
    }
}
