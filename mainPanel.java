import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.Timer;

//import mainPanel.LoginListener;
//import mainPanel.checkButtonListener;

//import mainPanel.LoginListener;
//import mainPanel.checkButtonListener;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.List;


/*
 * Hotel Management Software (HMS)
 * @Author: Salman
 * @Author: Elven
 * @Author: Austin
 * @Author: Hasin
 *
*/

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
	JTextField roomPriceField;
	JCheckBox preCableCB;
	JCheckBox preInternetCB;
	JComboBox<Object> preRoomCB;

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
	
	/*
	 * objects just for the Events DB.
	 */

	EventsDB events = new EventsDB();
	JTable eventsTable;
	JPanel eventsP;
	JScrollPane eventsScrollPane;
	JComboBox<Object> eventsCB;
	JComboBox<Object> daysCB;
	JComboBox<Object> hoursCB;
	JComboBox<Object> hoursCB2;
	JComboBox<Object> seatsCB;
	JComboBox<Object> dateCB;
	//JComboBox<Object> timeCB;
	SwingCalendar dateCalendar;
	JTextField fname;
	JTextField lname;
	double salesEvents;
	double eventTotal = 0;		//keeps track of total sales for events

	/*
	 * objects just for the Users DB.
	 */
	
	UserDB users = new UserDB();
	JTextField usernameField;
	JPasswordField passwordField;
	JTable userTable;
	JScrollPane userScrollPane;
	JFrame check;
	int count = 0;
	int delay = 1000;
	int countPassed = 60;
	Timer countdown;
	JLabel lblTimer;

	//private javax.swing.JLabel lblTimer;
	//private javax.swing.JPanel panel1;

	int attempts = 0;
	Timer timer;
	long startTime;
	JLabel label;
	
	
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
		
		JMenuItem logOutMenuItem = new JMenuItem("Log Out");
		logOutMenuItem.setMnemonic(KeyEvent.VK_O);
		logOutMenuItem.setToolTipText("Log out of current user.");
		logOutMenuItem.addActionListener((ActionEvent event) -> {
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Login");
			usernameField.setText("");
			passwordField.setText("");
		});
		
		file.add(logOutMenuItem);
		file.add(exitMenuItem); //add "Exit" to the "File" menu
		menubar.add(file); //add "File" to the menu bar itself
		
		add(menubar, BorderLayout.NORTH); //add the menubar to the top - Border Layout happens to work well with this
		//Onto the other elements. The Menu bar should be consistent throughout the program.
		
		c = new JPanel();//This CardLayout is how we essentially 'switch screens'
		c.setPreferredSize(new Dimension(1400,1000));
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
		
		usernameField = new JTextField(20);
		passwordField = new JPasswordField(20);
		ResultSet userSet = users.displayUsers();
		userTable = new JTable(buildTableModelu(userSet));
		userScrollPane = new JScrollPane(userTable);
		userTable.setVisible(false);
		
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
		loginP.add(userTable);
		
		loginButton.addActionListener(new LoginListener2());
		
		passwordField.addKeyListener(new LoginListener3());
		
		c.add(loginP, "Login");
		
		/*
		 * Main screen elements. This is done by nesting 2 gridlayouts.
		 */

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,1));
		JPanel pInP = new JPanel();
		pInP.setLayout(new GridLayout(1,3));
		
		//Create Buttons
		JButton payrollB = new JButton("Payroll");
		JButton maintB = new JButton("Maintenance");
		JButton entertainB = new JButton("Events");

		//Add them to the horizontal Grid
		pInP.add(payrollB);
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
		salesB.addActionListener(new roomsButtonListener());
		backB.addActionListener(new BackButtonListener());
		
		/*
		 * Screen: "Employees Menu"
		 */
		
		JPanel employeeP = new JPanel();
		employeeP.setLayout(new GridLayout(2,1));
		
		JPanel employeeBottomP = new JPanel();
		employeeBottomP.setLayout(new GridLayout(1,2));
		
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
		//employeesB.addActionListener(new payrollButtonListener());
		
		JButton generateReportB = new JButton("Generate Report");
		generateReportB.addActionListener(new PayrollGenerateListener());

		employeeBottomP.add(employeeBackB);
		employeeBottomP.add(generateReportB);
		
		employeeP.add(employeeScrollPane);

		//employeeP.add(workSchedule);
		employeeP.add(employeeBottomP);

		//employeeP.add(employeesB);
		
		c.add(employeeP, "Employees");
		
		/*
		 * Screen: "Sales"
		 */
		//Everything is called "rooms" because these elements used to be the "Rooms" screen

		roomsP = new JPanel();
		roomsP.setLayout(new BorderLayout());
		
		JPanel roomModP = new JPanel();
		roomModP.setLayout(new GridLayout(4,1));
		
		JPanel roomInfoP = new JPanel();
		
		JPanel roomComboBoxPanel = new JPanel();
		
		JPanel roomPricePanel = new JPanel();
		
		ResultSet roomSet = rooms.displayRooms();
		roomTable = new JTable(buildTableModel(roomSet))
			{
	            public boolean getScrollableTracksViewportWidth()
	            {
	                return getPreferredSize().width < getParent().getWidth();
	            }
	        };
		roomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		roomScrollPane = new JScrollPane(roomTable);
		roomScrollPane.setOpaque(false);
		roomScrollPane.getViewport().setOpaque(false);
		roomTable.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		        int r = roomTable.rowAtPoint(e.getPoint());
		        if (r >= 0 && r < roomTable.getRowCount()) {
		            roomTable.setRowSelectionInterval(r, r);
		        } else {
		            roomTable.clearSelection();
		        }

		        int rowindex = roomTable.getSelectedRow();
		        if (rowindex < 0)
		            return;
		        if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
		            JPopupMenu popup = null;
		            //In case we need a popup event when right clicking the table.
		            //popup.show(e.getComponent(), e.getX(), e.getY());
		        }
		    }
		});
		roomTable.setOpaque(false);
		((DefaultTableCellRenderer)roomTable.getDefaultRenderer(Object.class)).setOpaque(false);
		
		JLabel roomInfoLabel = new JLabel("Update Services:");
		
		
		
		JLabel roomModLabel = new JLabel("Select Room:");
		
		
		roomCB = new JComboBox<Object>(buildComboBoxModel(roomTable));
		roomCB.addItemListener(new floorBoxListener());
		
		cableCB = new JCheckBox("Cable");
		
		internetCB = new JCheckBox("Internet");
		
		JButton roomChangeButton = new JButton("Change Services");
		roomChangeButton.addActionListener(new roomChangeListener());
		
		JLabel roomPriceLabel = new JLabel("Change the Price:");
		
		roomPriceField = new JTextField(5);
		
		JButton roomPriceButton = new JButton("Update Price");
		roomPriceButton.addActionListener(new RoomPriceChangeListener());
		
		JButton roomsBackB2 = new JButton("Back");
		roomsBackB2.addActionListener(new BackButtonListener());
		
		roomComboBoxPanel.add(roomModLabel);
		roomComboBoxPanel.add(roomCB);
		
		roomInfoP.add(roomInfoLabel);
		roomInfoP.add(cableCB);
		roomInfoP.add(internetCB);
		roomInfoP.add(roomChangeButton);
		
		roomPricePanel.add(roomPriceLabel);
		roomPricePanel.add(roomPriceField);
		roomPricePanel.add(roomPriceButton);
		
		//roomModP.add(roomModLabel);
		roomModP.add(roomComboBoxPanel);
		roomModP.add(roomInfoP);
		roomModP.add(roomPricePanel);
		
		roomsP.add(roomScrollPane, BorderLayout.WEST);
		roomsP.add(roomModP, BorderLayout.EAST);
		roomsP.add(roomsBackB2, BorderLayout.SOUTH);
		
		c.add(roomsP, "Sales");

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
		 * "Events"
		 */
		
		JPanel eventP = new JPanel();
		eventP.setLayout(new BorderLayout());
		JPanel event = new JPanel();
		JPanel event2 = new JPanel();
		JPanel outer = new JPanel(new BorderLayout());
		event.setLayout(new FlowLayout());
		event2.setLayout(new FlowLayout());
		
		JButton buttonss = new JButton("RESERVE");
		
		JLabel date = new JLabel("Choose Date:");
		JLabel from = new JLabel("From:");
		JLabel to = new JLabel("To:");
		JLabel size = new JLabel("Seats:");
		JLabel fn = new JLabel("First Name:");
		JLabel ln = new JLabel("Last Name:");
		dateCalendar = new SwingCalendar();
		
		ResultSet eventSet = events.displayEvents();
		eventsTable = new JTable(buildTableModele(eventSet));
		eventsScrollPane = new JScrollPane(eventsTable);
		
		
		daysCB = new JComboBox<Object>(buildComboBoxModeld(eventsTable));
		hoursCB = new JComboBox<Object>(buildComboBoxModelh(eventsTable));
		seatsCB = new JComboBox<Object>(buildComboBoxModels(eventsTable));
		hoursCB2 = new JComboBox<Object>(buildComboBoxModelh(eventsTable));
		fname = new JTextField(20);
		lname = new JTextField(20);
		event2.add(fname);
		event2.add(lname);
		
		event.setLayout(new FlowLayout());
		event.add(date);
		event.add(dateCalendar);
		event.add(daysCB);
		event.add(from);
		event.add(hoursCB);
		event.add(to);
		event.add(hoursCB2);
		event.add(size);
		event.add(seatsCB);
		event.add(fn);
		event.add(fname);
		event.add(ln);
		event.add(lname);
		
		
		outer.add(event, BorderLayout.EAST);
		outer.add(event2, BorderLayout.WEST);
		eventP.add(outer, BorderLayout.CENTER);
		
		buttonss.addActionListener(new dateListener());
		
		
		
		eventP.add(eventsScrollPane, BorderLayout.NORTH);
		
		
		
		JButton eventBackB = new JButton("Back");
		eventBackB.addActionListener(new BackButtonListener());
		
		eventP.add(buttonss, BorderLayout.EAST);
		eventP.add(eventBackB, BorderLayout.SOUTH);
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
		checkInGuestP.setLayout(new GridLayout(3,1));
		
		JPanel firstNameP = new JPanel();
		firstNameP.setLayout(new FlowLayout());
		
		JPanel lastNameP = new JPanel();
		lastNameP.setLayout(new FlowLayout());
		
		JPanel checkInBottomP = new JPanel();
		checkInBottomP.setLayout(new GridLayout(1,2));
		
		JPanel checkInChangeP = new JPanel();
		checkInChangeP.setLayout(new FlowLayout());
		

		JLabel beginDateLabel = new JLabel("Start Date:");
		
		beginDateCalendar = new SwingCalendar();
		
		JLabel endDateLabel = new JLabel("End Date:");
		
		endDateCalendar = new SwingCalendar();
		
		JLabel checkInIDFName = new JLabel("First Name:",SwingConstants.RIGHT);
		checkInIDFNameField = new JTextField(20);
		
		JLabel checkInIDLName = new JLabel("Last Name:",SwingConstants.RIGHT);
		checkInIDLNameField = new JTextField(30);
		
		JLabel preCheckRoomChanger = new JLabel("Enable Services:");
		
		preRoomCB = new JComboBox<Object>(buildComboBoxModel(roomTable));
		preRoomCB.addItemListener(new floorBoxListener());
		
		preCableCB = new JCheckBox("Cable");
		
		preInternetCB = new JCheckBox("Internet");
		
		JButton preRoomChangeButton = new JButton("Change Services");
		preRoomChangeButton.addActionListener(new preRoomChangeListener());
		
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
		
		checkInChangeP.add(preCheckRoomChanger);
		checkInChangeP.add(preRoomCB);
		checkInChangeP.add(preCableCB);
		checkInChangeP.add(preInternetCB);
		checkInChangeP.add(preRoomChangeButton);
		
		checkInGuestP.add(firstNameP);
		checkInGuestP.add(lastNameP);
		checkInGuestP.add(checkInChangeP);
		
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
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	public static DefaultTableModel buildTableModelu(ResultSet rs) //stackoverflow/questions/10620448/
			throws SQLException{
			ResultSetMetaData metaData = rs.getMetaData();
			
			Vector<String> columnNames = new Vector<String>(); //get columnnames
			//int columnCount = metaData.getColumnCount(); this line has no use for us since we know how many columns there are
			for (int column = 1; column <= 2; column++){
				columnNames.add(metaData.getColumnName(column));
			}
			
			//table data
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()){
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= 2; columnIndex++){
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
	
		///////////	//////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	//*******EVENTS*********//
	//////////////////////////////////////////////////////////////////////////////////////////////
	public static DefaultTableModel buildTableModele(ResultSet rs) //stackoverflow/questions/10620448/
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//*******Payroll********/
	//////////////////////////////////////////////////////////////////////////////////////////////


	public static DefaultTableModel buildTableModelp(ResultSet rs) //stackoverflow/questions/10620448/
			throws SQLException{
			ResultSetMetaData metaData = rs.getMetaData();
			
			Vector<String> columnNames = new Vector<String>(); //get columnnames
			//int columnCount = metaData.getColumnCount(); this line has no use for us since we know how many columns there are
			for (int column = 1; column <= 11; column++){
				columnNames.add(metaData.getColumnName(column));
			}
			
			//table data
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()){
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= 11; columnIndex++){
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


	//********GENERATE PAYROLL****************//
	private class PayrollGenerateListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {

			try {
				//stackoverflow/83575022
				JTextArea textArea = new JTextArea(payroll.generateReport());
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(1280,720));
				JOptionPane.showMessageDialog(null, scrollPane,"Payroll Report",JOptionPane.INFORMATION_MESSAGE);

			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	//**********Events*************//
	///////////////////////////////////////////////////////////////////////////////////////////

	public static Vector<Object> buildComboBoxModele(JTable table){
		Vector<Object> events = new Vector<Object>();
		for(int event = 0; event < table.getRowCount(); event++){
			events.add(table.getValueAt(event, 0));
		}
		return events;
	}
	
	public static Vector<Object> buildComboBoxModeld(JTable table){
		Vector<Object> events = new Vector<Object>();
		for(int event = 0; event < table.getRowCount(); event++){
			events.add(table.getValueAt(event, 1));
		}
		return events;
	}

	public static Vector<Object> buildComboBoxModelh(JTable table){
		Vector<Object> events = new Vector<Object>();

		//for(int event = 0; event < table.getRowCount(); event++){
			//events.add(table.getValueAt(event, 3));
		//}

		for(int event = 9; event <= 19; event++) {
			//events.add(event+" AM");
			if(event < 12) {
				events.add(event+ " AM");
			}
			else if(event == 12) {
				events.add(event+ " PM");

			}
			
			else {
				events.add((event-12) + " PM");
				//events.add(12, "AM");
			}
		}
		return events;
	}
	
	public static Vector<Object> buildComboBoxModels(JTable table){
		
		Vector<Object> events = new Vector<Object>();

		//for(int event = 0; event < table.getRowCount(); event++){
			//events.add(table.getValueAt(event, 2));
		//}

		events.add(100);
		events.add(300);
		events.add(500);
		return events;
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
			check.dispose();
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
	private class EventsBackButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c,  "Events/Dining");
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////
	private class checkInOutBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In/Out");
		}
	}
	
	//*********Events************//
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private class floorBoxListener2 implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event){
			// TODO Auto-generated method stub
			int thing = (Integer)event.getItem();
			for(int i = 0; i<eventsTable.getRowCount(); i++){
				if ((int)eventsTable.getValueAt(i, 0) == thing){
					eventsCB.setSelectedItem(true);
				}
					else {
						eventsCB.setSelectedItem(false);
					
			}
		}
	}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
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
	private class preRoomChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try {
				rooms.updateCable((int)roomTable.getValueAt(roomTable.getSelectedRow(), 1), preCableCB.isSelected()); //This updates the database based on the status of the "Cable" checkbox
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				rooms.updateInternet((int)roomTable.getValueAt(roomTable.getSelectedRow(), 1), preInternetCB.isSelected()); //This updates the database based on the status of the "Internet" checkbox
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0;i < roomTable.getRowCount(); i++){
				if((int)roomTable.getValueAt(i,1) == (int)roomTable.getValueAt(roomTable.getSelectedRow(), 1)){
					if(preCableCB.isSelected()){
						roomTable.setValueAt("Yes", i, 3);
					}
					else
						roomTable.setValueAt("No", i, 3);
					if(internetCB.isSelected()){
						roomTable.setValueAt("Yes", i, 4);
					}
					else roomTable.setValueAt("No", i, 4);
					try {
						roomTable.setValueAt(rooms.getPrice(rooms.getRoomDetail((int)roomTable.getValueAt(roomTable.getSelectedRow(), 1))), i, 5);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}


	private class RoomPriceChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			try {
			    rooms.updatePrice((int)roomCB.getSelectedItem(),Double.parseDouble(roomPriceField.getText()));
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			    // handle the error
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < roomTable.getRowCount(); i++) {
				if((int)roomTable.getValueAt(i,1) == (int)roomCB.getSelectedItem()) {
					roomTable.setValueAt(Double.parseDouble(roomPriceField.getText()), i, 5);
				}
			}
		}
	}

	private class CheckInListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(roomTable.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(null, "Please select a room from the table.", "Alert", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(beginDateCalendar.getDate().before(new Date(System.currentTimeMillis()))){
				JOptionPane.showMessageDialog(null, "Error: Selected check-in date is on or before current date.", "Alert", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(beginDateCalendar.getDate().after(endDateCalendar.getDate())){
				JOptionPane.showMessageDialog(null, "Error: Selected check-out date is before selected check-in date.", "Alert", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(beginDateCalendar.getDate().equals(endDateCalendar.getDate())) {
				JOptionPane.showMessageDialog(null, "Error: Selected check-out and check-in dates are the same.", "Alert", JOptionPane.WARNING_MESSAGE);
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
					}
					else {
						roomTable.clearSelection();
						checkInIDFNameField.setText("");
						checkInIDLNameField.setText("");
					}
						return;

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
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	private class ticket implements ActionListener{
		public void actionPerformed(ActionEvent event){
			
			for(int i = 0;i < eventsTable.getRowCount(); i++){
				if((int)eventsTable.getValueAt(i,0) == (int)eventsCB.getSelectedItem()){
					eventsTable.setValueAt(((int)eventsTable.getValueAt(i, 1))-1, i, 1);
					eventsTable.setValueAt(((double)eventsTable.getValueAt(i, 9))+5, i, 9);

					}
					
				}
			}
		}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	
	//closes the jframe
	private class checkButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			
			check.dispose();
		
	}
	}
	
	


public void startTimer(int countPassed, JFrame frame) {
	ActionListener action = new ActionListener() {
	public void actionPerformed(ActionEvent event){
		if(count==0) {
			countdown.stop();
			frame.dispose();
		}else {
			
			count--;
		}
	}
};
	countdown = new Timer(delay,action);
	countdown.setInitialDelay(0);
	countdown.start();
	count = countPassed;
}
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	//****LOGIN*****//
	private class LoginListener2 implements ActionListener{
		//ActionListener action;
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent event){
			String username = usernameField.getText();
			String pwtyped = passwordField.getText();	//user types this into password field
			
			String password = users.getHash(pwtyped.getBytes(),"SHA-512");	//password is encrypted
			boolean found = false;
			
			//checks to see if username and password match
				for(int i=0; i<userTable.getRowCount(); i++) {
					String un = (String)userTable.getValueAt(i, 0);
					String pw = (String)userTable.getValueAt(i, 1);
					if(un.equals(username) && pw.equals(password)){
						found=true;
						break;
					}
					else {
						
						found=false;
				}
					
			}
				if(found) {
					JButton button = new JButton("OK");
					check = new JFrame();
					check.setSize(300,300);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
					check.setLayout(new FlowLayout());
					JLabel label = new JLabel("Login Successful");
					check.add(label);
					check.add(button);
					button.addActionListener(new LoginListener());
					check.setVisible(true);
				}
				else {
					attempts++;
					if(attempts==3){
						
						JLabel label = new JLabel("You have attempted 3 times");
						JLabel lock = new JLabel("SYSTEM IS LOCKED FOR 30 SECONDS");
						check = new JFrame();
						check.setSize(300, 300);
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
						check.setLayout(new FlowLayout());
						check.add(label);
						check.add(lock);
						startTimer(30, check);
						check.setUndecorated(true);
						check.setVisible(true);
						attempts = 0;
					}
					
				else {
					JButton button = new JButton("Try again");
					check = new JFrame();
					check.setSize(300,300);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
					check.setLayout(new FlowLayout());
					JLabel label = new JLabel("Incorrect UserName/Password");
					check.add(label);
					check.add(button);
					button.addActionListener(new checkButtonListener());
					check.setVisible(true);
					}
				}
		}
		
	
	}
	
	
	//This is to allow login by just pressing ENTER in the passwordfield
	private class LoginListener3 implements KeyListener{
		@SuppressWarnings("deprecation")
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_ENTER) {
				String username = usernameField.getText();
				String pwtyped = passwordField.getText();	//user types this into password field
				
				String password = users.getHash(pwtyped.getBytes(),"SHA-512");	//password is encrypted
				boolean found = false;
				
				
				//checks to see if username and password match
					for(int i=0; i<userTable.getRowCount(); i++) {
						String un = (String)userTable.getValueAt(i, 0);
						String pw = (String)userTable.getValueAt(i, 1);
						if(un.equals(username) && pw.equals(password)){
							found=true;
							break;
						}
						else {
							
							found=false;
					}
						
				}
					if(found) {
						JButton button = new JButton("OK");
						check = new JFrame();
						check.setSize(300,300);
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
						check.setLayout(new FlowLayout());
						JLabel label = new JLabel("Login Successful");
						check.add(label);
						check.add(button);
						button.addActionListener(new LoginListener());
						check.setVisible(true);
					}
					else {
						attempts++;
						if(attempts==3){
							JPanel panel = new JPanel();
							panel.setLayout(new GridLayout(0, 1));
							startTime = -1;
							long duration = 30000;
							timer = new Timer(10, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									JLabel label = new JLabel();
					                if (startTime < 0) {
					                    startTime = System.currentTimeMillis();
					                }
					                long now = System.currentTimeMillis();
					                long clockTime = now - startTime;
					                if (clockTime >= duration) {
					                    clockTime = duration;
					                    timer.stop();
					                }
					                SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
					                label.setText(df.format(duration - clockTime));
					            }
					        });
							
							JLabel tryLabel = new JLabel("You have attempted 3 times", SwingConstants.CENTER);
							JLabel lock = new JLabel("SYSTEM IS LOCKED FOR 30 SECONDS", SwingConstants.CENTER);
							label = new JLabel("...");
							panel.add(tryLabel);
							panel.add(label);
							panel.add(lock);
							check = new JFrame();
							check.setSize(300, 300);
							Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
							check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
							//check.setLayout(new FlowLayout());
							//check.add(label);
							//check.add(lock);
							check.add(panel);
							startTimer(30, check);
							timer.start();
							//CountDown loginCountDown = new CountDown();
							check.setUndecorated(true);
							check.setVisible(true);
							attempts = 0;
						}
						
					else {
						JButton button = new JButton("Try again");
						check = new JFrame();
						check.setSize(300,300);
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						check.setLocation(dim.width/2-check.getSize().width/2, dim.height/2-check.getSize().height/2);
						check.setLayout(new FlowLayout());
						JLabel label = new JLabel("Incorrect UserName/Password");
						check.add(label);
						check.add(button);
						button.addActionListener(new checkButtonListener());
						check.setVisible(true);
						}
					}
			}
		}
		public void keyReleased(KeyEvent arg0) {
		}
		public void keyTyped(KeyEvent arg0) {
			
		}
	}
	
	private class dateListener implements ActionListener{
		private int hr;
		private int hr2;
		private double small;
		private double medium;
		private double large;
		public void actionPerformed(ActionEvent event){
			int dayOfWeek;
			small = 100;
			medium = 200;
			large = 300;
			
			 if(dateCalendar.getDate().before(new Date(System.currentTimeMillis()))){

				return;
			}
			
			else{
				
				
				String day = dateCalendar.getDate2(dateCalendar.getDate());
				if ( ((String) hoursCB.getSelectedItem()).indexOf("AM") != -1 ||  ((String) hoursCB.getSelectedItem()).indexOf("PM") != -1 || ((String) hoursCB2.getSelectedItem()).indexOf("AM") != -1 || ((String) hoursCB2.getSelectedItem()).indexOf("PM") != -1) {
					int x = Integer.parseInt(((String) hoursCB.getSelectedItem()).replaceAll("[\\D]", ""));
					int y = Integer.parseInt(((String) hoursCB2.getSelectedItem()).replaceAll("[\\D]", ""));
					if(x>7 && y>7) {
						hr = y - x;
					}
					else if(x<=7 && y<=7) {
						hr = y - x;
					}
					else {
						hr2 = 12-x;
						hr = y+hr2;
					}

					} 
				
				if((int)seatsCB.getSelectedItem()==100) {
					salesEvents = small*hr;
				}
				if(((int)seatsCB.getSelectedItem())==300) {
					salesEvents = medium*hr;
				}
				if(((int)seatsCB.getSelectedItem())==500) {
					salesEvents = large*hr;
				}
				
				for(int i = 0; i < eventsTable.getRowCount(); i++) {
					if(day.equals(eventsTable.getValueAt(i,1)) && dateCalendar.isDayinWeek(dateCalendar.getDate())) {
						
						eventsTable.setValueAt("Reserved", i, 4);
						eventsTable.setValueAt(hr, i, 3);
						eventsTable.setValueAt(seatsCB.getSelectedItem(), i, 2);
						eventsTable.setValueAt(salesEvents, i, 5);

					}
				}
				for(int j = 0; j<eventsTable.getRowCount(); j++) {
					eventTotal = eventTotal + ((double)eventsTable.getValueAt(j, 5));
				}
					
				CardLayout cardLayout = (CardLayout)(c.getLayout());
				cardLayout.show(c, "Events");
			}
		}
	}
}