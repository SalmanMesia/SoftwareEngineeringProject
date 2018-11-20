import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Hotel Management Software (HMS)
 * @Author: Salman
 * @Author: Elven
 * @Author: Austin
 * @Author: Hasin
 *
 *Database that stores Maintenance tickets
 *Allows users to create tickets
 *Allows users to delete tickets
 *
*/



public class MaintenanceDB {

	private static Connection con;
	private static boolean hasData = false;
	
   /*
   * Prints tickets
   */

	public ResultSet displayTickets() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}	
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT * FROM maintenance");
		return res;
		
	}

   /*
   * Connects to Database
   */

	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Maintenance.db");
		initialize();
	}

   /*
   * Creates Database
   */

	private void initialize() throws SQLException {
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='maintenance'");
			if(!res.next()) {
				//Building the table
				Statement state2 = con.createStatement();

				state2.execute("CREATE TABLE maintenance("
					+ "TicketID INTEGER PRIMARY KEY," 
					+ "Room INT," 
					+ "Severity INT," 
					+ "Description VARCHAR(255));");
			}
		}
	}
	
   /*
	 * Updates Severity of a ticket
   */
	public void updateSeverity(int id, int level) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		PreparedStatement prep = con.prepareStatement("UPDATE maintenance SET Severity = " + level + " WHERE TicketID = " + id + ";");
		prep.executeUpdate();
	}
	
	//TO-DO: Remove Ticket

   /*
	//Change Existing Description
   */
	
	public void updateDescription(int id, String newDesc) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		String query = "UPDATE maintenance SET Description = '" + newDesc + "' WHERE TicketID = " + id + ";";
		PreparedStatement prep = con.prepareStatement(query);
		prep.executeUpdate();
	}
	
	//Add Ticket
	/*
	public void addTicket(int roomNumber, int sev, String desc) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		String query = "INSERT INTO maintenance(Room, Severity, Description) VALUES ('" + roomNumber + "', '" + sev + "', '"+ desc + "');";
		PreparedStatement prep = con.prepareStatement(query);
		prep.executeUpdate();
	}
	*/
	//ADD TICKET & RETURN ID

	public int addTicket(int roomNumber, int sev, String desc) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}

		String query = "INSERT INTO maintenance(Room, Severity, Description) VALUES ('" + roomNumber + "', '" + sev + "', '"+ desc + "');";
		PreparedStatement prep = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int success = prep.executeUpdate();

		if (success == 0) {
			throw new SQLException("Insertion failed.");
		}

		try(ResultSet generatedKeys = prep.getGeneratedKeys()){
			if(generatedKeys.next()) {
				return generatedKeys.getInt(1);
			}
			else {
				throw new SQLException("Insertion failed.");
			}
		}
	}
	
	//Removes a ticket

	public boolean removeTicket(int id) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		String query = "DELETE FROM maintenance WHERE TicketID = " + id + ";";
		PreparedStatement prep = con.prepareStatement(query);
		prep.executeUpdate();
		return true;
	}

	//Prints all tickets

	public String printLogs(ResultSet rs) throws SQLException {
		String result = "";

		while(rs.next()) {
			result += rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getString(4) +"\n";
		}
		return result;
	}
	
	public ResultSet descOrder(ResultSet rs) throws ClassNotFoundException, SQLException {

		if(con == null) {
			getConnection();
		}

		String query = "SELECT * FROM maintenance ORDER BY Severity DESC";
		PreparedStatement prep = con.prepareStatement(query);
		rs = prep.executeQuery();
		return rs;
	}
	

	public ResultSet ascOrder(ResultSet rs) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		String query = "SELECT * FROM maintenance ORDER BY Severity ASC";
		PreparedStatement prep = con.prepareStatement(query);
		rs = prep.executeQuery();
		return rs;
	}
}