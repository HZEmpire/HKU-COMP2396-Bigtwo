/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards. It has 
 * a private instance variable for storing the player who plays this hand. It also has methods for 
 * getting the player of this hand, checking if it is a valid hand, getting the type of this hand, 
 * getting the top card of this hand, and checking if it beats a specified hand. 
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public abstract class Hand extends CardList{
    /**
     * Constructor for building a hand with the specified player and list of cards.
     * 
     * @param player The player who plays this hand.
     * @param cards The cards played by the player.
     */
    public Hand(CardGamePlayer player, CardList cards){
        this.player = player;
        
        for(int i = 0; i < cards.size(); i++){
            this.addCard(cards.getCard(i));
        }
    }

    private CardGamePlayer player; //The player who plays this hand.

    /**
     * Method to return the player who plays this hand.
     * 
     * @return The player who plays this hand.
     */
    public CardGamePlayer getPlayer(){
        return player;
    }

    /**
     * Method to return the top card of the played cards.
     * 
     * @return The top card.
     */
    public Card getTopCard(){
        return this.getCard(this.size() - 1);
        //This method only get the top single card in a hand.
        //It may not get the top card of Quad and FullHouse.
        //This problem will be fixed by another method getLarge(). 
    }

    /**
     * Method to compare this hand with another given hand.
     * 
     * @param givenhand The given hand used to compare.
     * @return True for larger than given hand and False other wise(including wrong input).
     */
    public boolean beats(Hand givenhand)
    {
        //This is used for general case if the hand is not sorted by BigTwo order.
        if(this.size() >= 1 && this.size() <= 3 && this.getType() == givenhand.getType())
        {
            if (this.getLarge() == givenhand.getLarge())
                return this.getTopCard().getSuit() > givenhand.getTopCard().getSuit();
            return this.getLarge() > givenhand.getLarge();
        }
        else if(this.size() == 5 && givenhand.size() == 5)
        {
            if(this.getType() == "Straight"){
                if(givenhand.getType() == "Straight"){
                    if (this.getLarge() == givenhand.getLarge())
                        return this.getTopCard().getSuit() > givenhand.getTopCard().getSuit();
                    return this.getLarge() > givenhand.getLarge();
                }
                return false;
            }
            else if(this.getType() == "Flush"){
                if(givenhand.getType() == "Straight")
                    return true;
                else if(givenhand.getType() == "Flush"){
                    if (this.getTopCard().getSuit() == givenhand.getTopCard().getSuit())
                        return this.getLarge() > givenhand.getLarge();
                    return this.getTopCard().getSuit() > givenhand.getTopCard().getSuit();
                }
                return false;
            }
            else if(this.getType() == "FullHouse"){
                if(givenhand.getType() == "Straight" || givenhand.getType() == "Flush")
                    return true;
                else if(givenhand.getType() == "FullHouse")
                    return this.getLarge() > givenhand.getLarge();
                return false;
            }
            else if(this.getType() == "Quad"){
                if(givenhand.getType() == "Straight" || givenhand.getType() == "Flush" || givenhand.getType() == "FullHouse")
                    return true;
                else if(givenhand.getType() == "Quad")
                    return this.getLarge() > givenhand.getLarge();
                return false;
            }
            else if(this.getType() == "StraightFlush"){
                if(givenhand.getType() == "Straight" || givenhand.getType() == "Flush" || givenhand.getType() == "FullHouse" || givenhand.getType() == "Quad")
                    return true;
                else if(givenhand.getType() == "StraightFlush"){
                    if (this.getLarge() == givenhand.getLarge())
                        return this.getTopCard().getSuit() > givenhand.getTopCard().getSuit();
                    return this.getLarge() > givenhand.getLarge();
                }
                return false;
            }
            return false;
        }
        else 
            return false;
    }

    /**
     * Method for checking if it is a valid hands. To be implement further.
     * 
     * @return True for valid and False otherwise.
     */
    public abstract boolean isValid();

    /**
     * Method to get the type of this hand. To be implement further.
     * 
     * @return The type of this hand.
     */
    public abstract String getType();

    /**
     * Method to represent how large this hands is with other same type hands. To be implement further.
     * 
     * @return The value to represent how large it is.
     */
    public abstract int getLarge();
}
