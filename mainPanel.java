import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;

import javax.swing.*;


public class mainPanel extends JPanel{
	static String[] columnNames = {"Priority","Description"};
	static String[][] maintenanceTickets = {{"4", "FIRE"},{"2", "Clean Pool"}};
	static String[][] employees;
	static String[][] hours;
	static Object[] rooms; //array of room objects from the database
	static String[][] sales;
	static JPanel c;
	static JPanel checkInCards;
	
	public mainPanel(){
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
		pInP.setLayout(new GridLayout(1,3));
		
		//Create Buttons
		JButton payrollB = new JButton("Payroll");
		JButton maintB = new JButton("Maintenance");
		JButton entertainB = new JButton("Entertainment");

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
		
		
		/*JPanel roomDummy = new JPanel();
		roomDummy.setBorder(BorderFactory.createLineBorder(Color.black)); //add a line border to the JPanel
		roomDummy.setLayout(new GridLayout(4,1));
		JLabel roomDummyName = new JLabel("Name: Dummy Room");
		JLabel roomDummyPlan = new JLabel("Plan: 0 Bed 0 Bath");
		JLabel roomDummyPrice = new JLabel("Price: $0");
		JLabel roomDummyVacancy = new JLabel("Status: Vacant");
		
		roomDummy.add(roomDummyName);
		roomDummy.add(roomDummyPlan);
		roomDummy.add(roomDummyPrice);
		roomDummy.add(roomDummyVacancy);
		roomsP.add(roomDummy);
		*/
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
