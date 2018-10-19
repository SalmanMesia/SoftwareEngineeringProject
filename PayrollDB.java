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
	String shift[] = {"Morning", "Afternoon", "Night", "Off"};
	
	
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
					+ "ID INTEGER PRIMARY KEY," 
					+ "Employee INT," 
					+ "Sunday STRING," 
					+ "Monday STRING," 
					+ "Tuesday STRING,"
					+ "Wednesday STRING,"
					+ "Thursday STRING,"
					+ "Friday STRING,"
					+ "Saturday STRING," 
					+ "Wages INT);");
				
				PreparedStatement prep = con.prepareStatement("INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?);");

				for(int i=0; i<employees.length; i++) {
					
					Random rand = new Random();
					int s = rand.nextInt(4)+1;
					prep.setInt(2, i);
					prep.setString(3, shift[s]);
					int m = rand.nextInt(4)+1;
					prep.setString(4, shift[m]);
					int t = rand.nextInt(4)+1;
					prep.setString(5, shift[t]);
					int w = rand.nextInt(4)+1;
					prep.setString(6, shift[w]);
					int tr = rand.nextInt(4)+1;
					prep.setString(7, shift[tr]);
					int f = rand.nextInt(4)+1;
					prep.setString(8, shift[f]);
					int st = rand.nextInt(4)+1;
					prep.setString(9, shift[st]);
					
					int salary = 10;
					prep.setInt(10, salary);
					
					prep.execute();
				}
					
					



				}
		}
	}
	
	
	
	//returns info on room
		public ResultSet getpayrollDetail(int employee) throws ClassNotFoundException, SQLException {
			if(con == null) {
				getConnection();
			}
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT * FROM payroll WHERE Employee = '" + employee + "';");
			return res;
		}

		//prints room
		public String printPayroll(ResultSet test_rs) throws SQLException {
			return test_rs.getInt(1) + " " + test_rs.getInt(2) + " " + test_rs.getString(3) + " " + test_rs.getString(4) + " " 
					+ test_rs.getString(5) + " " + test_rs.getString(6)+ " " + test_rs.getString(7)+ " " +
					test_rs.getString(8)+" " +test_rs.getString(9)+" " + test_rs.getInt(10);
		}
	
	
	
	
	
		
	
	
}