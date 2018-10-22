import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;


public class mainPanel extends JPanel{
	/*
	 * This is the master panel that actually contains everything.
	 */
	static JPanel c;
	/*
	 * These are placeholder variables that should be replaced by actual database objects.
	 */
	static String[][] employees;
	static String[][] hours;
	static String[][] sales;
	
	String str;
	
	/*
	 * objects just for the Rooms DB.
	 */
	RoomsDB rooms = new RoomsDB();
	JTable roomTable;
	JCheckBox cableCB;
	JCheckBox internetCB;
	JComboBox<Object> roomCB;
	JPanel roomsP;
	JScrollPane roomScrollPane;
	
	/*
	 * objects for maintenance DB.
	 */
	MaintenanceDB maintenance = new MaintenanceDB();
	JComboBox<Object> maintReqRoomCB;
	JComboBox<Object> maintReqStatusCB;
	JTextField maintReqText;
	JTable maintTable;
	TableRowSorter<DefaultTableModel> sorter;
	
	/*
	 * objects just for the Registration DB.
	 */
	RegistrationDB registration = new RegistrationDB();
	SwingCalendar beginDateCalendar;
	SwingCalendar endDateCalendar;
	JTextField checkInIDFNameField;
	JTextField checkInIDLNameField;
	JTextField checkOutIDFNameField;
	JTextField checkOutIDLNameField;
	JLabel checkOutMessage;
	JLabel checkInTime;
	JPanel checkInP;
	
