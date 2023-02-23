/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a 
 * Big Two card game. It should override the compareTo() method it inherits from the Card 
 * class to reflect the ordering of cards used in a Big Two card game. 
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class BigTwoCard extends Card{
    /**
     * Constructor for building a card with the specified suit and rank. Suit is an integer 
     * between 0 and 3, and rank is an integer between 0 and 12.
     * 
     * @param suit An integer between 0 - 3 to specify the suit. 0-3 is Diamond, Club, Heart, Spade repectively.
     * @param rank An integer between 0 - 12 to specify the rank. 0-12 is A, 2, 3... 12 repectively.
     */
    public BigTwoCard(int suit, int rank) {
        super(suit, rank);
    }
    

    
    /**
     * Method for comparing the order of this card with the specified card. Returns a negative integer,
     * zero, or a positive integer when this card is less than, equal to, or greater than the specified card.
     * 
     * @param card The card to be compared with current one.
     * @return Larger with positive results.
     */
    @Override
    public int compareTo(Card card) {
        int re = (((this.getRank() + 11) % 13) * 4) - (((card.getRank() + 11) % 13) * 4) + this.getSuit() - card.getSuit();
        return re; 
    }
}
