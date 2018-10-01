import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.Vector;


public class mainPanel extends JPanel{
	static String[] columnNames = {"Priority","Description"};
	static String[][] maintenanceTickets = {{"4", "FIRE"},{"2", "Clean Pool"}};
	static String[][] employees;
	static String[][] hours;
	static String[][] sales;
	static JPanel c;
	static RoomsDB rooms = gui.rooms;
	static JTable roomTable;
	static JCheckBox cableCB;
	static JCheckBox internetCB;
	static JComboBox<Object> roomCB;
	                                    
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
		c.setPreferredSize(new Dimension(800,600));
		c.setLayout(new CardLayout(1, 1)); //Purely cosmetic borders
		
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
		JButton entertainB = new JButton("Entertainment");

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
		roomsB.addActionListener(new ButtonListener());
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
		employeeP.setLayout(new GridLayout(3,1));
		
		JTable employeeTable = new JTable();
		
		JTable workSchedule = new JTable();
		
		JButton employeeBackB = new JButton("Back");
		employeeBackB.addActionListener(new payrollBackListener());
		
		employeeP.add(employeeTable);
		employeeP.add(workSchedule);
		employeeP.add(employeeBackB);
		
		c.add(employeeP, "Employees");
		
		/*
		 * Screen: "Sales"
		 */
		
		JPanel salesP = new JPanel();
		salesP.setLayout(new BorderLayout());

		JTable salesTable = new JTable();
		
		JLabel salesTitle = new JLabel("Sales");
		salesTitle.setHorizontalAlignment(JLabel.CENTER);
		
		JButton salesBackB = new JButton("Back");
		salesBackB.addActionListener(new payrollBackListener());
		
		salesP.add(salesTitle, BorderLayout.NORTH);
		salesP.add(salesBackB, BorderLayout.SOUTH);
		salesP.add(salesTable, BorderLayout.CENTER);
		
		c.add(salesP, "Sales");
		
		/*
		 * Screen: "Rooms"
		 */
		
		JPanel roomsP = new JPanel();
		roomsP.setLayout(new BorderLayout());
		
		ResultSet roomSet = rooms.displayRooms();
		roomTable = new JTable(buildTableModel(roomSet));
		
		JScrollPane roomScrollPane = new JScrollPane(roomTable);
		
		JPanel roomInfoP = new JPanel();
		
		roomCB = new JComboBox<Object>(buildComboBoxModel(roomTable));
		roomCB.addItemListener(new floorBoxListener());
		
		cableCB = new JCheckBox("Cable");
		
		internetCB = new JCheckBox("Internet");
		
		JButton roomChangeButton = new JButton("Change");
		roomChangeButton.addActionListener(new roomChangeListener());
		
		JButton roomsBackB = new JButton("Back");
		roomsBackB.addActionListener(new BackButtonListener());
		
		roomInfoP.add(roomCB);
		roomInfoP.add(cableCB);
		roomInfoP.add(internetCB);
		roomInfoP.add(roomChangeButton);
		
		roomsP.add(roomScrollPane, BorderLayout.WEST);
		roomsP.add(roomInfoP, BorderLayout.EAST);
		roomsP.add(roomsBackB, BorderLayout.SOUTH);
		
		c.add(roomsP, "Rooms");

		/*
		 * Screen: "Maintenance Menu"
		 */
		JPanel maintP = new JPanel();
		maintP.setLayout(new BorderLayout());
		
		JPanel maintRequestP = new JPanel();
		maintRequestP.setLayout(new GridLayout(1,2));
		
		JLabel maintLabel = new JLabel("Maintenance Tasks");
		maintLabel.setFont(new Font("Times New Roman", Font.PLAIN, 44));
		maintLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JTable maintTable = new JTable(maintenanceTickets, columnNames);
		maintTable.setFillsViewportHeight(true);
		
		JScrollPane maintScrollPane = new JScrollPane(maintTable);
		maintTable.setFillsViewportHeight(true);
		
		JButton maintBackB = new JButton("Back");
		maintBackB.addActionListener(new BackButtonListener());
		
		JButton maintRequestB = new JButton("Maintenance Request");
		maintRequestB.addActionListener(new ButtonListener());
		
		maintP.add(maintLabel, BorderLayout.NORTH);
		maintP.add(maintScrollPane, BorderLayout.CENTER);
		maintRequestP.add(maintBackB);
		maintRequestP.add(maintRequestB);
		maintP.add(maintRequestP, BorderLayout.SOUTH);
		
		c.add(maintP, "Maintenance");
		/*
		 * Screen: "Maintenance Request"
		 */
		JPanel maintReqP = new JPanel();
		maintReqP.setLayout(new BorderLayout());
		JPanel maintReqBotP = new JPanel();
		maintReqBotP.setLayout(new GridLayout(1,2));
		
