 package boundary;

 import javax.swing.*;

import control.PersonManagement;
import entity.Customer;
import entity.Wine;

import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

 public class FrmOrder extends JInternalFrame {
     protected JTextField txtCustomerID;
     protected JTextField txtCustomerName;
     protected JTextField txtCustomerAddress;
     protected JTextField txtOrderNumber;
     protected JButton btnPreferences;
     protected JPanel wineSelectionPanel;
     protected HashMap<String, JSpinner> wineQuantitySpinners;
     
     public FrmOrder() {
         super("Order", true, true, true, true);
         setSize(800, 600); 
         setLayout(null); 
         
         
       //Design
         Color buttonBackgroundColor = Color.decode("#6f2936");
         Color buttonTextColor = Color.WHITE; 
         Color fieldBackgroundColor = Color.WHITE; 

         UIManager.put("Button.background", buttonBackgroundColor);
         UIManager.put("Button.foreground", buttonTextColor);
         UIManager.put("TextField.background", fieldBackgroundColor);
         UIManager.put("ComboBox.background", fieldBackgroundColor);
         UIManager.put("ComboBox.foreground", Color.decode("#252525")); // תוכל להוסיף גם צבע טקסט אם תרצה
 		UIManager.put("ComboBox.selectionBackground", Color.decode("#6f2936"));
 		UIManager.put("ComboBox.selectionForeground", Color.WHITE);
 		
   
 		this.getContentPane().setBackground(Color.WHITE);
         
         

         JLabel lblCustomerID = new JLabel("Customer ID:");
         lblCustomerID.setBounds(50, 50, 120, 30);
         add(lblCustomerID);
         
         txtCustomerID = new JTextField();
         txtCustomerID.setBounds(180, 50, 200, 30);
         add(txtCustomerID);
         txtCustomerID.addActionListener(e -> loadCustomerDetails());
         
         JLabel lblCustomerName = new JLabel("Customer Name:");
         lblCustomerName.setBounds(50, 100, 120, 30);
         add(lblCustomerName);
         
         txtCustomerName = new JTextField();
         txtCustomerName.setBounds(180, 100, 200, 30);
         txtCustomerName.setEditable(false);
         add(txtCustomerName);
         
         JLabel lblCustomerAddress = new JLabel("Address:");
         lblCustomerAddress.setBounds(50, 150, 120, 30);
         add(lblCustomerAddress);
         
         txtCustomerAddress = new JTextField();
         txtCustomerAddress.setBounds(180, 150, 200, 30);
         txtCustomerAddress.setEditable(false);
         add(txtCustomerAddress);
         
         JLabel lblOrderNumber = new JLabel("Order Number:");
         lblOrderNumber.setBounds(50, 200, 120, 30);
         add(lblOrderNumber);
         
         txtOrderNumber = new JTextField();
         txtOrderNumber.setBounds(180, 200, 200, 30);
         txtOrderNumber.setEditable(false);
         txtOrderNumber.setText(generateOrderNumber());
         add(txtOrderNumber);
         
        
         wineSelectionPanel = new JPanel();
         wineSelectionPanel.setLayout(new GridLayout(0, 2, 5, 5));
         JScrollPane scrollPane = new JScrollPane(wineSelectionPanel);
         scrollPane.setBounds(50, 300, 500, 200);
         add(scrollPane);

         wineQuantitySpinners = new HashMap<>();
         
         btnPreferences = new JButton("בחר העדפות יין");
         btnPreferences.setBounds(50, 250, 200, 30);
         btnPreferences.setBackground(Color.decode("#6f2936"));
         btnPreferences.setForeground(Color.WHITE);
         btnPreferences.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 openPreferencesScreen();
             }
         });
         add(btnPreferences);
        
     }
     
     private void loadCustomerDetails() {
         String customerIdStr = txtCustomerID.getText();
         
         if (customerIdStr.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please enter a customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         try {
             int customerId = Integer.parseInt(customerIdStr);  // המרת ה-ID למספר שלם
             Customer customer = PersonManagement.getInstance().getCustomerDetailsById(customerId);

             if (customer != null) {
                 // עדכון השדות עם פרטי הלקוח
            	 txtCustomerName.setText(customer.getName());
                 txtCustomerAddress.setText(customer.getDeliveryAddress());
                } else {
                 JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
             }

         } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
     
     private String generateOrderNumber() {
         return "ORD" + System.currentTimeMillis();
     }
     
     private void openPreferencesScreen() {
    	 FrmWinesPreferences preferencesScreen = new FrmWinesPreferences(this);
    	 getParent().add(preferencesScreen); // הוספת המסך לתוך ה- JDesktopPane
    	    preferencesScreen.setVisible(true);
    	    preferencesScreen.toFront();
     }

     public void updateWineList(ArrayList<Wine> selectedWines) {
         wineSelectionPanel.removeAll();
         wineQuantitySpinners.clear();

         for (Wine wine : selectedWines) {
             JLabel wineName = new JLabel(wine.getName());
             //JLabel wineID = new JLabel (String(wine.getCatalogNumber()));
             JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

             wineSelectionPanel.add(wineName);
             //wineSelectionPanel.add(wineID);
             wineSelectionPanel.add(quantitySpinner);

             wineQuantitySpinners.put(wine.getName(), quantitySpinner);
         }

         wineSelectionPanel.revalidate();
         wineSelectionPanel.repaint(); //TODO: לההפוך את ההאשמפ לכזה של "יין" ו"מספר" + לעדכן באקסס
     }
     
    
     
     
     
     
     
 }
