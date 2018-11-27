import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


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


//TO-DO: Add Employee
//TO-DO: Delete Employee


public class PayrollDB {
 private static Connection con;
 private static boolean hasData = false;
 final int employees[] = new int[20];
 final String shift[] = {
  "Morning",
  "Afternoon",
  "Night",
  "Off"
 };
 final String days[] = {
  "Sunday",
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday"
 };
 final double hoursPerDay = 8;
 RoomsDB r = new RoomsDB();
 ResultSet rss = null;



 public ResultSet displayPayroll() throws ClassNotFoundException, SQLException {
  if (con == null) {
   getConnection();
  }


  Statement state = con.createStatement();
  ResultSet res = state.executeQuery("SELECT * FROM payroll");
  return res;

 }



 private void getConnection() throws ClassNotFoundException, SQLException {
  Class.forName("org.sqlite.JDBC");
  con = DriverManager.getConnection("jdbc:sqlite:Payroll.db");
  initialize();
 }



 private void initialize() throws SQLException {
  // TODO Auto-generated method stub
  if (!hasData) {
   hasData = true;
   Statement state = con.createStatement();
   ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='payroll'");
   if (!res.next()) {
    System.out.println("Building the User table with prepopulated values");
    //Building the table
    Statement state2 = con.createStatement();

    state2.execute("CREATE TABLE payroll(" +
     "EmployeeID INTEGER PRIMARY KEY," +
     "Name varchar(255)," +
     "Position varchar(255)," +
     "Sunday varchar(255)," +
     "Monday varchar(255)," +
     "Tuesday varchar(255)," +
     "Wednesday varchar(255)," +
     "Thursday varchar(255)," +
     "Friday varchar(255)," +
     "Saturday varchar(255)," +
     "Wages DOUBLE);");

    //String sql = "INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?);"
    PreparedStatement pre = con.prepareStatement("INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?,?);");
    //Statement pre = con.createStatement();
    //pre.execute("INSERT INTO payroll VALUES(?,?,?,?,?,?,?,?,?,?);");

    for (int i = 1; i < employees.length; i++) {

     //pre.setInt(2, i);
     pre.setString(2, populateNames(i));
     pre.setString(3, "Receptionist");
     Random rand = new Random();
     int s = rand.nextInt(3) + 1;
     pre.setString(4, shift[s]);
     int m = rand.nextInt(3) + 1;
     pre.setString(5, shift[m]);
     int t = rand.nextInt(3) + 1;
     pre.setString(6, shift[t]);
     int w = rand.nextInt(3) + 1;
     pre.setString(7, shift[w]);
     int tr = rand.nextInt(3) + 1;
     pre.setString(8, shift[tr]);
     int f = rand.nextInt(3) + 1;
     pre.setString(9, shift[f]);
     int st = rand.nextInt(3) + 1;
     pre.setString(10, shift[st]);

     double salary = 7.25;
     pre.setDouble(11, salary);
     pre.execute();
    }
   }
  }
 }


 public ResultSet getpayrollDetail(int employee) throws ClassNotFoundException, SQLException {
  if (con == null) {
   getConnection();
  }
  Statement state = con.createStatement();
  ResultSet res = state.executeQuery("SELECT * FROM payroll WHERE Employee = '" + employee + "';");
  return res;
 }


 //prints payroll
 public String printPayroll(ResultSet test_rs) throws SQLException {
  return test_rs.getInt(1) + " " + test_rs.getString(2) + " " + test_rs.getString(3) + " " + test_rs.getString(4) + " " +
   test_rs.getString(5) + " " + test_rs.getString(6) + " " + test_rs.getString(7) + " " +
   test_rs.getString(8) + " " + test_rs.getString(9) + " " + " " + test_rs.getString(10) + " " + test_rs.getDouble(11);
 }



