 package boundary;

 import javax.swing.*;

import control.OrderManagement;
import control.PersonManagement;
import entity.Customer;
import entity.Order;
import entity.RegularOrder;

import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
import java.util.ArrayList;

 public class FrmRegularOrder extends FrmOrder {
     private JButton btnMergeOrder;
     private JButton btnSaveOrder;
     
     public FrmRegularOrder() {
         super();
         setSize(800, 600); 
         setLayout(null); 
         
         btnMergeOrder = new JButton("Merge Order");
         btnMergeOrder.setBounds(400, 200, 150, 30);
         btnMergeOrder.setFont(new Font("Arial", Font.BOLD, 13));
         add(btnMergeOrder);
         btnMergeOrder.addActionListener(e -> openMergeDialog());
         
         btnSaveOrder = new JButton("Save Order");
         btnSaveOrder.setBounds(400, 250, 150, 30);
         btnSaveOrder.setFont(new Font("Arial", Font.BOLD, 13));
         add(btnSaveOrder);
         btnSaveOrder.addActionListener(e -> saveRegularOrder());
         }
     
        
     /*public void mergeOrder() {
    	    ArrayList<Order> orders = OrderManagement.getAllOrders();  // שליפת כל ההזמנות
    	    ArrayList<RegularOrder> regularOrders = new ArrayList<>();

    	    // סינון הזמנות רגילות
    	    for (Order order : orders) {
    	        if (order instanceof RegularOrder) {
    	            regularOrders.add((RegularOrder) order);
    	        }
    	    }

    	    comboBoxOrders.removeAllItems();  // מחיקת פריטים קיימים ב JComboBox
            for (RegularOrder regularOrder : regularOrders) {
                comboBoxOrders.addItem(regularOrder);  // הוספת הזמנה ל JComboBox
            }

            // התראה למשתמש על ההזמנות שהוספו
            if (regularOrders.size() > 0) {
                JOptionPane.showMessageDialog(this, "Regular orders are now available in the dropdown", "Orders Loaded", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No regular orders available", "No Orders", JOptionPane.WARNING_MESSAGE);
            }
     } */
     
     public void openMergeDialog() {
    	 JDialog mergeDialog = new JDialog((Frame) null, "Merge Regular Order", true);
         
         ArrayList<Order> orders = OrderManagement.getAllOrders();  // שליפת כל ההזמנות
         ArrayList<RegularOrder> regularOrders = new ArrayList<>();

         // סינון הזמנות רגילות
         for (Order order : orders) {
             if (order instanceof RegularOrder) {
                 regularOrders.add((RegularOrder) order);
             }
         }
         
         if (regularOrders.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No regular orders available", "No Orders", JOptionPane.WARNING_MESSAGE);
             return;
         }
         
         // יצירת JComboBox עם הזמנות רגילות
         JComboBox<RegularOrder> comboBox = new JComboBox<>(regularOrders.toArray(new RegularOrder[0]));
         comboBox.setRenderer(new DefaultListCellRenderer() {
             @Override
             public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                 JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 if (value instanceof RegularOrder) {
                     label.setText("Order Num: " + ((RegularOrder) value).getOrderNumber());
                 }
                 return label;
             }
         });

         // יצירת כפתור Confirm
         JButton btnConfirm = new JButton("Confirm");
         btnConfirm.addActionListener(e -> {
             RegularOrder selectedOrder = (RegularOrder) comboBox.getSelectedItem();
             if (selectedOrder != null) {
            	 txtOrderNumber.setText(Integer.toString(selectedOrder.getOrderNumber()));
             }
             mergeDialog.dispose();
         });

      // יצירת חלון דיאלוג עם JComboBox
         mergeDialog.setLayout(new BorderLayout());
         mergeDialog.add(comboBox, BorderLayout.CENTER);
         mergeDialog.add(btnConfirm, BorderLayout.SOUTH);
         mergeDialog.setSize(300, 150);
         mergeDialog.setLocationRelativeTo(this);
         mergeDialog.setVisible(true);
         }

     
     
     public void saveRegularOrder() {
         // שליפת נתונים מהשדות
         int orderNumber = Integer.parseInt(txtOrderNumber.getText());
         int customerId = Integer.parseInt(txtCustomerId.getText());  // ייתכן שתצטרך להוסיף שדה כזה אם הוא לא קיים
         Date orderDate = Date.valueOf(txtOrderDate.getText()); // תאריך הזמנה, נניח שהלקוח הכניס תאריך בפורמט YYYY-MM-DD

         // יצירת HashMap של היינות והכמויות
         HashMap<Integer, Integer> wineQuantities = new HashMap<>();
         // כאן יש להוסיף קוד לאיסוף היינות הנבחרים והכמויות שלהם
         for (Wine wine : selectedWines) {
             wineQuantities.put(wine.getWineId(), wine.getQuantity());  // נניח ש- Wine מכיל getWineId() ו-getQuantity()
         }

         // יצירת אובייקט RegularOrder עם הנתונים שנאספו
         RegularOrder regularOrder = new RegularOrder(orderNumber, customerId, orderDate, wineQuantities);

         // קריאה לפונקציה לשמירת ההזמנה במסד הנתונים
         OrderManagement.saveRegularOrder(regularOrder);
     }
     
     
 }
