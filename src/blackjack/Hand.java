package blackjack;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author davidortego
 */
public class Hand {
  
  private boolean busted;
  private String message;
  public ArrayList<Card> inHand;
  private int handVal;
  private int count;
  
  public Hand() {
    this.busted = false;
    this.message = "";
    this.inHand = new ArrayList<Card>() {};
    this.handVal = 0;
    this.count = 0;
  }
  
  public Card newCard(Deck currentDeck) {
    Card drawnCard = currentDeck.dealCard();
    inHand.add(drawnCard);
    handVal += drawnCard.getValue();
    checkHand();
    count++;
    return drawnCard;
  }
  
  public int getHandVal() {
    return handVal;
  }
  
  // Reduce handVal if there is an ace in hand and handVal > 21 (aces can be high or low)
  private void checkHand() {
      if((handVal) > 21) {
         if(aceInHand())
            handVal -= 10;           
      }
  }
  
  // Check if there is an ace in the hand.
  private boolean aceInHand() {
    boolean result = false;
    Card cardCheck = null;
    Iterator<Card> scan = inHand.iterator();

    while (scan.hasNext() && !result) {
       cardCheck = scan.next();
       if(cardCheck.getValue() == 11) {
//         cardCheck.setValue(1);
         result = true;
       }
      
    }
    return result;
  }
}
