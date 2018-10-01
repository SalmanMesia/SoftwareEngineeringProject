import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		
		RoomsDB	test = new RoomsDB();
		ResultSet rs;
		ResultSet test_rs;
		
		
		rs = test.displayRooms();
		test.updatePrice(1010, 500);
		test.updateCable(1009, true);
		test.updateInternet(1008, true);
		
		while(rs.next()) {
			System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getBoolean(4) + " " + rs.getBoolean(5)+ " " + 
		rs.getInt(6));
		}
		
		RegistrationDB guest = new RegistrationDB();
		ResultSet res;
		res = guest.displayGuests();
		String startDateString = "2018/09/30";
		String endDateString = "2018/10/23";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = df.parse(startDateString);
		Date endDate = df.parse(endDateString);
//		guest.addGuest("Alice", "Bob", 3, 304, startDate, endDate);
		while(res.next()) {
			System.out.println(res.getString(1) + " " + res.getString(2) + " " + res.getInt(3) + " " + res.getInt(4) 
			+	" " + res.getObject(5) + " " + res.getObject(6) + " " + res.getInt(7));
		}
		test_rs = test.getRoomDetail(105);

		System.out.println(test.printRoom(test_rs));
	}
}
