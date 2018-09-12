import javax.swing.*;

public class gui extends JFrame{

	public static void main(String[] args) {
		JFrame frame = new JFrame("HMS - Hotel Management System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(new mainPanel());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
