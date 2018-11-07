import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDB {

	private static Connection con;
	private static boolean hasData = false;
	final int users[] = new int[20];
	ResultSet rss = null;
	
	public ResultSet displayUsers() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT UserName, Password FROM users");
		return res;
		
	}
	
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:User.db");
		initialize();
	}
	
	private void initialize() throws SQLException {
		// TODO Auto-generated method stub
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
			if(!res.next()) {
				System.out.println("Building the User table with prepopulated values");
				//Building the table
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE users("
					+ "EmployeeID INTEGER PRIMARY KEY," 
					+ "UserName varchar(255),"
					+ "Password varchar(255));");
				
				PreparedStatement pre = con.prepareStatement("INSERT INTO users VALUES(?,?,?);");
				
				for(int i=0; i<users.length; i++) {
					
					pre.setString(2, populateNames(i));	
					pre.setString(3, populateNames(i)+"2018");
					pre.execute();				
				}		
			}
			
		}
	}
	
	public String populateNames(int num) {
		switch(num) {
			case 0: 
				return "Elven";
			case 1:
				return "Salman";
			case 2:
				return "Austin";
			case 3:
				return "Hasin";
			case 4:
				return "Nic Cage";
			case 5:
				return "Barack Obama";
			case 6:
				return "John Cena";
			case 7:
				return "Will Smith";
			case 8:
				return "Trump";
			case 9:
				return "Hackerman";
			case 10:
				return "Mark Jacob";
			case 11:
				return "Louis Smith";
			case 12:
				return "Peter Parker";
			case 13:
				return "Cough Syrup";
			case 14:
				return "Anonymous";
			case 15:
				return "NotAnEmployee";
			case 16:
				return "Jackie Chan";
			case 17:
				return "Gym teacher";
			case 18:
				return "William";
			case 19:
				return "Apple";
			default:
				return "Android";
		}
	}
	
	
	

}
