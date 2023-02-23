/**
 * The Quad class is a subclass of the Hand class and is used to model a hand of Quad. It has method and
 * variables which Hand has. Also it implement abstract methods in hand which are use to check if it is a valid
 * Quad and get the Type of it and get how large it is in same type hands.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class Quad extends Hand{
    private int large = 0; //How large it is in same type.

    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public Quad(CardGamePlayer player, CardList cards) {
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
     * Method for checking if it is a valid Quad hands.
     * 
     * @return True for valid and False otherwise.
     */
    public boolean isValid(){
        if(this.size() != 5)//Wrong lenth.
            return false;
        int firstrank = this.getCard(0).getRank();//The place of first rank starts.
        int secondrank = -1;//The place of second rank starts.
        for(int i = 1; i < 5; i++){
            if(this.getCard(i).getRank() != firstrank){
                secondrank = this.getCard(i).getRank();
                break;
            }
        }
        if( secondrank == -1)
            return false;
        
        int countfirst = 0, countsecond = 0;

        for(int i = 0; i < 5; i++)
        {
            if( acardisvalid(i) == false)
                return false;
            if( this.getCard(i).getRank() == firstrank)
                countfirst += 1;
            else if(this.getCard(i).getRank() == secondrank)
                countsecond += 1;
            for(int j = i+1; j < 5; j++){
                if(this.getCard(j).getRank() == this.getCard(i).getRank() && this.getCard(j).getSuit() == this.getCard(i).getSuit())
                    return false;
            }
        }  //Count.
        if ((countsecond == 4 && countfirst == 1) || (countsecond == 1 && countfirst == 4)){
            this.large = countfirst > countsecond ? ((firstrank + 11) % 13) : ((secondrank + 11) % 13);
            return true;
        }//Check validness and set the top card.
        return false;
    }

    /**
     * Method to get the type of this hand.
     * 
     * @return The type "Quad".
     */
    public String getType(){
        return "Quad";
    }

    /**
     * Method to represent how large this hands is with other same type hands.
     * 
     * @return The value to represent how large it is in Quad.
     */
    public int getLarge(){
        return this.large;
    }
}