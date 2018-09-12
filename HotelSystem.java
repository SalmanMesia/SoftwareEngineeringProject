import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HotelSystem extends JFrame{
   
   //private variables
   private JButton servicesButton;
   private JButton maintenanceButton;
   private JButton entertainmentButton;
   private JButton reservationButton;
   private JFrame guiFrame;
   
   //constructor
   public HotelSystem(){
      
      //Create Frame
      guiFrame = new JFrame();
      guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      guiFrame.setTitle("Hotel System"); //Window Title
      guiFrame.setSize(400,300);
      guiFrame.setLocationRelativeTo(null);
       
      //initialize variables

      //BUTTONS
      servicesButton = new JButton("Services");
      maintenanceButton = new JButton("Maintainance");
      entertainmentButton = new JButton("Entertainment");
      reservationButton = new JButton("Reservation");

      //PANELS
      final JPanel servicePanel = new JPanel();
      final JPanel maintenancePanel = new JPanel();
      final JPanel entertainmentPanel = new JPanel();               
      final JPanel reservationPanel = new JPanel();               
      
      servicePanel.add(servicesButton);
      maintenancePanel.add(maintenanceButton);
      entertainmentPanel.add(entertainmentButton);
      reservationPanel.add(reservationButton);

      //ADD TO FRAME
      guiFrame.add(servicePanel, BorderLayout.LINE_START);
      guiFrame.add(maintenancePanel, BorderLayout.CENTER);
      guiFrame.add(entertainmentPanel, BorderLayout.LINE_END);
      guiFrame.add(reservationPanel, BorderLayout.PAGE_END);
      
      guiFrame.setVisible(true);
   }
   
   //methods
   
   //main method
   public static void main(String[] arg){
      HotelSystem graphicInterface = new HotelSystem();
   }
}