	/*
	 * objects just for the Payroll DB.
	 */
	PayrollDB payroll = new PayrollDB();
	JTable employeeTable;
	JPanel employeeP;
	JScrollPane employeeScrollPane;
	JTable salesTable;
	/////////////////
	JPanel salesP;
	JScrollPane roomScrollPane2;
	JComboBox<Object> roomCB2;


	
	public mainPanel() throws ClassNotFoundException, SQLException{
		//Establish main layout of our GUI
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		//Making a Menu bar
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = new JMenu ("File"); //The "File" menu which can be accessed via alt+F
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit"); //The "Exit" button within the "File" menu
		exitMenuItem.setMnemonic(KeyEvent.VK_E); //Alt+F+E
		exitMenuItem.setToolTipText("Exit application."); //neat tooltip when you hover over
		exitMenuItem.addActionListener((ActionEvent event) -> { //actionlistener to close the program
			System.exit(0);
		});
		
		file.add(exitMenuItem); //add "Exit" to the "File" menu
		menubar.add(file); //add "File" to the menu bar itself
		
		add(menubar, BorderLayout.NORTH); //add the menubar to the top - Border Layout happens to work well with this
		//Onto the other elements. The Menu bar should be consistent throughout the program.
		
		c = new JPanel();//This CardLayout is how we essentially 'switch screens'
		c.setPreferredSize(new Dimension(800,800));
		c.setLayout(new CardLayout(1, 1)); //Purely cosmetic borders
		
		/*
		 * Login Screen, yo
		 */
		JPanel loginP = new JPanel();
		loginP.setLayout(new GridBagLayout());
		loginP.setBackground(Color.WHITE);
		
		JPanel loginForm = new JPanel();
		loginForm.setLayout(new GridLayout(3,1));
		
		JPanel loginUser = new JPanel();
		loginUser.setLayout(new FlowLayout());
		loginUser.setBackground(Color.WHITE);
		
		JPanel loginPassword = new JPanel();
		loginPassword.setLayout(new FlowLayout());
		loginPassword.setBackground(Color.WHITE);
		
		JPanel loginEtcP = new JPanel();
		loginEtcP.setLayout(new FlowLayout());
		loginEtcP.setBackground(Color.WHITE);
		
		JLabel usernameLabel = new JLabel("Username: ", JLabel.RIGHT);
		JLabel passwordLabel = new JLabel("Password: ", JLabel.RIGHT);
		
		JTextField usernameField = new JTextField(20);
		JTextField passwordField = new JTextField(20);
		
		JLabel loginMessage = new JLabel();
		JButton loginButton = new JButton("Confirm");
		
		loginUser.add(usernameLabel);
		loginUser.add(usernameField);
		
		loginPassword.add(passwordLabel);
		loginPassword.add(passwordField);
		
		loginEtcP.add(loginMessage);
		loginEtcP.add(loginButton);
		
		loginForm.add(loginUser);
		loginForm.add(loginPassword);
		loginForm.add(loginEtcP);
		
		loginP.add(loginForm);
		
		loginButton.addActionListener(new LoginListener());
		
		c.add(loginP, "Login");
		
		/*
		 * Main screen elements. This is done by nesting 2 gridlayouts.
		 */
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,1));
		JPanel pInP = new JPanel();
		pInP.setLayout(new GridLayout(1,4));
		
		//Create Buttons
		JButton payrollB = new JButton("Payroll");
		JButton roomsB = new JButton("Rooms");
		JButton maintB = new JButton("Maintenance");
		JButton entertainB = new JButton("Events");

		//Add them to the horizontal Grid
		pInP.add(payrollB);
		pInP.add(roomsB);
		pInP.add(maintB);
		pInP.add(entertainB);
		//Add horizontal Grid to a vertical Grid
		p.add(pInP);
		//Bottom button
		JButton checkinout = new JButton("Check-In/Out");
		p.add(checkinout);
		//Add it all as 1 card
		c.add(p, "main");
		//Add that card to the overall layout
		add(c,BorderLayout.CENTER);
		//Add listeners
		payrollB.addActionListener(new ButtonListener());
		//payrollB.addActionListener(new payrollButtonListener());
		roomsB.addActionListener(new roomsButtonListener());
		maintB.addActionListener(new ButtonListener());
		entertainB.addActionListener(new ButtonListener());
		checkinout.addActionListener(new ButtonListener());
		
		/*
		 * Screen: "Payroll Menu"
		 */
		
		//Pretty self-explanatory, just like the main menu.
		JPanel payrollP = new JPanel();
		payrollP.setLayout(new GridLayout(2,1));
		JPanel payPInP = new JPanel();
		payPInP.setLayout(new GridLayout(1,2));
		
		JButton employeesB = new JButton("Employees");
		JButton salesB = new JButton("Sales");
		
		payPInP.add(employeesB);
		payPInP.add(salesB);
		
		JButton backB = new JButton("Back");
		
		payrollP.add(payPInP);
		payrollP.add(backB);
		
		c.add(payrollP, "Payroll");
		
		employeesB.addActionListener(new ButtonListener());
		salesB.addActionListener(new ButtonListener());
		backB.addActionListener(new BackButtonListener());
		
		/*
		 * Screen: "Employees Menu"
		 */
		
		JPanel employeeP = new JPanel();
		employeeP.setLayout(new GridLayout(2,1));
		
		//JTable employeeTable = new JTable();
		///////////////////////////////////////////////////////////////////////////
		ResultSet payrollSet = payroll.displayPayroll();
		JTable employeeTable = new JTable(buildTableModelp(payrollSet));
		employeeScrollPane = new JScrollPane(employeeTable);

		//JTable workSchedule = new JTable();
		
		JButton employeeBackB = new JButton("Back");
		employeeBackB.addActionListener(new payrollBackListener());
		//employeeP.add(x);
		//x.addActionListener(new payrollButtonListener());
		employeesB.addActionListener(new payrollButtonListener());

		employeeP.add(employeeScrollPane);
		//employeeP.add(workSchedule);
		employeeP.add(employeeBackB);
		//employeeP.add(employeesB);
		
		c.add(employeeP, "Employees");
		
		/*
		 * Screen: "Sales"
		 */
		
		JPanel salesP = new JPanel();
		salesP.setLayout(new BorderLayout());
		JButton button = new JButton("UPDATE PRICE");
		//updatePrice.addActionListener(new ButtonListener());
		
		JPanel chooseR = new JPanel(); //choose room to update price
		JPanel updateP = new JPanel(); //update price panel

		//JTable salesTable = new JTable();
		
		JLabel salesTitle = new JLabel("Sales");
		salesTitle.setHorizontalAlignment(JLabel.CENTER);
		//////////////////////////////////////////////////////////////////////
		//roomsP2 = new JPanel();
		salesP.setLayout(new BorderLayout());
		
		ResultSet roomSet2 = rooms.displayRooms();
		salesTable = new JTable(buildTableModel(roomSet2));
		roomScrollPane2 = new JScrollPane(salesTable);
		
		JPanel roomInfoP2 = new JPanel();
		
		roomCB2 = new JComboBox<Object>(buildComboBoxModel(salesTable));
		//roomCB2.addItemListener(new floorBoxListener2());
		
		//cableCB = new JCheckBox("Cable");
		
		//internetCB = new JCheckBox("Internet");
		
		//JButton roomChangeButton = new JButton("Change");
		//roomChangeButton.addActionListener(new roomChangeListener());
		
		JButton roomsBackB = new JButton("Back");
		roomsBackB.addActionListener(new BackButtonListener());
		
		roomInfoP2.add(roomCB2);
		//roomInfoP2.add(cableCB);
		//roomInfoP2.add(internetCB);
		//roomInfoP2.add(roomChangeButton);
		salesP.add(roomScrollPane2, BorderLayout.WEST);
		salesP.add(roomInfoP2, BorderLayout.EAST);
		salesP.add(roomsBackB, BorderLayout.SOUTH);
		button.add(salesTable);
		//button.addActionListener(new comboSelect());
		//c.add(roomsP, "Rooms");
		
		//updatePrice = new JTextField();
		
		//chooseR.add(roomCB);
		//salesP.add(roomScrollPane, BorderLayout.WEST);
		//roomCB.addItemListener(new floorBoxListener());
		//JButton changePriceButton = new JButton("Change Price");
		//salesP.add(chooseR);
		//changePriceButton.addActionListener(new roomChangeListener());
		//////////////////////////////////////////////////////////////////////
		
		JButton salesBackB = new JButton("Back");
		salesBackB.addActionListener(new payrollBackListener());
		
		salesP.add(salesTitle, BorderLayout.NORTH);
		salesP.add(salesBackB, BorderLayout.SOUTH);
		salesP.add(salesTable, BorderLayout.CENTER);
		salesP.add(button, BorderLayout.WEST);
		//salesP.add(price, BorderLayout.EAST);
		
		c.add(salesP, "Sales");
		
		/*
		 * Screen: "Rooms"
		 */
		
		roomsP = new JPanel();
		roomsP.setLayout(new BorderLayout());
		
		ResultSet roomSet = rooms.displayRooms();
		roomTable = new JTable(buildTableModel(roomSet));
		roomScrollPane = new JScrollPane(roomTable);
		
		JPanel roomInfoP = new JPanel();
		
		roomCB = new JComboBox<Object>(buildComboBoxModel(roomTable));
		roomCB.addItemListener(new floorBoxListener());
		
		cableCB = new JCheckBox("Cable");
		
		internetCB = new JCheckBox("Internet");
		
		JButton roomChangeButton = new JButton("Change");
		roomChangeButton.addActionListener(new roomChangeListener());
		
		//JButton roomsBackB = new JButton("Back");
		//roomsBackB.addActionListener(new BackButtonListener());
		
		JButton roomsBackB2 = new JButton("Back");
		roomsBackB2.addActionListener(new BackButtonListener());
		
		roomInfoP.add(roomCB);
		roomInfoP.add(cableCB);
		roomInfoP.add(internetCB);
		roomInfoP.add(roomChangeButton);
		
		roomsP.add(roomScrollPane, BorderLayout.WEST);
		roomsP.add(roomInfoP, BorderLayout.EAST);
		//roomsP.add(roomsBackB, BorderLayout.SOUTH);
		roomsP.add(roomsBackB2, BorderLayout.SOUTH);
		
		c.add(roomsP, "Rooms");

		/*
		 * Screen: "Maintenance Menu"
		 */
		JPanel maintP = new JPanel();
		maintP.setLayout(new BorderLayout());
		
		JPanel maintRequestP = new JPanel();
		maintRequestP.setLayout(new GridLayout(1,3));
		
		JLabel maintLabel = new JLabel("Maintenance Tasks");
		maintLabel.setFont(new Font("Times New Roman", Font.PLAIN, 44));
		maintLabel.setHorizontalAlignment(JLabel.CENTER);
		
		ResultSet maintSet = maintenance.displayTickets();
		maintTable = new JTable(buildTableModelDefault(maintSet));
		maintTable.setFillsViewportHeight(true);
		
		sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)maintTable.getModel());
		maintTable.setRowSorter(sorter);
		
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		
		JScrollPane maintScrollPane = new JScrollPane(maintTable);
		
		JButton maintBackB = new JButton("Back");
		maintBackB.addActionListener(new BackButtonListener());
		
		JButton maintRequestB = new JButton("Maintenance Request");
		maintRequestB.addActionListener(new ButtonListener());
		
		JButton maintDeleteB = new JButton("Remove Request");
		maintDeleteB.addActionListener(new MaintRemoveListener());
		
		maintP.add(maintLabel, BorderLayout.NORTH);
		maintP.add(maintScrollPane, BorderLayout.CENTER);
		maintRequestP.add(maintBackB);
		maintRequestP.add(maintRequestB);
		maintRequestP.add(maintDeleteB);
		maintP.add(maintRequestP, BorderLayout.SOUTH);
		
		c.add(maintP, "Maintenance");
		/*
		 * Screen: "Maintenance Request"
		 */
		JPanel maintReqP = new JPanel();
		maintReqP.setLayout(new BorderLayout());
		
		JPanel maintReqBotP = new JPanel();
		maintReqBotP.setLayout(new GridLayout(1,2));
		
		JPanel maintReqRoomP = new JPanel();
		maintReqRoomP.setLayout(new FlowLayout());
		
		JPanel maintReqStatusP = new JPanel();
		maintReqStatusP.setLayout(new FlowLayout());
		
		JPanel maintReqTextP = new JPanel();
		maintReqTextP.setLayout(new FlowLayout());
		
		JLabel maintReqLabel = new JLabel("Enter Request:");
		maintReqLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel maintReqRoomL = new JLabel("Room:");
		maintReqRoomCB = new JComboBox<Object>(buildComboBoxModel(roomTable));
		
		JLabel maintReqStatusL = new JLabel("Severity:");
		String[] statuses = {"1","2","3","4"};
		maintReqStatusCB = new JComboBox<Object>(statuses);
		
		maintReqText = new JTextField(20);
		
		JButton maintReqBackB = new JButton("Back");
		JButton maintReqSubmitB = new JButton("Submit");
		
		maintReqBackB.addActionListener(new maintReqBackListener());
		maintReqSubmitB.addActionListener( new MaintRequestListener());
		
		maintReqRoomP.add(maintReqRoomL);
		maintReqRoomP.add(maintReqRoomCB);
		
		maintReqStatusP.add(maintReqStatusL);
		maintReqStatusP.add(maintReqStatusCB);
		
		maintReqTextP.add(maintReqRoomP);
		maintReqTextP.add(maintReqStatusP);
		maintReqTextP.add(maintReqText);
		
		maintReqBotP.add(maintReqBackB);
		maintReqBotP.add(maintReqSubmitB);
		
		maintReqP.add(maintReqLabel, BorderLayout.NORTH);
		maintReqP.add(maintReqBotP, BorderLayout.SOUTH);
		maintReqP.add(maintReqTextP, BorderLayout.CENTER);
		
		c.add(maintReqP, "Maintenance Request");
		/*
		 * Check In/Check Out
		 */
		JPanel checkP = new JPanel();
		checkP.setLayout(new GridLayout(2,1));
		JPanel checkPInP = new JPanel();
		checkPInP.setLayout(new GridLayout(1,2));
		
		JButton checkInB = new JButton("Check-In");
		JButton checkOutB = new JButton("Check-Out");
		
		checkPInP.add(checkInB);
		checkPInP.add(checkOutB);
		
		checkP.add(checkPInP);
		
		JButton checkBackB = new JButton("Back");
		checkP.add(checkBackB);
		
		c.add(checkP, "Check-In/Out");
		
		checkInB.addActionListener(new checkInButtonListener());
		checkOutB.addActionListener(new ButtonListener());
		checkBackB.addActionListener(new BackButtonListener());

		/*
		 * "Event Spaces"
		 */
		JPanel eventP = new JPanel();
		eventP.setLayout(new GridLayout(2,1));
		JPanel eventDataP = new JPanel();
		eventDataP.setLayout(new GridLayout(1,2));
		
		JLabel eventMap = new JLabel("MAP GOES HERE");
		eventMap.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel eventSchedule = new JLabel("Schedule:");
		
		JButton eventBackB = new JButton("Back");
		eventBackB.addActionListener(new BackButtonListener());
		
		eventDataP.add(eventMap);
		eventDataP.add(eventSchedule);
		eventP.add(eventDataP);
		eventP.add(eventBackB);
		
		c.add(eventP, "Events");
		/*
		 * Check-In
		 */
		//ResultSet registrationSet = registration.displayBooking();
		
		checkInP = new JPanel();
		checkInP.setLayout(new BorderLayout());
		
		JPanel checkInDateP = new JPanel();
		checkInDateP.setLayout(new GridLayout(4,1));
		
		JPanel checkInGuestP = new JPanel();
		checkInGuestP.setLayout(new GridLayout(2,1));
		
		JPanel firstNameP = new JPanel();
		firstNameP.setLayout(new FlowLayout());
		
		JPanel lastNameP = new JPanel();
		lastNameP.setLayout(new FlowLayout());
		
		JPanel checkInBottomP = new JPanel();
		checkInBottomP.setLayout(new GridLayout(1,2));
		

		JLabel beginDateLabel = new JLabel("Start Date:");
		
		beginDateCalendar = new SwingCalendar();
		
		JLabel endDateLabel = new JLabel("End Date:");
		
		endDateCalendar = new SwingCalendar();
		
		JLabel checkInIDFName = new JLabel("First Name:",SwingConstants.RIGHT);
		checkInIDFNameField = new JTextField(20);
		
		JLabel checkInIDLName = new JLabel("Last Name:",SwingConstants.RIGHT);
		checkInIDLNameField = new JTextField(30);
		
		JButton checkInBackB = new JButton("Back");
		checkInBackB.addActionListener(new checkInOutBackListener());
		
		JButton checkInConfirmB = new JButton("Confirm");
		checkInConfirmB.addActionListener(new CheckInListener());
		
		checkInDateP.add(beginDateLabel);
		checkInDateP.add(beginDateCalendar);
		checkInDateP.add(endDateLabel);
		checkInDateP.add(endDateCalendar);
		
		firstNameP.add(checkInIDFName);
		firstNameP.add(checkInIDFNameField);
		
		lastNameP.add(checkInIDLName);
		lastNameP.add(checkInIDLNameField);
		
		checkInGuestP.add(firstNameP);
		checkInGuestP.add(lastNameP);
		
		checkInBottomP.add(checkInBackB);
		checkInBottomP.add(checkInConfirmB);
		
		checkInP.add(roomScrollPane, BorderLayout.NORTH);
		checkInP.add(checkInDateP, BorderLayout.WEST);
		checkInP.add(checkInGuestP, BorderLayout.CENTER);
		checkInP.add(checkInBottomP, BorderLayout.SOUTH);
		c.add(checkInP, "Check-In");

		/*
		 * "Check-In Time"
		 */
		JPanel checkInTimeP = new JPanel();
		checkInTimeP.setLayout(new GridLayout(2,1));
		
		checkInTime = new JLabel("Check-In Success! Enjoy your Stay!");
		checkInTime.setHorizontalAlignment(JLabel.CENTER);
		
		JButton checkInCloseB = new JButton("Reset");
		checkInCloseB.addActionListener(new BackButtonListener());
		
		checkInTimeP.add(checkInTime);
		checkInTimeP.add(checkInCloseB);
		
		c.add(checkInTimeP, "Check-In Time");
		/*
		 * "Check-Out ID"
		 */
		JPanel checkOutIDP = new JPanel();
		checkOutIDP.setLayout(new GridLayout(2,1));
		
		JPanel checkOutIDCredP = new JPanel();
		checkOutIDCredP.setLayout(new FlowLayout());
		
		JLabel checkOutIDFName = new JLabel("First Name:");
		checkOutIDFNameField = new JTextField(20);
		JLabel checkOutIDLName = new JLabel("Last Name:");
		checkOutIDLNameField = new JTextField(30);
		//JLabel checkOutIDNum = new JLabel("ID Number:");
		//JTextField checkOutIDNumField = new JTextField(9);
		JButton checkOutIDConfirm = new JButton("Submit");
		checkOutIDConfirm.addActionListener(new IDOutListener());
		checkOutMessage = new JLabel();
		JButton checkOutIDBackB = new JButton("Back");
		checkOutIDBackB.addActionListener(new checkInOutBackListener());
		
		checkOutIDCredP.add(checkOutIDFName);
		checkOutIDCredP.add(checkOutIDFNameField);
		checkOutIDCredP.add(checkOutIDLName);
		checkOutIDCredP.add(checkOutIDLNameField);
		//checkOutIDCredP.add(checkOutIDNum);
		//checkOutIDCredP.add(checkOutIDNumField);
		checkOutIDCredP.add(checkOutIDConfirm);
		checkOutIDCredP.add(checkOutMessage);
		
		checkOutIDP.add(checkOutIDCredP);
		checkOutIDP.add(checkOutIDBackB);
		
		c.add(checkOutIDP, "Check-Out");
		/*
		 * "Check-Out Time"
		 */
		JPanel checkOutTimeP = new JPanel();
		checkOutTimeP.setLayout(new GridLayout(2,1));
		
		JLabel checkOutTime = new JLabel("Thanks for Checking Out! Please come again!");
		checkOutTime.setHorizontalAlignment(JLabel.CENTER);
		
		JButton checkOutBackB = new JButton("Close");
		checkOutBackB.addActionListener(new BackButtonListener());
		
		checkOutTimeP.add(checkOutTime);
		checkOutTimeP.add(checkOutBackB);
		
		c.add(checkOutTimeP, "Check-Out Time");
	}
	public static DefaultTableModel buildTableModel(ResultSet rs) //stackoverflow/questions/10620448/
		throws SQLException{
		ResultSetMetaData metaData = rs.getMetaData();
		
		Vector<String> columnNames = new Vector<String>(); //get columnnames
		//int columnCount = metaData.getColumnCount(); this line has no use for us since we know how many columns there are
		for (int column = 1; column <= 6; column++){
			columnNames.add(metaData.getColumnName(column));
		}
		
		//table data
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()){
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= 6; columnIndex++){
				if(columnIndex == 4 || columnIndex == 5){
					if((int)rs.getObject(columnIndex)==1){
						vector.add("Yes");
					}
					else
						vector.add("No");
				}
				else
					vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		return new DefaultTableModel(data,columnNames){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
	}
	public static DefaultTableModel buildTableModelDefault(ResultSet rs)
			throws SQLException{
			ResultSetMetaData metaData = rs.getMetaData();
			
			Vector<String> columnNames = new Vector<String>();
			int columnCount = metaData.getColumnCount();
			for(int column = 1; column <= columnCount; column++) {
				columnNames.add(metaData.getColumnName(column));
			}
			
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}
			return new DefaultTableModel(data, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
	}
	
	
	//*******Payroll********/
	//////////////////////////////////////////////////////////////////////////////////////////////
	public static DefaultTableModel buildTableModelp(ResultSet rs) //stackoverflow/questions/10620448/
			throws SQLException{
			ResultSetMetaData metaData = rs.getMetaData();
			
			Vector<String> columnNames = new Vector<String>(); //get columnnames
			//int columnCount = metaData.getColumnCount(); this line has no use for us since we know how many columns there are
			for (int column = 1; column <= 9; column++){
				columnNames.add(metaData.getColumnName(column));
			}
			
			//table data
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()){
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= 9; columnIndex++){
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}
			return new DefaultTableModel(data,columnNames){
				@Override
				public boolean isCellEditable(int row, int column){
					return false;
				}
			};
		}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	public static Vector<Object> buildComboBoxModel(JTable table){
		Vector<Object> rooms = new Vector<Object>();
		for(int room = 0; room < table.getRowCount(); room++){
			rooms.add(table.getValueAt(room, 1));
		}
		return rooms;
	}
	/*
	 * placeholder listener until we get a working user database
	 */
	private class LoginListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "main");
		}
	}
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, event.getActionCommand());
		}
	}
	private class roomsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			roomsP.add(roomScrollPane, BorderLayout.WEST);
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c,  event.getActionCommand());
		}
	}
	///////////////////////////////
	private class roomsButtonListener2 implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			salesP.add(roomScrollPane2, BorderLayout.WEST);
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, event.getActionCommand());
		}
	}
	private class checkInButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			checkInP.add(roomScrollPane, BorderLayout.NORTH);
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, event.getActionCommand());
		}
	}
	private class BackButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "main");
		}
	}
	private class payrollBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Payroll");
		}
	}
	
	//*****Payroll********//
	///////////////////////////////////////////////////////////////////////////////////////////
	private class payrollButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			//employeeP.add(employeeScrollPane, BorderLayout.WEST);
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c,  event.getActionCommand());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	private class maintReqBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Maintenance");
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	private class entertainmentBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c,  "Entertainment");
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
	private class checkInOutBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In/Out");
		}
	}
	/*
	 * This displays the current state of the selected room ie: after you select a room in the combobox, the checkboxes get checked if the room has those features
	 */
	private class floorBoxListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event){
			// TODO Auto-generated method stub
			int thing = (Integer)event.getItem();
			for(int i = 0; i<roomTable.getRowCount(); i++){
				if ((int)roomTable.getValueAt(i,1) == thing){
					cableCB.setSelected(true);
					if(roomTable.getValueAt(i,3)=="Yes"){
						cableCB.setSelected(true);
					}
					else
						cableCB.setSelected(false);
					if(roomTable.getValueAt(i,4)=="Yes"){
						internetCB.setSelected(true);
					}
					else
						internetCB.setSelected(false);
				}
			}
		}
	}
	/*
	 * This details what happens when you try to change the attributes of a room.
	 */
	private class roomChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try {
				rooms.updateCable((int)roomCB.getSelectedItem(), cableCB.isSelected()); //This updates the database based on the status of the "Cable" checkbox
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				rooms.updateInternet((int)roomCB.getSelectedItem(), internetCB.isSelected()); //This updates the database based on the status of the "Internet" checkbox
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0;i < roomTable.getRowCount(); i++){
				if((int)roomTable.getValueAt(i,1) == (int)roomCB.getSelectedItem()){
					if(cableCB.isSelected()){
						roomTable.setValueAt("Yes", i, 3);
					}
					else
						roomTable.setValueAt("No", i, 3);
					if(internetCB.isSelected()){
						roomTable.setValueAt("Yes", i, 4);
					}
					else roomTable.setValueAt("No", i, 4);
					try {
						roomTable.setValueAt(rooms.getPrice(rooms.getRoomDetail((int)roomCB.getSelectedItem())), i, 5);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	private class CheckInListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(roomTable.getSelectedRow() == -1){
				return;
			}
			else if(beginDateCalendar.getDate().before(new Date(System.currentTimeMillis()))){
				return;
			}
			else if(beginDateCalendar.getDate().after(endDateCalendar.getDate())){
				return;
			}
			else{
				//try {
				//	registration.addGuest(checkInIDFNameField.getText(), checkInIDLNameField.getText(), (int)roomTable.getValueAt(roomTable.getSelectedRow(),0), (int)roomTable.getValueAt(roomTable.getSelectedRow(), 1), beginDateCalendar.getDate(), endDateCalendar.getDate());
				//} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				try {
					//ResultSet checkcheck = registration.displayBooking(beginDateModel.getDate(), endDateModel.getDate());
					//if (!checkcheck.isBeforeFirst()){
					//	checkInTime.setText("Sorry, this room and date are already booked.");
					if (!registration.addGuest(checkInIDFNameField.getText(), checkInIDLNameField.getText(), (int)roomTable.getValueAt(roomTable.getSelectedRow(),0), (int)roomTable.getValueAt(roomTable.getSelectedRow(), 1), beginDateCalendar.getDate(), endDateCalendar.getDate())){
						JOptionPane.showMessageDialog(null, "Room already reserved!","Alert",JOptionPane.WARNING_MESSAGE);
						return;
					}

				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CardLayout cardLayout = (CardLayout)(c.getLayout());
				cardLayout.show(c, "Check-In Time");
			}
		}
	}
	private class IDOutListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try {
				if(registration.removeGuest(checkOutIDFNameField.getText(), checkOutIDLNameField.getText())) {
					CardLayout cardLayout = (CardLayout)(c.getLayout());
					cardLayout.show(c, "Check-Out Time");
				}
				else
					checkOutMessage.setText("Check-Out Unsuccessful.");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class MaintRequestListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			int id = 0;
			if(maintReqText.getText() == "" || maintReqText.getText() == null) {
				return;
			}
			try {
				id = maintenance.addTicket((int)maintReqRoomCB.getSelectedItem(), Integer.parseInt((String)maintReqStatusCB.getSelectedItem()), maintReqText.getText());
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DefaultTableModel model = (DefaultTableModel) maintTable.getModel();
			Vector<Object> vector = new Vector<Object>();
			vector.add(id);
			vector.add(maintReqRoomCB.getSelectedItem());
			vector.add(maintReqStatusCB.getSelectedItem());
			vector.add(maintReqText.getText());
			model.addRow(vector);
			maintTable.setModel(model);
			sorter.setModel(model);
			maintReqText.setText("");
		}
	}
	private class MaintRemoveListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			if (maintTable.getSelectedRow()==-1) {
				maintTable.clearSelection();
				return;
			}
			System.out.println(maintTable.getValueAt(maintTable.getSelectedRow(),0));
			try {
				if(maintenance.removeTicket((int)maintTable.getValueAt(maintTable.getSelectedRow(), 0))) {
					DefaultTableModel model = (DefaultTableModel)maintTable.getModel();
					model.removeRow(maintTable.getSelectedRow());
					maintTable.setModel(model);
					sorter.setModel(model);
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			maintTable.clearSelection();
		}
	}
}
