import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;


public class UserDB {

	private static Connection con;
	private static boolean hasData = false;
	final int users[] = new int[20];
	String pw[] = new String[20];
	ResultSet rss = null;
	
	public ResultSet displayUsers() throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT UserName, Password FROM users");
		return res;
		
	}
	
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:User.db");
		initialize();
	}
	
	private void initialize() throws SQLException {
		//SecureRandom random = new SecureRandom();
		//byte[] salt = new byte[16];
		//random.nextBytes(salt);
		//MessageDigest md = MessageDigest.getInstance("SHA-512");
		//md.update(salt);
		//byte[] hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
		// TODO Auto-generated method stub
		if(!hasData) {
			hasData = true;
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
			if(!res.next()) {
				System.out.println("Building the User table with prepopulated values");
				//Building the table
				Statement state2 = con.createStatement();
				state2.execute("CREATE TABLE users("
					+ "EmployeeID INTEGER PRIMARY KEY," 
					+ "UserName varchar(255),"
					+ "Password varchar(255));");
				
				PreparedStatement pre = con.prepareStatement("INSERT INTO users VALUES(?,?,?);");
				
				for(int i=0; i<users.length; i++) {
					
					pre.setString(2, populateNames(i));	
					//pre.setString(3, populateNames(i)+"2018");
					//byte[] hashedPassword = md.digest((populateNames(i)+"2018").getBytes(StandardCharsets.UTF_8));
					//String pwconvert = new String(salt, "UTF-8");
					pw[i]=getHash((populateNames(i)+"2018").getBytes(),"SHA-512");
					pre.setString(3, pw[i]);

					//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
					 //String hashedPassword = passwordEncoder.encode(password);
					pre.execute();				
				}		
			}
			
		}
	}
	
	public String populateNames(int num) {
		switch(num) {
			case 0: 
				return "Elven";
			case 1:
				return "Salman";
			case 2:
				return "Austin";
			case 3:
				return "Hasin";
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
	
	public String getHash(byte[] inputBytes, String algorithm) {
		String hashvalue = "";
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(inputBytes);
			byte digestedBytes[] = md.digest();
			hashvalue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		}
		catch(Exception e){
		}
		return hashvalue;
	}
	
	
	

}
