import javax.swing.*;
import java.sql.*;

/*
 * Hotel Management Software (HMS)
 * @Author: Salman
 * @Author: Elven
 * @Author: Austin
 * @Author: Hasin
 *
 * Events Database that tracks events hosted by hotel
 *
*/

public class main extends JFrame{
	
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
