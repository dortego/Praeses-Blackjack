package blackjack;

import java.sql.*;

/**
 *
 * @author davidortego
 */
public class DatabaseHelper {
  
  private static Connection con;
  private static boolean hasData = false;
  
  public ResultSet displayStats() throws ClassNotFoundException, SQLException {
    if(con == null)
      getConnection();
    
    Statement statement = con.createStatement();
    ResultSet res = statement.executeQuery("SELECT games_played, won, lost, bust, push, blackjacks FROM stats");
    return res;
  }
  
  private void getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    con = DriverManager.getConnection("jdbc:sqlite:Stats.db");
    initialize();
  }
  
  private void initialize() throws SQLException {
    if(!hasData) {
      hasData = true;
      Statement statement = con.createStatement();
      ResultSet res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'stats'");
      if(!res.next()) {
        System.out.println("Building the stats table w/ pre-populated values.");
        // need to build table
        Statement statement2 = con.createStatement();
        statement2.execute("CREATE TABLE stats(id integer primary key, games_played integer," +
                                              "won integer, lost integer," +
                                              "bust integer, push integer, blackjacks);");
        
        // insert some intitial data
        PreparedStatement prep = con.prepareStatement("INSERT INTO stats values(?,?,?,?,?,?,?);");
        prep.setInt(1, 0);
        prep.setInt(2, 0);
        prep.setInt(3, 0);
        prep.setInt(4, 0);
        prep.setInt(5, 0);
        prep.setInt(6, 0);
        prep.setInt(7, 0);
        prep.execute();
      }
    }
  }
  
  // methods to increment stat counts:
  public void updateGames() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET games_played = games_played + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
  
  public void updateWon() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET won = won + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
  
  public void updateLost() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET lost = lost + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
  
  public void updateBust() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET bust = bust + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
  
  public void updatePush() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET push = push + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
  
  public void updateBlackjacks() throws ClassNotFoundException, SQLException {
    if(con == null) 
      getConnection();
    
    PreparedStatement prep = con.prepareStatement("UPDATE stats SET blackjacks = blackjacks + 1 WHERE id = 0;");
    prep.executeUpdate();
  }
}
