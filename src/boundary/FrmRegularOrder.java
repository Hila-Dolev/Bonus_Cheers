 package boundary;

 import javax.swing.*;

import control.PersonManagement;
import entity.Customer;

import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;

 public class FrmRegularOrder extends JInternalFrame {
     private JTextField txtCustomerID;
     private JTextField txtCustomerName;
     private JTextField txtCustomerAddress;
     private JTextField txtOrderNumber;
     private JButton btnMergeOrder;
     
     public FrmRegularOrder() {
         super("Regular Order", true, true, true, true);
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
         
         btnMergeOrder = new JButton("Merge Order");
         btnMergeOrder.setBounds(50, 250, 200, 40);btnMergeOrder.setFont(new Font("Arial", Font.BOLD, 14));
         add(btnMergeOrder);
         btnMergeOrder.addActionListener(e -> mergeOrder());
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
     
     private void mergeOrder() {
         String input = JOptionPane.showInputDialog(this, "Enter existing order number:");
         if (input != null && !input.trim().isEmpty()) {
             txtOrderNumber.setText(input.trim());
         }
     
     }
     
     
     
     
 }
