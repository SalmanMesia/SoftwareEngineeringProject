import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PayrollDB{
	private static Connection con;
	private static boolean hasData = false;
	final int employees[] = new int[20];
	final String shift[] = {"Morning", "Afternoon", "Night", "Off"};
	RoomsDB r = new RoomsDB();
	ResultSet rss = null;
	
	public ResultSet displayPayroll() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT Employee, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Wages FROM payroll");
		return res;
		
	}
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Payroll.db");
		initialize();
	}
	
	private void initialize() throws SQLException {
		// TODO Auto-generated method stub
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='payroll'");
			if(!res.next()) {
				System.out.println("Building the User table with prepopulated values");
				//Building the table
				Statement state2 = con.createStatement();

				state2.execute("CREATE TABLE payroll("
					+ "Employee INTEGER PRIMARY KEY," 
					+ "Sunday varchar(255)," 
					+ "Monday varchar(255)," 
					+ "Tuesday varchar(255),"
					+ "Wednesday varchar(255),"
					+ "Thursday varchar(255),"
					+ "Friday varchar(255),"
					+ "Saturday varchar(255)," 
					+ "Wages INT);");
				
				//String sql = "INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?);"
				PreparedStatement pre = con.prepareStatement("INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?);");
				//Statement pre = con.createStatement();
				//pre.execute("INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?);");
				
				for(int i=1; i<20; i++) {
					
					//pre.setInt(2, i);

					Random rand = new Random();
					int s = rand.nextInt(3)+1;
					pre.setString(2, shift[s]);
					int m = rand.nextInt(3)+1;
					pre.setString(3, shift[m]);
					int t = rand.nextInt(3)+1;
					pre.setString(4, shift[t]);
					int w = rand.nextInt(3)+1;
					pre.setString(5, shift[w]);
					int tr = rand.nextInt(3)+1;
					pre.setString(6, shift[tr]);
					int f = rand.nextInt(3)+1;
					pre.setString(7, shift[f]);
					int st = rand.nextInt(3)+1;
					pre.setString(8, shift[st]);
					
					int salary = 9;
					pre.setInt(9, salary);
					
					pre.execute();
				
				}
					
					
				//display(rss);


				}
		}
		//con.commit();
	}
	
	
		public ResultSet getpayrollDetail(int employee) throws ClassNotFoundException, SQLException {
			if(con == null) {
				getConnection();
			}
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT * FROM payroll WHERE Employee = '" + employee + "';");
			return res;
		}

		//prints payroll
		public String printPayroll(ResultSet test_rs) throws SQLException {
			return test_rs.getInt(1) + " " + test_rs.getString(2) + " " + test_rs.getString(3) + " " 
					+ test_rs.getString(4) + " " + test_rs.getString(5)+ " " + test_rs.getString(6)+ " " +
					test_rs.getString(7)+" " +test_rs.getString(8)+" " + test_rs.getInt(9);
		}
		
	
		
	
	
}