package blackjack;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author davidortego
 */
public class Hand {
  
  private String message;
  public ArrayList<Card> inHand;
  private int handVal;
  
  public Hand() {
    this.message = "";
    this.inHand = new ArrayList<Card>() {};
    this.handVal = 0;
  }
  
  public Card newCard(Deck deck) {
    Card drawnCard = deck.dealCard();
    inHand.add(drawnCard);
    handVal += drawnCard.getValue();
    checkHand(drawnCard);
    
    return drawnCard;
  }
  
  public int getHandVal() {
    return handVal;
  }
  
  // Reduce handVal if there is an ace in hand and handVal > 21 (aces can be high or low)
  private void checkHand(Card card) {
      if((handVal) > 21 && aceInHand()) {
        handVal -= 10;           
      }
  }
  
  // Check if there is an ace in the hand.
  private boolean aceInHand() {
    boolean result = false;
    Card cardCheck = null;
    Iterator<Card> scan = inHand.iterator();

    while (scan.hasNext() && result == false) {
       cardCheck = scan.next();
       if(cardCheck.getValue() == 11) {
         cardCheck.setValue(1);
         result = true;
       }
      
    }
    return result;
  }
}