		JLabel maintReqLabel = new JLabel("Enter Request:");
		maintReqLabel.setHorizontalAlignment(JLabel.CENTER);
		JTextArea maintReqText = new JTextArea();
		JButton maintReqBackB = new JButton("Back");
		JButton maintReqSubmitB = new JButton("Submit");
		
		maintReqBackB.addActionListener(new maintReqBackListener());
		//maintReqSubmitB.addActionListener();
		
		
		maintReqBotP.add(maintReqBackB);
		maintReqBotP.add(maintReqSubmitB);
		maintReqP.add(maintReqLabel, BorderLayout.NORTH);
		maintReqP.add(maintReqBotP, BorderLayout.SOUTH);
		maintReqP.add(maintReqText, BorderLayout.CENTER);
		
		c.add(maintReqP, "Maintenance Request");
		/*
		 * Screen: "Entertainment"
		 */
		JPanel entertainP = new JPanel();
		entertainP.setLayout(new GridLayout(2,1));
		JPanel entertainPInP = new JPanel();
		entertainPInP.setLayout(new GridLayout(1,2));
		
		JButton diningB = new JButton("Dining");
		JButton eventB = new JButton("Event Spaces");
		//JButton poolB = new JButton("Pool");
		
		entertainPInP.add(diningB);
		entertainPInP.add(eventB);
		//entertainPInP.add(poolB);
		
		entertainP.add(entertainPInP);
		
		JButton entertainBackB = new JButton("Back");
		entertainP.add(entertainBackB);
		
		c.add(entertainP, "Entertainment");
		
		diningB.addActionListener(new ButtonListener());
		eventB.addActionListener(new ButtonListener());
		//poolB.addActionListener(new ButtonListener());
		entertainBackB.addActionListener(new BackButtonListener());
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
		
		checkInB.addActionListener(new ButtonListener());
		checkOutB.addActionListener(new ButtonListener());
		checkBackB.addActionListener(new BackButtonListener());
		/*
		 * "Dining"
		 */
		JPanel diningP = new JPanel();
		diningP.setLayout(new GridLayout(2,1));
		
		JLabel diningMenu = new JLabel("MENU GOES HERE");
		diningMenu.setHorizontalAlignment(JLabel.CENTER);
		JButton diningBackB = new JButton("Back");
		
		diningBackB.addActionListener(new entertainmentBackListener());
		
		diningP.add(diningMenu);
		diningP.add(diningBackB);
		
		c.add(diningP, "Dining");
		/*
		 * "Event Spaces"
		 */
		JPanel eventP = new JPanel();
		eventP.setLayout(new GridLayout(2,1));
		JPanel eventDataP = new JPanel();
		eventDataP.setLayout(new GridLayout(1,2));
		
		JLabel eventMap = new JLabel("MAP GOES HERE");
		eventMap.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel eventSchedule = new JLabel("Schedule:\n9/12/2018 - Presentation Build #1 @ 2:30PM");
		
		JButton eventBackB = new JButton("Back");
		eventBackB.addActionListener(new entertainmentBackListener());
		
		eventDataP.add(eventMap);
		eventDataP.add(eventSchedule);
		eventP.add(eventDataP);
		eventP.add(eventBackB);
		
		c.add(eventP, "Event Spaces");
		/*
		 * Check-In Date
		 */
		JPanel checkInCards = new JPanel();
		checkInCards.setLayout(new CardLayout());
		
		JPanel checkInDateP = new JPanel();
		checkInDateP.setLayout(new BorderLayout());
		
		JPanel checkInDatePP = new JPanel();
		checkInDatePP.setLayout(new GridLayout(2,1));
		
		JPanel checkInDateBottomP = new JPanel();
		checkInDateBottomP.setLayout(new GridLayout(1,2));
		
		//JXDatePicker datePicker = new JXDatePicker();
		//datePicker.setDate(Calendar.getInstance().getTime());
		//datePicker.setFormates(new SimpleDateFormate("dd.MM.yyyy"));
		SpinnerDateModel dateModel = new SpinnerDateModel();
		JSpinner datePicker = new JSpinner(dateModel);
		
		//Calendar checkInCal = new GregorianCalendar(2000, Calendar.JANUARY, 1);
		//datePicker.setValue(checkInCal.getTime());
		
		JButton checkInDateBackB = new JButton("Back");
		checkInDateBackB.addActionListener(new checkInOutBackListener());
		
		JButton checkInDateB = new JButton("Confirm");
		checkInDateB.addActionListener(new DateListener());
		
		checkInDatePP.add(datePicker);
		checkInDatePP.add(checkInDateBottomP);
		checkInDateBottomP.add(checkInDateBackB);
		checkInDateBottomP.add(checkInDateB);
		checkInDateP.add(checkInDatePP, BorderLayout.CENTER);
		checkInCards.add(checkInDateP);
		c.add(checkInCards, "Check-In");
		/*
		 * Check-In ID
		 */
		JPanel checkInIDP = new JPanel();
		checkInIDP.setLayout(new GridLayout(2,1));
		
