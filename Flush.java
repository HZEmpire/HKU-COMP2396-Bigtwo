/**
 * The Flush class is a subclass of the Hand class and is used to model a hand of Flush. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * Flush and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class Flush extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public Flush(CardGamePlayer player, CardList cards) {
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
     * Method for checking if it is a valid Flush hands.
     * 
     * @return True for valid and False otherwise.
     */
    public boolean isValid(){
        if(this.size() != 5)//Wrong lenth.
            return false;
        int firstsuit = this.getCard(0).getSuit();
        for(int i = 0; i < 5; i++)
        {
            if( acardisvalid(i) == false)
                return false;
            if( this.getCard(i).suit != firstsuit)
                return false;
            for(int j = i+1; j < 5; j++)
            {
                if(this.getCard(j).getRank() == this.getCard(i).getRank())
                    return false;
            }
        }//Check if same card occurs twice and if not same suit occurs.
        int [] num = new int[13];
        for(int i = 0; i < 13; i++)
            num[i] = 0;
        for(int i = 0; i < 5; i++){
            num[(this.getCard(i).getRank() + 11) % 13] = 1;
        } 
        for(int i = 12; i >= 0; i--){
            if(num[i] == 1)
            {
                this.large = i;
                break;
            }
        }//Get the top card.
        return true;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "Flush".
     */
    public String getType(){
        return "Flush";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in Flush.
     */
    public int getLarge(){
        return this.large;
    }
}