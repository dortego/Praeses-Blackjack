package blackjack;

import java.awt.image.BufferedImage;

/**
 * @author davidortego
 */
public class Card {
  
  private BufferedImage image;
  private int value;
  private String faceName, suit;
  
  public Card(String suit, String face, int value, BufferedImage image) {
    this.suit = suit;
    this.faceName = face;
    this.image = image;
    this.value = value;
  }
  
  public int getValue() { 
    if(value >  10)
      value = 10;
    else if(value == 1)
      value = 11;
    return value; 
  }

  public void setValue(int value) { 
    this.value = value; 
  }
  
  public BufferedImage getImage() { 
    return image; 
  }
  
  public String getSuit() {
    return suit;
  }
  
  public void setImage(BufferedImage image) { 
    this.image = image; 
  }
  
  public String toString() {
    return faceName + " of " + suit + " , val: " + value;
  }
  
}
