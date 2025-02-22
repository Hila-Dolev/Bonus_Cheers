 package boundary;

 import javax.swing.*;

import control.PersonManagement;
import entity.Customer;

import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;

 public class FrmRegularOrder extends FrmOrder {
     private JTextField txtCustomerID;
     private JTextField txtCustomerName;
     private JTextField txtCustomerAddress;
     private JTextField txtOrderNumber;
     private JButton btnMergeOrder;
     
     public FrmRegularOrder() {
         super();
         setSize(800, 600); 
         setLayout(null); 

         
         btnMergeOrder = new JButton("Merge Order");
         btnMergeOrder.setBounds(400, 200, 150, 30);btnMergeOrder.setFont(new Font("Arial", Font.BOLD, 13));
         add(btnMergeOrder);
         btnMergeOrder.addActionListener(e -> mergeOrder());
     }
     
        
     private void mergeOrder() {
         String input = JOptionPane.showInputDialog(this, "Enter existing order number:");
         if (input != null && !input.trim().isEmpty()) {
             txtOrderNumber.setText(input.trim());
         }
     
     }
     
     
     
     
 }
