import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//database needs (int floor (max10), int room (max 10), int beds (max 3), boolean cable, boolean internet, int price) 

public class RoomsDB {
	private static Connection con;
	private static boolean hasData = false;
	final int MAX_FLOOR = 10; //maximum number of floors in hotel
	final int MAX_ROOM = 10; //maximum number of rooms in hotel
	int cablePrice = 50;
	int internetPrice = 25;
	int oneBedPrice = 125;
	int twoBedPrice = 200;
	int threeBedPrice = 250; //includes Cable/Internet

	public ResultSet displayRooms() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT Floor, Room, Beds, Cable, Internet, Price FROM rooms");
		return res;
		
	}

	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Rooms.db");
		initialize();
	}

	private void initialize() throws SQLException {
		// TODO Auto-generated method stub
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='rooms'");
			if(!res.next()) {
				System.out.println("Building the User table with prepopulated values");
				//Building the table
				Statement state2 = con.createStatement();

				state2.execute("CREATE TABLE rooms("
					+ "ID INTEGER PRIMARY KEY," 
					+ "Floor INT," 
					+ "Room INT," 
					+ "Beds INT," 
					+ "Cable BOOLEAN," 
					+ "Internet BOOLEAN," 
					+ "Price INT);");


				PreparedStatement prep = con.prepareStatement("INSERT INTO rooms VALUES(?,?,?,?,?,?,?);");
				
				//populate floors and rooms
				for(int i = 1; i <= MAX_FLOOR; i++) {
					for(int j = 1; j <= MAX_ROOM; j++) {
						prep.setInt(2, i); //set floor
						prep.setInt(3, i*100 + j); //set room number
						if(j <= 4) {
							prep.setInt(4, 1);
						}
						else if (j <= 8) {
							prep.setInt(4, 2);
						}
						else {
							prep.setInt(4, 3);
						}

						prep.setBoolean(5, false); //No Cable
						prep.setBoolean(6, false); //No Internet
						if(j <= 4) {
							prep.setInt(7, oneBedPrice);
						}
						else if (j <= 8) {
							prep.setInt(7, twoBedPrice);
						}
						else {
							prep.setInt(7, threeBedPrice);
						}
						prep.execute();
					}
				}				
			}
		}
	}
	
	//Updates Price of a given room
	public void updatePrice(int roomNumber, int price) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("UPDATE rooms SET Price = " + price + " WHERE Room = " + roomNumber + ";");
		prep.executeUpdate();
	}
	
	//change cable price
	public void setCablePrice(int newPrice) {
		cablePrice = newPrice;
	}
	
	//change internet price
	public void setInternetPrice(int newPrice) {
		internetPrice = newPrice;
	}
	
	public String getPrice(ResultSet room) throws ClassNotFoundException, SQLException{
		return room.getString(7);
	}
	
//setRoomAvailability
/*
	public void updateAvail(int roomNumber, boolean available) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		int avail = (available) ? 1 : 0;
		PreparedStatement prep = con.prepareStatement("UPDATE rooms SET Available = " + avail + " WHERE Room = " + roomNumber + ";");
		prep.executeUpdate();
	}
*/	
	
	public void updateCable(int roomNumber, boolean cableVal) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		int cable =  (cableVal) ?  1 : 0;
		String query = "UPDATE rooms SET cable = " + cable + " WHERE Room = " + roomNumber + ";";
		PreparedStatement prep = con.prepareStatement(query);
		prep.executeUpdate();
		query = "SELECT Price FROM rooms WHERE Room = " + roomNumber + ";"; 
		Statement s = con.createStatement();
		ResultSet res = s.executeQuery(query);
		int newPrice = res.getInt(1);
		updatePrice(roomNumber, newPrice = (cableVal) ? newPrice + cablePrice : newPrice - cablePrice); //Increase or decrease price
	}
	
	public void updateInternet(int roomNumber, boolean internetVal) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		int internet =  (internetVal) ?  1 : 0;
		String query = "UPDATE rooms SET Internet = " + internet + " WHERE Room = " + roomNumber + ";";
		PreparedStatement prep = con.prepareStatement(query);
		prep.executeUpdate();
		query = "SELECT Price FROM rooms WHERE Room = " + roomNumber + ";"; 
		Statement s = con.createStatement();
		ResultSet res = s.executeQuery(query);
		int newPrice = res.getInt(1);
		updatePrice(roomNumber, newPrice = (internetVal) ? newPrice + internetPrice : newPrice - internetPrice); //Increase or decrease price
	}
	
	//returns info on room
	public ResultSet getRoomDetail(int roomNumber) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT * FROM rooms WHERE Room = '" + roomNumber + "';");
		return res;
	}

	//prints room
	public String printRoom(ResultSet test_rs) throws SQLException {
		return test_rs.getInt(1) + " " + test_rs.getInt(2) + " " + test_rs.getInt(3) + " " + test_rs.getInt(4) + " " 
				+ test_rs.getBoolean(5) + " " + test_rs.getBoolean(6)+ " " + test_rs.getInt(7);
	}
}