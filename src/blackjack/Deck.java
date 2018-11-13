package blackjack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import javax.imageio.*;

/**
 *
 * @author davidortego
 */
public class Deck {
  
  private Card[] cards;
  private int currentCard;  // index of next card to be delt
  private BufferedImage backCardImg;
  
  public Deck() throws IOException {
    String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
    
    cards = new Card[52];
    currentCard = 0;  
    
    // info used to extract individual cards from single image:
    final int WIDTH = 72;
    final int HEIGHT = 113;
    final int ROWS = 4;
    final int COLS = 13;
    
    BufferedImage wholeImg = ImageIO.read(new File("card-deck.png"));
    BufferedImage tempCardImg;
    
    backCardImg = wholeImg.getSubimage(2 * WIDTH + 5, 3 * HEIGHT + 122, WIDTH, HEIGHT);
    
    for(int suit = 0; suit < ROWS; suit++) {
      for(int faceNum = 0; faceNum < COLS; faceNum++) {
        // extract the image: 
        tempCardImg = wholeImg.getSubimage(faceNum*WIDTH + (faceNum*2), suit*HEIGHT + (suit*3), WIDTH, HEIGHT);
        
        cards[(faceNum + (suit*13))] = new Card(suits[suit], faces[faceNum], faceNum+1, tempCardImg);
      }
    }
  }
  
  public BufferedImage displayBackCard() {
    return backCardImg;
  }
  
  public void displayDeck() {
    for(Card card : cards)
      System.out.println(card);
  }
  
  public void shuffle() {
    currentCard = 0;
    
    SecureRandom random = new SecureRandom();
    
    for(int first = 0; first < cards.length; first++) {
      int second = random.nextInt(52);
      
      Card temp = cards[first];
      cards[first] = cards[second];
      cards[second] = temp;
    }
  }
  
  // return current card and increment current card position (always shuffles first)
  public Card dealCard() {
    shuffle();
  
    if (currentCard < cards.length) {    
      return cards[currentCard++];
    }  
    else {
      return null;
    }
  }
}
