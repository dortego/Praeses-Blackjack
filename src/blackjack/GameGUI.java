package blackjack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author davidortego
 */
public class GameGUI {

  JFrame frame = new JFrame();
  
  Hand dealerHand = new Hand();
  Hand userHand = new Hand();
  
  GamePlay game = new GamePlay(dealerHand, userHand);
  
  JPanel boardPanel;
  JPanel controlPanel = new JPanel();
  
  DatabaseHelper db = new DatabaseHelper();
  
  //****************************************************************************
  //********    BOARD PANEL    *************************************************
  //**************************************************************************** 
  private final Color tableColor = new Color(39, 119, 20); 
 
  // initial card labels and to-be-instantiated card labels:
  JLabel userCardLabel1;
  JLabel userCardLabel2;
  JLabel newUserCardLabel;
  
  JLabel dealerCardLabel0;
  JLabel dealerCardLabel1;
  JLabel dealerCardLabel2;
  JLabel newDealerCardLabel;

  // nested panels for dealer and player cards:
  JPanel dealerPanel = new JPanel();
  JPanel userPanel = new JPanel();    

  // labels for the dealer and player hand values:
  JLabel dealerValLabel = new JLabel();
  JLabel userValLabel = new JLabel();

  //****************************************************************************
  //********    CONTROL PANEL    ***********************************************
  //****************************************************************************
  Color controlColor = new Color(212, 212, 212); 
  
