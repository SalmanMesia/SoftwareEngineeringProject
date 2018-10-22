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
		String startDateString = "2018/10/11";
		String endDateString = "2018/11/09";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = df.parse(startDateString);
		Date endDate = df.parse(endDateString);
		guest.addGuest("Amy", "Chu", 5, 1006, startDate, endDate);
		while(res.next()) {
			System.out.println(res.getString(1) + " " + res.getString(2) + " " + res.getInt(3) + " " + res.getInt(4) 
			+	" " + res.getObject(5) + " " + res.getObject(6) + " " + res.getInt(7));
		}
		test_rs = test.getRoomDetail(105);
/*
 * CREATE TEST DATES HERE BY REPLACING YYYY/MM/DD and UNCOMMENT
 * 		String startDateString = "YYYY/MM/DD";
		String endDateString = "YYYY/MM/DD""; 
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd"); //DON'T REPLACE THIS
		Date startDate = df.parse(startDateString);
		Date endDate = df.parse(endDateString);

 */
		System.out.println(test.printRoom(test_rs));
		test_rs = guest.displayBooking(startDate, endDate);
		if (!test_rs.isBeforeFirst() ) {    
		    System.out.println("No data"); 
		} while(test_rs.next()) {
			System.out.println(test_rs.getString(1) + " " + test_rs.getString(2) + " " + test_rs.getInt(3) + " " + test_rs.getInt(4) 
			+	" " + test_rs.getObject(5) + " " + test_rs.getObject(6) + " " + test_rs.getInt(7));
		}
	}
}
