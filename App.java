import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		RoomsDB	test = new RoomsDB();
		ResultSet rs;
		
		rs = test.displayRooms();
		test.updatePrice(1010, 500);
		test.updateCable(1009, false);
		test.updateInternet(1008, false);
		test.updateAvail(1007, true);
		
		while(rs.next()) {
			System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getBoolean(4) + " " + rs.getBoolean(5)+ " " + 
		rs.getInt(6) + " " + rs.getBoolean(7));
		}
		
	}
}
