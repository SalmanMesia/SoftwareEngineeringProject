import javax.swing.*;
import java.sql.*;

public class gui extends JFrame{
	static RoomsDB rooms = new RoomsDB();
	static RegistrationDB registration = new RegistrationDB();
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		JFrame frame = new JFrame("HMS - Hotel Management System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(new mainPanel());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