		JPanel checkInIDCredP = new JPanel();
		checkInIDCredP.setLayout(new GridLayout(1,7));
		
		JLabel checkInIDFName = new JLabel("First Name:");
		JTextField checkInIDFNameField = new JTextField(20);
		JLabel checkInIDLName = new JLabel("Last Name:");
		JTextField checkInIDLNameField = new JTextField(30);
		JLabel checkInIDNum = new JLabel("ID Number:");
		JTextField checkInIDNumField = new JTextField(9);
		JButton checkInIDConfirm = new JButton("Submit");
		checkInIDConfirm.addActionListener(new IDListener());
		JButton checkInIDBackB = new JButton("Back");
		checkInIDBackB.addActionListener(new checkInIDBackListener());
		
		checkInIDCredP.add(checkInIDFName);
		checkInIDCredP.add(checkInIDFNameField);
		checkInIDCredP.add(checkInIDLName);
		checkInIDCredP.add(checkInIDLNameField);
		checkInIDCredP.add(checkInIDNum);
		checkInIDCredP.add(checkInIDNumField);
		checkInIDCredP.add(checkInIDConfirm);
		
		checkInIDP.add(checkInIDCredP);
		checkInIDP.add(checkInIDBackB);
		
		c.add(checkInIDP, "Check-In ID");
		/*
		 * "Check-In Time"
		 */
		JPanel checkInTimeP = new JPanel();
		checkInTimeP.setLayout(new GridLayout(2,1));
		
		JLabel checkInTime = new JLabel("Welcome to HOTEL NAME! Your Check-In Time is 2PM today! Enjoy your Stay!");
		checkInTime.setHorizontalAlignment(JLabel.CENTER);
		
		JButton checkInBackB = new JButton("Close");
		checkInBackB.addActionListener(new BackButtonListener());
		
		checkInTimeP.add(checkInTime);
		checkInTimeP.add(checkInBackB);
		
		c.add(checkInTimeP, "Check-In Time");
		/*
		 * "Check-Out ID"
		 */
		JPanel checkOutIDP = new JPanel();
		checkOutIDP.setLayout(new GridLayout(2,1));
		
		JPanel checkOutIDCredP = new JPanel();
		checkOutIDCredP.setLayout(new GridLayout(1,7));
		
		JLabel checkOutIDFName = new JLabel("First Name:");
		JTextField checkOutIDFNameField = new JTextField(20);
		JLabel checkOutIDLName = new JLabel("Last Name:");
		JTextField checkOutIDLNameField = new JTextField(30);
		JLabel checkOutIDNum = new JLabel("ID Number:");
		JTextField checkOutIDNumField = new JTextField(9);
		JButton checkOutIDConfirm = new JButton("Submit");
		checkOutIDConfirm.addActionListener(new IDOutListener());
		JButton checkOutIDBackB = new JButton("Back");
		checkOutIDBackB.addActionListener(new checkInOutBackListener());
		
		checkOutIDCredP.add(checkOutIDFName);
		checkOutIDCredP.add(checkOutIDFNameField);
		checkOutIDCredP.add(checkOutIDLName);
		checkOutIDCredP.add(checkOutIDLNameField);
		checkOutIDCredP.add(checkOutIDNum);
		checkOutIDCredP.add(checkOutIDNumField);
		checkOutIDCredP.add(checkOutIDConfirm);
		
		checkOutIDP.add(checkOutIDCredP);
		checkOutIDP.add(checkOutIDBackB);
		
		c.add(checkOutIDP, "Check-Out");
		/*
		 * "Check-Out Time"
		 */
		JPanel checkOutTimeP = new JPanel();
		checkOutTimeP.setLayout(new GridLayout(2,1));
		
		JLabel checkOutTime = new JLabel("Your Check-Out Time is in like 2 hours! We hope you enjoyed your Stay!");
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
	public static Vector<Object> buildComboBoxModel(JTable table){
		Vector<Object> rooms = new Vector<Object>();
		for(int room = 0; room < table.getRowCount(); room++){
			rooms.add(table.getValueAt(room, 1));
		}
		return rooms;
	}
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, event.getActionCommand());
		}
	}
	private class BackButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.first(c);
		}
	}
	private class payrollBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Payroll");
		}
	}
	private class maintReqBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Maintenance");
		}
	}
	private class entertainmentBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Entertainment");
		}
	}
	private class checkInOutBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In/Out");
		}
	}
	private class checkInIDBackListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In");
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
	private class DateListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			//if(datePicker.)
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In ID");
		}
	}
	private class IDListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-In Time");
		}
	}
	private class IDOutListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			CardLayout cardLayout = (CardLayout)(c.getLayout());
			cardLayout.show(c, "Check-Out Time");
		}
	}
}
