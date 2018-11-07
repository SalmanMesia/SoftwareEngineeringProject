import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class EventsDB {

	private static Connection con;
	private static boolean hasData = false;
	final int event[] = new int[20];
	final String e[] = {"Business Conference", "Birthday Party", "Musical Performance", "Banquet Hall"};
	final String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	double ticketPrice = 5.00;
	ResultSet rss = null;

	public ResultSet displayEvents() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT * FROM events");
		return res;
		
	}
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Events.db");
		initialize();
	}
	
	private void initialize() throws SQLException {
		// TODO Auto-generated method stub
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='events'");
			if(!res.next()) {
				System.out.println("Building the User table with prepopulated values");
				//Building the table
				Statement state2 = con.createStatement();

				state2.execute("CREATE TABLE events("
					+ "EventsID INTEGER PRIMARY KEY," 
					+ "Seats INT,"
					+ "Sunday varchar(255)," 
					+ "Monday varchar(255)," 
					+ "Tuesday varchar(255),"
					+ "Wednesday varchar(255),"
					+ "Thursday varchar(255),"
					+ "Friday varchar(255),"
					+ "Saturday varchar(255)," 
					+ "Sales DOUBLE);");
				
				PreparedStatement pre = con.prepareStatement("INSERT INTO events VALUES(?,?,?,?,?,?,?,?,?,?);");
				
				
				for(int i=1; i<event.length; i++) {
					
					//pre.setInt(2, i);
					pre.setInt(2, 300);
					Random rand = new Random();
					int day = rand.nextInt((9 - 3) + 1) + 3;
					System.out.println(day);
					int f = rand.nextInt(3)+1;

					pre.setString(day, e[f]);
					for(int j=3; j<10; j++) {
						if(j!=day) {
							pre.setString(j, "None");
						}
					}
					/*int f = rand.nextInt(4)+1;
					pre.setString(2, e[f]);
					pre.setInt(2, 300);
					//Random rand = new Random();
					int s = rand.nextInt(4)+1;
					pre.setString(3, e[s]);
					int m = rand.nextInt(4)+1;
					pre.setString(4, e[m]);
					int t = rand.nextInt(4)+1;
					pre.setString(5, e[t]);
					int w = rand.nextInt(4)+1;
					pre.setString(6, e[w]);
					int tr = rand.nextInt(4)+1;
					pre.setString(7, e[tr]);
					int ff = rand.nextInt(4)+1;
					pre.setString(8, e[ff]);
					int st = rand.nextInt(4)+1;
					pre.setString(9, e[st]); 
					
					double sales = 0.00;
					//pre.setDouble(4, sales);
					pre.setDouble(10, sales);*/
					double sales = 0;
					pre.setDouble(10, sales);
					pre.execute();				
				}		
			}
		}
	}

}
