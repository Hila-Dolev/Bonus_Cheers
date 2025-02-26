 package boundary;

 import javax.swing.*;

import control.OrderManagement;
import control.PersonManagement;
import entity.Customer;
import entity.Order;
import entity.OrderStatus;
import entity.PriorityLevel;
import entity.RegularOrder;
import entity.UrgentOrder;
import entity.Wine;

import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

 public class FrmRegularOrder extends FrmOrder {
	     private JTextField txtMainCustomerID;
	    private JTextArea txtWinesInOrder;
	    private int currentOrderIndex;  // אינדקס להזמנה הנוכחית

	    public FrmRegularOrder() {
	        super();  // קריאה לקונסטרוקטור של FrmOrder
	        setTitle("Regular Order");
	        setSize(790, 520);
	        setLayout(new BorderLayout(5, 5));


	        // הוספת רכיבי FrmOrder למסך היורש (כעת למעלה)
	        JPanel orderDetailsPanel = new JPanel(new GridBagLayout());
	        orderDetailsPanel.setBackground(Color.WHITE);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(4, 4, 4, 4);
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.weightx = 1;

	        // Customer ID
	        int row = 0;
	        orderDetailsPanel.add(new JLabel("Customer ID:"), setGBC(gbc, 0, row));
	        orderDetailsPanel.add(txtCustomerID, setGBC(gbc, 1, row++));
	        txtCustomerID.setEditable(false);  // הגדרת השדה כלא ניתן לעריכה


	        // Customer Name
	        gbc.gridx = 0; gbc.gridy = 1;
	        orderDetailsPanel.add(new JLabel("Customer Name:"), gbc);
	        gbc.gridx = 1;
	        orderDetailsPanel.add(txtCustomerName, gbc);

	        // Customer Address
	        gbc.gridx = 0; gbc.gridy = 2;
	        orderDetailsPanel.add(new JLabel("Customer Address:"), gbc);
	        gbc.gridx = 1;
	        orderDetailsPanel.add(txtCustomerAddress, gbc);

	        // Order Number
	        gbc.gridx = 0; gbc.gridy = 3;
	        orderDetailsPanel.add(new JLabel("Order Number:"), gbc);
	        gbc.gridx = 1;
	        orderDetailsPanel.add(txtOrderNumber, gbc);

	        // Assigned Sale Employee ID
	        gbc.gridx = 0; gbc.gridy = 4;
	        orderDetailsPanel.add(new JLabel("Assigned Sale Employee ID:"), gbc);
	        gbc.gridx = 1;
	        orderDetailsPanel.add(txtAssignedSaleEmployeeID, gbc);

	        // Order Date
	        gbc.gridx = 0; gbc.gridy = 5;
	        orderDetailsPanel.add(new JLabel("Order Date:"), gbc);
	        gbc.gridx = 1;
	        orderDetailsPanel.add(orderDateChooser, gbc);
	        
	     // Order Status - ComboBox
	        gbc.gridx = 0; gbc.gridy = 6;
	        orderDetailsPanel.add(new JLabel("Order Status:"), gbc); // הוספת תווית "סטטוס הזמנה"
	        gbc.gridx = 1;
	        cmbOrderStatus = new JComboBox<>(OrderStatus.values()); // אתחול ה-ComboBox
	        orderDetailsPanel.add(cmbOrderStatus, gbc); // הוספת ה-ComboBox לפאנל פרטי ההזמנה
	        
	        add(orderDetailsPanel, BorderLayout.NORTH);  // הוספת פאנל העליון

	        JPanel buttonPanel = new JPanel();
	        //btnPreferences.addActionListener(e -> openPreferences());
	        btnSaveOrder.addActionListener(e -> saveOrder());
	        btnDeleteOrder.addActionListener(e -> deleteOrder());
	        btnCreateOrder.addActionListener(e -> createOrder());
	        btnSearchOrder.addActionListener(e -> searchOrder());

	        // הוספת כפתור "Merge Order" לפאנל הכפתורים
	        JButton btnMergeOrder = new JButton("Merge Order");
	        btnMergeOrder.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // פותח את הדיאלוג החדש
	                openMergeOrderDialog();
	            }
	        });
	        buttonPanel.add(btnMergeOrder);  // הוספת כפתור לפאנל
	        
	        buttonPanel.add(btnPreferences);
	        buttonPanel.add(btnSaveOrder);
	        buttonPanel.add(btnDeleteOrder);
	        buttonPanel.add(btnCreateOrder);
	        buttonPanel.add(searchOrderField);
	        buttonPanel.add(btnSearchOrder);

	        // עדכון כפתורי הניווט
	        JButton btnNextOrder = new JButton(">>");
	        btnNextOrder.addActionListener(e -> loadNextOrder());
	        JButton btnPreviousOrder = new JButton("<<");
	        btnPreviousOrder.addActionListener(e -> loadPreviousOrder());

	        buttonPanel.add(btnPreviousOrder);
	        buttonPanel.add(btnNextOrder);
	        add(buttonPanel, BorderLayout.SOUTH);

	     // יצירת פאנל חדש לפרטי הזמנה דחופה (כעת למטה)
	        JPanel regPanel = new JPanel();
	        regPanel.setLayout(new GridBagLayout());
	        regPanel.setBackground(Color.WHITE);
	       // GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5); // מרווחים גדולים יותר

	     
	     /// Expected Delivery Time
	        gbc.gridx = 0; gbc.gridy = 1;
	        regPanel.add(new JLabel("Expected Delivery Time (days):"), gbc);
	        gbc.gridx = 1;
	        txtMainCustomerID = new JTextField();
	        txtMainCustomerID.setPreferredSize(new Dimension(200, 30)); // גודל מועדף
	        txtMainCustomerID.setMinimumSize(new Dimension(200, 30)); // גודל מינימלי
	        txtMainCustomerID.setMaximumSize(new Dimension(400, 30)); // גודל מקסימלי
	        regPanel.add(txtMainCustomerID, gbc);

	     // Wines in Order (Displayed as TextArea)
	        gbc.gridx = 0; gbc.gridy = 2;
	        regPanel.add(new JLabel("Wines in the Order:"), gbc);
	        gbc.gridx = 1;

	        // יצירת JTextArea עם גודל מותאם
	        txtWinesInOrder = new JTextArea(8, 25); // מספר שורות
	        txtWinesInOrder.setEditable(false);
	        txtWinesInOrder.setPreferredSize(new Dimension(400, 150)); // גודל מועדף
	        txtWinesInOrder.setMinimumSize(new Dimension(400, 150)); // גודל מינימלי
	        txtWinesInOrder.setMaximumSize(new Dimension(600, 200)); // גודל מקסימלי

	        // יצירת JScrollPane והוספת JTextArea לתוכו
	        JScrollPane scrollPane = new JScrollPane(txtWinesInOrder);
	        scrollPane.setPreferredSize(new Dimension(400, 150)); // גודל מותאם ל-SCROLLPANE

	        regPanel.add(scrollPane, gbc);





	        // הוספת פאנל התחתון
	        add(regPanel, BorderLayout.CENTER);

	        loadOrders();  // טעינת הזמנות דחופות
	    }

	    // שיטה לטעינת הזמנות דחופות מה-OrderManager או ממסד הנתונים
	    private void loadOrders() {
	       // urgentOrders = OrderManagement.getInstance().getAllUrgentOrders();  // טעינה ממסד הנתונים
	        //currentOrderIndex = 0;  // התחלת טעינת הזמנה ראשונה
	        //loadOrderDetails();  // טעינת פרטי הזמנה ראשונה
	    }

	    // הצגת פרטי הזמנה דחופה
	    private void loadOrderDetails() {
	       /* if (urgentOrders != null && !urgentOrders.isEmpty() && currentOrderIndex < urgentOrders.size()) {
	            UrgentOrder currentOrder = urgentOrders.get(currentOrderIndex);

	            // מילוי פרטי הזמנה דחופה
	            // עדכון ComboBox לפי ההזמנה
	            cmbUrgencyLevel.setSelectedItem(currentOrder.getPriorityLevel());
	            txtExpectedDeliveryTime.setText(String.valueOf(currentOrder.getExpectedDeliveryTime()));
	            displayWinesInOrder(OrderManagement.getInstance().getWinesInUrgentOrder(currentOrder.getOrderNumber()));

	            // מילוי פרטי ההזמנה הכללית
	            txtOrderNumber.setText(String.valueOf(currentOrder.getOrderNumber()));  // מספר הזמנה
	            txtCustomerID.setText(String.valueOf(currentOrder.getCustomerID()));  // מזהה לקוח

	            // עדכון ComboBox של סטטוס ההזמנה
	            if (currentOrder.getStatus() != null) {
	                cmbOrderStatus.setSelectedItem(currentOrder.getStatus());
	            }

	            // בדיקת האם הלקוח קיים
	            Customer customer = PersonManagement.getInstance().getCustomerDetailsById(currentOrder.getCustomerID());

	            if (customer != null) {
	                // הדפסת פרטי הלקוח לאימות
	                System.out.println("Found customer: " + customer.getName() + ", Address: " + customer.getDeliveryAddress());
	                
	                txtCustomerName.setText(customer.getName());  // שם לקוח
	                txtCustomerAddress.setText(customer.getDeliveryAddress());  // כתובת לקוח
	            } else {
	                // אם הלקוח לא נמצא
	                System.out.println("Customer not found for ID: " + currentOrder.getCustomerID());
	                
	                txtCustomerName.setText("Unknown Customer");  // ברירת מחדל אם לא נמצא לקוח
	                txtCustomerAddress.setText("No Address Available");
	            }

	            txtAssignedSaleEmployeeID.setText(String.valueOf(currentOrder.getAssignedSaleEmployeeID()));  // מזהה עובד מכירות
	            orderDateChooser.setDate(currentOrder.getOrderDate());  // תאריך הזמנה
	        }*/
	    }

	    private void displayWinesInOrder(HashMap<Wine, Integer> winesInOrder) {
	        StringBuilder winesText = new StringBuilder();

	        // עבור כל זוג יין וכמות במפה
	        for (HashMap.Entry<Wine, Integer> entry : winesInOrder.entrySet()) {
	            Wine wine = entry.getKey();
	            Integer quantity = entry.getValue();

	            // בדוק אם היין לא null לפני הגישה לשם
	            if (wine != null && wine.getName() != null) {
	                winesText.append(wine.getName())
	                         .append(" - Quantity: ")
	                         .append(quantity != null ? quantity : 0)  // אם quantity הוא null, השתמש ב-0
	                         .append("\n");
	            } else {
	                winesText.append("Unknown wine - Quantity: ")
	                         .append(quantity != null ? quantity : 0)
	                         .append("\n");
	            }
	        }

	        // עדכון טקסט באזור התצוגה
	        txtWinesInOrder.setText(winesText.toString());
	    }

	    // עדכון להזמנה הבאה
	    private void loadNextOrder() {
	     /*   currentOrderIndex++;
	        if (currentOrderIndex >= urgentOrders.size()) {
	            currentOrderIndex = 0;  // התחלה מחדש אם הגענו לסוף
	        }
	        loadOrderDetails();  // עדכון הפרטים למסך
	   */
	    }

	    // עדכון להזמנה הקודמת
	    private void loadPreviousOrder() {
	       /* currentOrderIndex--;
	        if (currentOrderIndex < 0) {
	            currentOrderIndex = urgentOrders.size() - 1;  // חזרה להתחלה אם הגענו להתחלה
	        }
	        loadOrderDetails();  // עדכון הפרטים למסך
	  */
	    }
	    
	    public void saveOrder() {
	    /*try {
	        // קריאה לכלל השדות במסך
	        int orderNumber = Integer.parseInt(txtOrderNumber.getText().trim());
	        java.util.Date utilDate = orderDateChooser.getDate();
	        java.sql.Date orderDate = new java.sql.Date(utilDate.getTime());
	        
	        // קריאה למצב ההזמנה
	        OrderStatus status = (OrderStatus) cmbOrderStatus.getSelectedItem();
	        
	        // קריאה לתאריך המשלוח
	        java.util.Date shipmentDateUtil = orderDateChooser.getDate();
	        java.sql.Date shipmentDate = new java.sql.Date(shipmentDateUtil.getTime());
	        
	        // קריאה למספר עובד מכירות שהוקצה להזמנה
	        int assignedSaleEmployeeID = Integer.parseInt(txtAssignedSaleEmployeeID.getText().trim());
	        
	        // קריאה לרמת הדחיפות
	        PriorityLevel priorityLevel = (PriorityLevel) cmbUrgencyLevel.getSelectedItem();
	        
	        // קריאה לזמן האספקה הצפוי
	        int expectedDeliveryTime = Integer.parseInt(txtExpectedDeliveryTime.getText().trim());
	        
	        // קריאה למספר הלקוח
	        int customerID = Integer.parseInt(txtCustomerID.getText().trim());
	        
	        // קריאה לרשימת היינות בהזמנה
	        HashMap<Wine, Integer> winesInOrder = OrderManagement.getInstance().getWinesInUrgentOrder(orderNumber);
	        if (winesInOrder == null || winesInOrder.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "No wines selected for this order!", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // יצירת אובייקט הזמנה דחופה חדש
	        UrgentOrder newOrder = new UrgentOrder(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID, priorityLevel, expectedDeliveryTime, customerID, winesInOrder);

	        // שמירה למסד הנתונים
	        boolean success = OrderManagement.getInstance().saveUrgentOrder(newOrder);
	        if (success) {
	            JOptionPane.showMessageDialog(this, "Order saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            loadOrders(); // רענון הרשימה לאחר השמירה
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to save order!", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Invalid number format! Please check all numeric fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	    */
	}

	    private void deleteOrder() {
	        // לוגיקה למחיקת הזמנה
	    }

	    private void createOrder() {
	        // לוגיקה ליצירת הזמנה חדשה
	    }

	    private void searchOrder() {
	        // לוגיקה לחיפוש הזמנה
	    }
	    
	    private GridBagConstraints setGBC(GridBagConstraints gbc, int x, int y) {
	        gbc.gridx = x;
	        gbc.gridy = y;
	        return gbc;
	    }

	    private void openMergeOrderDialog() {
	        // יצירת דיאלוג חדש (לדוגמה, אפשר להשתמש ב-JDialog)
	    	JDialog mergeOrderDialog = new JDialog((Frame) null, "Merge Orders", true);
	        mergeOrderDialog.setSize(400, 300);
	        mergeOrderDialog.setLayout(new BorderLayout());

	        // ניתן להוסיף כאן רכיבים לדיאלוג כמו טפסים או כפתורים
	        JLabel label = new JLabel("מיזוג הזמנות");
	        mergeOrderDialog.add(label, BorderLayout.CENTER);

	        // כפתור לסגירת הדיאלוג
	        JButton closeButton = new JButton("Close");
	        closeButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                mergeOrderDialog.dispose();  // סגירת הדיאלוג
	            }
	        });
	        mergeOrderDialog.add(closeButton, BorderLayout.SOUTH);

	        // הצגת הדיאלוג
	        mergeOrderDialog.setVisible(true);
	    }
     
 }
