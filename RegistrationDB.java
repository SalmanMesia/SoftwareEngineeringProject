//Database for Check-In, Check-Out
//Use the Rooms DB to pull available rooms, floor
//This database will have the  rooms from RoomsDB, Date of First Day, Date of Last Day, and Price of Registration
//This Database will toggle availability accordingly to date

//id, check-in, check-out, available, guestInfo, number of guests
//we need a Database for each room 

//Floor: The Floor
//Room: The Room
//PRICE: Calculate Price from RoomsDB x Number of Days via Date Difference
//CHECK-IN: YYYY/MM/DD HH:MI:SS 
//CHECK-OUT: YYYY/MM/DD HH:MI:SS 
//GUESTS: Number of Guests

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RegistrationDB {

	private static Connection con;
	private static boolean hasData = false;

	public ResultSet displayGuests() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT fname, lname, Floor, Room, CheckIn, CheckOut, Price FROM guests");
		return res;	
	}

	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Guests.db");
		initialize();
	}
	
	private void initialize() throws SQLException {

		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='guests'");
			if(!res.next()) {
				//Building the table
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE guests("
					+ "id INT PRIMARY KEY," 
					+ "fname varchar(60),"
					+ "lname varchar(60),"
					+ "Floor INT," 
					+ "Room INT," 
					+ "CheckIn DATE,"
					+ "CheckOut DATE,"
					+ "Price INT);");
			}
		}
	}
	
	/*
	 * Returns a list of available rooms from RoomsDB
	 * Front Desk chooses a floor, room, Check-In, Check-Out
	 */
	public void addGuest(String first_name, String last_name, int Floor, int Room, Date checking, Date checkout) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		
//		Statement s = con.createStatement();
//        boolean result = s.execute("SELECT Room FROM guests WHERE CheckIn = " + formatter.format(checking) + " AND Room = " + Room + ";");
//        if(result) {
//			System.out.println("Already Reserved " + result);
//			return;
//		}
//		else {
			PreparedStatement prep = con.prepareStatement("INSERT INTO guests VALUES(?,?,?,?,?,?,?,?);");
			prep.setString(2, first_name);
			prep.setString(3, last_name);
			prep.setInt(4, Floor);
			prep.setInt(5, Room);
			prep.setObject(6, formatter.format(checking));
			prep.setObject(7, formatter.format(checkout));
			prep.setInt(8, 350);
			
			prep.execute();		
		}
//	}
	
	//Display Rooms boooked on certain date
	public ResultSet displayBooking(Date checking, Date checkout) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT fname, lname, Floor, Room, Check-In, Check-Out, Price FROM guests");
		return res;	
	}
}