 //returns weekly total of each employee
 //DEBUG COMMENTS
 public double weeklyTotal() throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  double total = 0;
  Statement state = con.createStatement();
  String query = "SELECT * FROM payroll;";
  ResultSet res = state.executeQuery(query);
  while (res.next()) {
   int daysWorked = 0;
   //DEBUG
   //System.out.println("Employee: " + res.getInt(1));
   for (int i = 4; i < 11; i++) {
    if (!res.getString(i).equals("Off")) {
     daysWorked++;
     //DEBUG
     //System.out.print(" Shift " +i+ ": " + res.getString(i));
    }
   }
   total += daysWorked * res.getDouble(11) * hoursPerDay;
   //DEBUG
   //System.out.println("\nTotal so far: " + total);
  }
  return total;
 }


 //Change SHIFT
 public boolean updateShift(String newShift, String changeDay, int empID) throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  boolean flag = false;
  boolean valid = false;

  for (int i = 0; i < shift.length; i++) {
   if (newShift.equals(shift[i])) {
    valid = true;
   }
  }
  //invalid shift
  if (!valid) {
   //DEBUG
   System.out.println("Invalid Shift Entry");
   return false;
  }
  valid = false;
  for (int i = 0; i < days.length; i++) {
   if (changeDay.equals(days[i]))
    valid = true;
  }
  //invalid day
  if (!valid) {
   //DEBUG
   System.out.println("Invalid Day");
   return false;
  }

  String query = "UPDATE payroll SET " + changeDay + " = '" + newShift + "' WHERE EmployeeID = '" + empID + "';";
  PreparedStatement prep = con.prepareStatement(query);
  prep.executeUpdate();
  flag = true;
  return flag;
 }


 //UPDATE WAGES
 public boolean updateWage(double newWage, int empID) throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  String query = "UPDATE payroll SET Wages = '" + newWage + "' WHERE EmployeeID = '" + empID + "';";
  PreparedStatement prep = con.prepareStatement(query);
  prep.executeUpdate();
  return true;
 }

 public boolean updatePosition(String newPosition, int empID) throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  String query = "UPDATE payroll SET Position = '" + newPosition + "' WHERE EmployeeID = '" + empID + "';";
  PreparedStatement prep = con.prepareStatement(query);
  prep.executeUpdate();
  return true;
 }


 //Add new Employee with position
 public boolean addEmployee(String empName, String newPosition, double newWage) throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  String query = "INSERT INTO payroll(Name, Position, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Wages) VALUES ('" +
   empName + "', '" + newPosition + "' ,'Off', 'Off', 'Off', 'Off', 'Off', 'Off', 'Off', '" + newWage + "');";
  PreparedStatement prep = con.prepareStatement(query);
  prep.executeUpdate();
  return true;
 }


 //remove employee
 public boolean removeEmployee(int empID) throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  String query = "DELETE FROM payroll WHERE EmployeeID = " + empID + ";";
  PreparedStatement prep = con.prepareStatement(query);
  prep.executeUpdate();
  return true;
 }


 //POPULATE NAMES FOR TESTING
 public String populateNames(int num) {
  switch (num) {
   case 0:
    return "John Doe";
   case 1:
    return "Jane Eyre";
   case 2:
    return "Bhola";
   case 3:
    return "James Franco";
   case 4:
    return "Nic Cage";
   case 5:
    return "Barack Obama";
   case 6:
    return "John Cena";
   case 7:
    return "Will Smith";
   case 8:
    return "Trump";
   case 9:
    return "Hackerman";
   case 10:
    return "Mark Jacob";
   case 11:
    return "Louis Smith";
   case 12:
    return "Peter Parker";
   case 13:
    return "Cough Syrup";
   case 14:
    return "Anonymous";
   case 15:
    return "NotAnEmployee";
   case 16:
    return "Jackie Chan";
   case 17:
    return "Gym teacher";
   case 18:
    return "William";
   case 19:
    return "Apple";
   default:
    return "Android";
  }
 }



 //Generate Sales report for the week
 public String generateReport() throws SQLException, ClassNotFoundException {
  if (con == null) {
   getConnection();
  }
  String report = "";
  int dayCount = 0;
  report += "--------------------------------------------------------\n " +
   "\t\t\t WEEKLY REPORT \t\t\t\n" +
   "--------------------------------------------------------\n";
  for (int i = 0; i < days.length; i++) {
   report += "=============================================================================================================================================================\n";
   String title = String.format("%-32s %-32s %-20s \t %-10s \t %-13s \t\t %-6s \n",
    "NAME", "POSITION", days[dayCount].toUpperCase() + " SHIFT", "PAY", "HOURS WORKED", "TOTAL");
   report += title;
   report += "=============================================================================================================================================================\n";

   String query = "SELECT * FROM payroll WHERE " + days[dayCount] + " = 'Morning' OR " +
    days[dayCount] + " = 'Afternoon' OR " + days[dayCount] + " = 'Night';";

   Statement state = con.createStatement();
   ResultSet res = state.executeQuery(query);

   while (res.next()) {
    String temp = String.format("%-32s %-32s %-20s \t $%-4.2f/hr \t %.1f \t\t\t $%.2f \n",
     res.getString(2), res.getString(3), res.getString(dayCount + 4), res.getDouble(11), hoursPerDay, res.getDouble(11) * hoursPerDay);
    report += temp;
   }
   dayCount++;
  }
  report += "\n=============================================================================================================================================================\n";
  report += "Total Pay Per Week: $" + weeklyTotal();
  return report;
 }
}