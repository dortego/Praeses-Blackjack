package blackjack;

/**
 * @author davidortego
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;
import javax.swing.*;
 

public class Driver extends JFrame implements ActionListener {
  private JLabel l1, l2;
  private JButton statsButton;
  private JButton playButton;
  JFrame statsFrame = new JFrame();
  JTextArea textArea = new JTextArea();
 
  DatabaseHelper db = new DatabaseHelper();
  ResultSet rs;
 
  public Driver() {
    JFrame frame = new JFrame();
    l1 = new JLabel("Praeses Blackjack Game");
    l1.setForeground(Color.blue);
    l1.setFont(new Font("Courier", Font.BOLD, 20));

    l2 = new JLabel("created by David Ortego");
    l2.setForeground(Color.blue);
    l2.setFont(new Font("Courier", Font.BOLD, 15));
    statsButton = new JButton("Game Stats");
    playButton = new JButton("Play");

    l1.setBounds(80, 40, 400, 30);
    l2.setBounds(100, 80, 400, 30);
    statsButton.setBounds(75, 140, 100, 30);
    playButton.setBounds(200, 140, 100, 30);

    textArea.setFont(new Font("Courier", Font.PLAIN, 20));
    
    // *** display stats from database: ***
    try {
      rs = db.displayStats();
      while(rs.next()) {
        textArea.append("==================" + "\n" +
                        "  Games Played: " + rs.getInt("games_played") + "\n" + 
                        "==================\n" +
                        "  Won: " + "\t\t" + rs.getInt("won") + "\n" + 
                        "==================\n" +
                        "  Lost: " + "\t" + rs.getInt("lost") + "\n" + 
                        "==================\n" +
                        "  Bust: " + "\t" + rs.getInt("bust") + "\n" +
                        "==================\n" +
                        "  Push: " + "\t" + rs.getInt("push") + "\n" + 
                        "==================\n" +
                        "  Blackjacks: " + "\t" + rs.getInt("blackjacks") + "\n" + 
                        "==================" + "\n");
        JScrollPane scrollPane = new JScrollPane(textArea); 
        textArea.setEditable(false);
        statsFrame.getContentPane().add(scrollPane);

        System.out.println("Games played: " + rs.getInt("games_played"));
      } 
    } catch (ClassNotFoundException | SQLException ex) {
      Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
    }
    // ***********
    
    statsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        statsFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        statsFrame.setTitle("Game Stats");
        statsFrame.setSize(400, 400);
        statsFrame.setVisible(true);
      }
    });

    playButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          new GameGUI();
        } catch (IOException ex) {
          Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.dispose();
      }
    });

    frame.add(l1);
    frame.add(l2);
    frame.add(statsButton);
    frame.add(playButton);

    frame.setSize(400, 300);
    frame.setLayout(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) throws IOException {
    new Driver();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}