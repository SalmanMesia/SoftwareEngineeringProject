import javax.swing.*;
import java.sql.*;

public class gui extends JFrame{
	static RoomsDB rooms = new RoomsDB();
	static RegistrationDB registration = new RegistrationDB();
	static PayrollDB payroll = new PayrollDB();
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {}
		
		JFrame frame = new JFrame("HMS - Hotel Management System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(new mainPanel());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