  JButton hitBtn = new JButton();
  JButton standBtn = new JButton();
  JButton quitBtn = new JButton();
  Dimension d = new Dimension(30, 10);  // button size

  
  //****************************************************************************
  //********    GUI CONSTRUCTOR    *********************************************
  //****************************************************************************
  public GameGUI() throws IOException {
    
    // increment games count in the database:
    try {
    db.updateGames();
    } catch (ClassNotFoundException | SQLException ex) {
      Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //**********  BOARD PANEL  *************************************************
    dealerValLabel.setText(" Dealer: ");
    userValLabel.setText(" User: ");
    
    // nested panels for dealer and player cards (and vals):
    dealerPanel.add(dealerValLabel);
    userPanel.add(userValLabel);

    //***********  GAME HANDS INITIALIZING  ************************************
    game.initializeGame();
    
    // to iterate dealer hand and add a new dealer card
    Card dealerCard=null;
    Iterator<Card> dealerScan = (dealerHand.inHand).iterator();
    int count = 0;
    while (dealerScan.hasNext()) {
        dealerCard = dealerScan.next();
        if(count==0)
          dealerCardLabel1 = new JLabel(new ImageIcon(dealerCard.getImage()));
        else 
          dealerCardLabel2 = new JLabel(new ImageIcon(dealerCard.getImage()));
          
        count++;
    }

    // to iterate and add to a current user hand
    Iterator<Card> userScan = (userHand.inHand).iterator();
    count = 0;
    while (userScan.hasNext()) {
       Card userCard = userScan.next();
       if(count==0)
         userCardLabel1 = new JLabel(new ImageIcon(userCard.getImage()));
       else
         userCardLabel2 = new JLabel(new ImageIcon(userCard.getImage()));
   
       count++;
    }
    
    // get back card image:
    dealerCardLabel0 = new JLabel(new ImageIcon(game.getCardBack()));
    
    dealerPanel.add(dealerCardLabel0);
    dealerPanel.add(dealerCardLabel2);
 
    userPanel.add(userCardLabel1);
    userPanel.add(userCardLabel2);
    
    dealerValLabel.setText("  Dealer:  " + dealerCard.getValue());
    userValLabel.setText("  User:  " + game.handValue(userHand));
    
    hitBtn.setEnabled(true);
    standBtn.setEnabled(true);
    quitBtn.setEnabled(true);
    
    // match background of the table (game board):
    dealerPanel.setBackground(tableColor);
    userPanel.setBackground(tableColor);
    
    boardPanel = new JPanel();
    boardPanel.setLayout(new BorderLayout());
    boardPanel.add(dealerPanel, BorderLayout.NORTH);
    boardPanel.add(userPanel, BorderLayout.SOUTH);    
    boardPanel.setPreferredSize(new Dimension(500, 400));
    boardPanel.setBackground(tableColor);
    
    //**********  CONTROL PANEL  ***********************************************    
    hitBtn.setSize(d);
    standBtn.setSize(d);
    quitBtn.setSize(d);
    hitBtn.setText("Hit");
    standBtn.setText("Stand");
    quitBtn.setText("Quit");
    hitBtn.addActionListener(new Hit());
    standBtn.addActionListener(new Stand());
    
    // quit button anonymous actionlistener:
    quitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
        new Driver();
      }
    });
    
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
    controlPanel.add(hitBtn);
    controlPanel.add(standBtn);
    controlPanel.add(quitBtn);
    controlPanel.setPreferredSize(new Dimension(150, 400));
    controlPanel.setBackground(controlColor);
    
    //***********  GAME TABLE FRAME  *******************************************
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    frame.setTitle("Game Table");
    frame.setResizable(false);
    frame.setLayout(new FlowLayout());
    frame.getContentPane().add(boardPanel);
    frame.getContentPane().add(controlPanel);
    frame.pack();
    frame.setVisible(true);
    
    // check if user was dealt blackjack right off the bat:
    checkFor21();
  } // end of constructor
  
  private class Hit implements ActionListener { 
    public void actionPerformed(ActionEvent e) {
      
      Card hitCard = game.hit(userHand);
      newUserCardLabel = new JLabel(new ImageIcon(hitCard.getImage()));
      userPanel.add(newUserCardLabel);
      userPanel.repaint();
      
      userValLabel.setText("  Player:   " + game.handValue(userHand));
      
      int option = -1;
      
      //***** user got blackjack:
      if(game.userHasBlackjack()) {
        
        // increment won and blackjacks in database:
        try {
          db.updateWon();
          db.updateBlackjacks();
        } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        option = JOptionPane.showConfirmDialog(null, "Congrats!\nYou got blackjack.\nPlay again?", "", JOptionPane.YES_NO_OPTION);
        hitBtn.setEnabled(false);
        standBtn.setEnabled(false);
        System.out.println("Blackjack");
      }
      
      //***** user busted:
      if(game.hasBusted(userHand)) {
        option = JOptionPane.showConfirmDialog(null, "You've busted!\nPlay again?", "", JOptionPane.YES_NO_OPTION);
        System.out.println("Bust");
        hitBtn.setEnabled(false);
        standBtn.setEnabled(false);
        
        // increment bust in database:
        try {
          db.updateBust();
        } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      
      // play again or not:
      if(option == 0) {   // yes = 0
        try {
          new GameGUI();
        } catch (IOException ex) {
          Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.dispose();
      }  
      else if(option == 1) {  // no = 1
        frame.dispose();
        new Driver();
      }  
    }
  } // end of Hit
  
  private class Stand implements ActionListener { 
    public void actionPerformed(ActionEvent e) {
      
      dealerPanel.remove(dealerCardLabel0);
      dealerPanel.add(dealerCardLabel1);

      dealerHand = game.dealerAI();
      dealerPanel.removeAll();
      dealerPanel.add(dealerValLabel);
      dealerValLabel.setText(" " + dealerValLabel.getText());  

      // iterate through cards and re-display
      Card dealerhitCard = null;
      Iterator<Card> scan = (dealerHand.inHand).iterator();
      while (scan.hasNext()) {
        dealerhitCard = scan.next();
        newDealerCardLabel = new JLabel(new ImageIcon(dealerhitCard.getImage()));
        dealerPanel.add(newDealerCardLabel);
      }

      dealerValLabel.setText("Dealer: " + game.handValue(dealerHand));
      userValLabel.setText("User: " + game.handValue(userHand));

      int option = -1;
      String message = game.outcome();
      option = JOptionPane.showConfirmDialog(null, message + "!\nPlay again?", "", JOptionPane.YES_NO_OPTION);
      System.out.println(game.outcome());
      hitBtn.setEnabled(false);
      standBtn.setEnabled(false);
      
      // increment outcome in the database:
      if(message.equalsIgnoreCase("You've won")) {
        try {
          db.updateWon();
        } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      else if(message.equalsIgnoreCase("You've lost")) {
        try {
          db.updateLost();
        } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      else if(message.equalsIgnoreCase("Push")) {
        try {
          db.updatePush();
        } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      
      // play again or not:
      if(option == 0) {   // yes = 0
        try {
          new GameGUI();
        } catch (IOException ex) {
          Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.dispose();
      }  
      else if(option == 1) {  // no = 1
        frame.dispose();
        new Driver();
      }  
    }
  } // end of Stand

  // check if user was dealt blackjack right off the bat
  public void checkFor21() {
    int option = -1;
    
    if(game.userHasBlackjack()) {
      option = JOptionPane.showConfirmDialog(null, "Lucky you!\nYou got blackjack right off the bat.\nPlay again?", "", JOptionPane.YES_NO_OPTION);
      hitBtn.setEnabled(false);
      standBtn.setEnabled(false);
      System.out.println("Blackjack");
      
      try {
        db.updateWon();
        db.updateBlackjacks();
      } catch (ClassNotFoundException | SQLException ex) {
        Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
      }
     }
    
    // play again or not:
      if(option == 0) {   // yes = 0
        try {
          new GameGUI();
        } catch (IOException ex) {
          Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.dispose();
      }  
      else if(option == 1) {  // no = 1
        frame.dispose();
        new Driver();
      }  
  } // end of checkFor21
}
