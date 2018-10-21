import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		
		MaintenanceDB test = new MaintenanceDB();
		ResultSet rs;
//		test.updateDescription(1, "There is a fire in the room.");
//		test.removeTicket(1);
		rs = test.displayTickets();
		System.out.println(test.printLogs(rs));
		rs = test.descOrder(rs);
		System.out.println(test.printLogs(rs));
		rs = test.ascOrder(rs);
		System.out.println(test.printLogs(rs));
		int this_id = test.addTicket(605, 2, "I am 7");
		System.out.println("This should be latest ID entry: " + this_id);
		RegistrationDB guest = new RegistrationDB();
		ResultSet res;
		res = guest.displayGuests();
		String startDateString = "2018/10/11";
		String endDateString = "2018/11/09";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = df.parse(startDateString);
		Date endDate = df.parse(endDateString);
//		guest.addGuest("Tam", "Bam", 4, 404, startDate, endDate);
		guest.removeGuest("Robert", "Smith");
		System.out.println(guest.printGuests(res));
	}
}
