/**
 * The Triple class is a subclass of the Hand class and is used to model a hand of Triple. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * Triple and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class Triple extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */

    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }//Check if a single card is valid.

    /**
     * Method for checking if it is a valid Triple hands.
     * 
     * @return True for valid and False otherwise.
     */
    private boolean acardisvalid(int i){
        Card current = this.getCard(i);
        if(0 <= current.getSuit() && current.getSuit() <= 3 && 0 <= current.getRank() && current.getRank() <= 12)
            return true;
        else
            return false;
    }
    public boolean isValid(){
        if(this.size() == 3){
            if(acardisvalid(0) && acardisvalid(1) && acardisvalid(2))
            {
                if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(2).getRank()){
                    if(this.getCard(0).getSuit() != this.getCard(1).getSuit() && this.getCard(0).getSuit() != this.getCard(2).getSuit() && this.getCard(1).getSuit() != this.getCard(2).getSuit()){
                        this.large = (this.getCard(0).getRank() + 11) % 13;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "Triple".
     */
    public String getType(){
        return "Triple";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in Triple.
     */
    public int getLarge(){
        return this.large;
    }
}