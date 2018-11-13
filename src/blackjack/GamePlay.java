package blackjack;

import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * @author davidortego
 */
public class GamePlay {
  
  private Deck deck;
  
  private Hand dealerHand;
  private Hand userHand;
  
  private int winCount = 0;
  private int bustCount = 0;
  private int lostCount = 0;
  private int pushCount = 0;
  private int blackjackCount;

  public GamePlay(Hand dealerHand, Hand userHand) throws IOException {
    this.deck = new Deck();
    this.dealerHand = dealerHand;
    this.userHand = userHand;
  }
  
  // makes initial dealings
  public void initializeGame() {
    dealerHand.newCard(deck);
    dealerHand.newCard(deck);
    userHand.newCard(deck);
    userHand.newCard(deck); 
  }
  
  public BufferedImage getCardBack() {
    return this.deck.displayBackCard();
  }
  
  public Card hit(Hand player) {
    return player.newCard(deck);
  }
  
  public int handValue(Hand player) {
    return player.getHandVal();
  }
  
  // checks if user hand value has reached 21
  public boolean userHasBlackjack() {
    boolean won = false;
    
    if(userHand.getHandVal() == 21)
      won = true;
    
    winCount++;
    blackjackCount++;
    
    return won;
  }
  
  // checks hand value to see if it has busted
  public boolean hasBusted(Hand player) {
    boolean bust = false;

    if(player.getHandVal() > 21)
      bust = true;

    bustCount++;
 
    return bust;
  }
  
  // dealer stands on 17
  public Hand dealerAI() {
    Hand hand = dealerHand;

    while(dealerHand.getHandVal() <= 17)
      dealerHand.newCard(deck);
    
    return hand;
  }
  
  // determines outcome and returns appropriate string message
  public String outcome() {
    
    String message = "";
    if((userHand.getHandVal() < dealerHand.getHandVal()) && dealerHand.getHandVal() <= 21) {                
      lostCount++;

      message = "You've lost";
    } 
    else if ((userHand.getHandVal() == dealerHand.getHandVal()) && dealerHand.getHandVal() <= 21) {
      pushCount++;

      message = "Push";
    }  
    else {
      winCount++;
      
      message = "You've won";
    }
    
    return message;
  }
  
}
