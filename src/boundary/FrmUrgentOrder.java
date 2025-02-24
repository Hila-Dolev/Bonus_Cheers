package boundary;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import control.OrderManagement;
import control.PersonManagement;
import entity.Customer;
import entity.UrgentOrder;
import entity.Wine;
import entity.PriorityLevel;  // הוספתי את PriorityLevel

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class FrmUrgentOrder extends FrmOrder {
	private JComboBox<PriorityLevel> cmbUrgencyLevel; 
    private JTextField txtExpectedDeliveryTime;
    private JTextArea txtWinesInOrder;
    private ArrayList<UrgentOrder> urgentOrders;  // רשימה של הזמנות דחופות
    private int currentOrderIndex;  // אינדקס להזמנה הנוכחית

    public FrmUrgentOrder() {
        super();  // קריאה לקונסטרוקטור של FrmOrder
        setTitle("Urgent Order");
        setSize(790, 520);

        // הוספת רכיבי FrmOrder למסך היורש (כעת למעלה)
        JPanel orderDetailsPanel = new JPanel();
        orderDetailsPanel.setLayout(new GridBagLayout());
        orderDetailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        orderDetailsPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        orderDetailsPanel.add(txtCustomerID, gbc);
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

        add(orderDetailsPanel, BorderLayout.NORTH);  // הוספת פאנל העליון

        // עדכון כפתורי הניווט
        JPanel buttonPanel = new JPanel();
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
        JPanel urgentPanel = new JPanel();
        urgentPanel.setLayout(new GridBagLayout());
        urgentPanel.setBackground(Color.WHITE);

        // Urgency Level
        gbc.gridx = 0; gbc.gridy = 0;
        urgentPanel.add(new JLabel("Urgency Level:"), gbc);
        gbc.gridx = 1;
        
        cmbUrgencyLevel = new JComboBox<>(PriorityLevel.values()); // ComboBox עם כל הערכים
        urgentPanel.add(cmbUrgencyLevel, gbc);

        // Expected Delivery Time
        gbc.gridx = 0; gbc.gridy = 1;
        urgentPanel.add(new JLabel("Expected Delivery Time (days):"), gbc);
        gbc.gridx = 1;
        txtExpectedDeliveryTime = new JTextField(15);
        urgentPanel.add(txtExpectedDeliveryTime, gbc);

        // Wines in Order (Displayed as TextArea)
        gbc.gridx = 0; gbc.gridy = 2;
        urgentPanel.add(new JLabel("Wines in the Order:"), gbc);
        gbc.gridx = 1;
        txtWinesInOrder = new JTextArea(5, 20);
        txtWinesInOrder.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtWinesInOrder);
        urgentPanel.add(scrollPane, gbc);

        add(urgentPanel, BorderLayout.CENTER);  // הוספת פאנל התחתון

        loadOrders();  // טעינת הזמנות דחופות
    }

    // שיטה לטעינת הזמנות דחופות מה-OrderManager או ממסד הנתונים
    private void loadOrders() {
        urgentOrders = OrderManagement.getInstance().getAllUrgentOrders();  // טעינה ממסד הנתונים
        currentOrderIndex = 0;  // התחלת טעינת הזמנה ראשונה
        loadOrderDetails();  // טעינת פרטי הזמנה ראשונה
    }

    // הצגת פרטי הזמנה דחופה
    private void loadOrderDetails() {
        if (urgentOrders != null && !urgentOrders.isEmpty() && currentOrderIndex < urgentOrders.size()) {
            UrgentOrder currentOrder = urgentOrders.get(currentOrderIndex);

            // מילוי פרטי הזמנה דחופה
            // עדכון ComboBox לפי ההזמנה
            cmbUrgencyLevel.setSelectedItem(currentOrder.getPriorityLevel());
            txtExpectedDeliveryTime.setText(String.valueOf(currentOrder.getExpectedDeliveryTime()));
            displayWinesInOrder(OrderManagement.getInstance().getWinesInUrgentOrder(currentOrder.getOrderNumber()));

            // מילוי פרטי ההזמנה הכללית
            txtOrderNumber.setText(String.valueOf(currentOrder.getOrderNumber()));  // מספר הזמנה
            txtCustomerID.setText(String.valueOf(currentOrder.getCustomerID()));  // מזהה לקוח

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
        }
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
        currentOrderIndex++;
        if (currentOrderIndex >= urgentOrders.size()) {
            currentOrderIndex = 0;  // התחלה מחדש אם הגענו לסוף
        }
        loadOrderDetails();  // עדכון הפרטים למסך
    }

    // עדכון להזמנה הקודמת
    private void loadPreviousOrder() {
        currentOrderIndex--;
        if (currentOrderIndex < 0) {
            currentOrderIndex = urgentOrders.size() - 1;  // חזרה להתחלה אם הגענו להתחלה
        }
        loadOrderDetails();  // עדכון הפרטים למסך
    }
}
