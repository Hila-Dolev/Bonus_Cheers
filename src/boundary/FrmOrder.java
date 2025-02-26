package boundary;

import javax.swing.*;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import control.PersonManagement;
import entity.Customer;
import entity.Order;
import entity.OrderStatus; // יש להוסיף את ה-import עבור ה-ENUM
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrmOrder extends JInternalFrame {
    protected  JTextField txtCustomerID, txtCustomerName, txtCustomerAddress, txtOrderNumber, txtAssignedSaleEmployeeID;
    protected  JButton btnPreferences;
    protected JButton btnSaveOrder = new JButton("Save");
    protected JButton btnDeleteOrder = new JButton("Delete");
    protected JButton btnCreateOrder = new JButton("Create");
    protected JTextField searchOrderField = new JTextField(15);
    protected JButton btnSearchOrder = new JButton("Search");
    protected JButton btnPreviousOrder = new JButton("<<");
    protected JButton btnNextOrder = new JButton(">>");
    protected JDateChooser orderDateChooser;
    protected JComboBox<OrderStatus> cmbOrderStatus; // הוספת ה-CMB סטטוס הזמנה

    // רשימה של הזמנות ומצב של הזמנה נוכחית
    private ArrayList<Order> orders;
    private int currentOrderIndex;

    public FrmOrder() {
        super("Order", true, true, true, true);
        setSize(790, 520);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        txtCustomerID = new JTextField(15);
        formPanel.add(txtCustomerID, gbc);
        txtCustomerID.addActionListener(e -> loadCustomerDetails());

        // Customer Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        txtCustomerName = new JTextField(15);
        txtCustomerName.setEditable(false);
        formPanel.add(txtCustomerName, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        txtCustomerAddress = new JTextField(15);
        txtCustomerAddress.setEditable(false);
        formPanel.add(txtCustomerAddress, gbc);

        // Order Number
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Order Number:"), gbc);
        gbc.gridx = 1;
        txtOrderNumber = new JTextField(15);
        txtOrderNumber.setEditable(false);
        formPanel.add(txtOrderNumber, gbc);

        // Assigned Sale Employee ID
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Assigned Sale Employee ID:"), gbc);
        gbc.gridx = 1;
        txtAssignedSaleEmployeeID = new JTextField(15);
        txtAssignedSaleEmployeeID.setEditable(true);
        formPanel.add(txtAssignedSaleEmployeeID, gbc);

        // Order Date
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Order Date:"), gbc);
        gbc.gridx = 1;
        orderDateChooser = new JDateChooser();
        orderDateChooser.setDateFormatString("dd/MM/yyyy"); 
        orderDateChooser.setPreferredSize(new Dimension(150, 25));
        formPanel.add(orderDateChooser, gbc);

        // סטטוס הזמנה - ComboBox
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Order Status:"), gbc);
        gbc.gridx = 1;
        cmbOrderStatus = new JComboBox<>(OrderStatus.values()); // אתחול ה-ComboBox
        formPanel.add(cmbOrderStatus, gbc);

        // גישה לשינוי צבעי רכיבי הלוח שנה
        JCalendar calendar = orderDateChooser.getJCalendar();
        calendar.getDayChooser().setForeground(Color.WHITE);  // צבע טקסט של הימים
        
        add(formPanel, BorderLayout.NORTH);

        // שינוי ל-GridBagLayout בכדי לשלוט בצורה גמישה יותר על מיקום הכפתורים
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        buttonPanel.setBackground(Color.WHITE);

        // הוספת כפתור "Pick Wine Preferences" בשורה נפרדת
        btnPreferences = new JButton("Pick Wine Preferences");
        btnPreferences.addActionListener(e -> openPreferencesScreen());
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 0;
        gbcButtonPanel.gridwidth = 2; // כפתור זה יתפוס שתי תאים
        buttonPanel.add(btnPreferences, gbcButtonPanel);

        // הוספת כפתורי "Save", "Delete", ו-"Create" בשורות נוספות
        gbcButtonPanel.gridwidth = 1; // כל כפתור יתפוס תא אחד
        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 1;
        buttonPanel.add(btnSaveOrder, gbcButtonPanel);
        btnSaveOrder.addActionListener(e -> {});

        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 2;
        buttonPanel.add(btnDeleteOrder, gbcButtonPanel);
        btnDeleteOrder.addActionListener(e -> {});

        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 3;
        buttonPanel.add(btnCreateOrder, gbcButtonPanel);
        btnCreateOrder.addActionListener(e -> {});

        // הוספת השדות לחיפוש בשורה נפרדת
        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 4;
        buttonPanel.add(new JLabel("Search:"), gbcButtonPanel);

        gbcButtonPanel.gridx = 1; gbcButtonPanel.gridy = 4;
        buttonPanel.add(searchOrderField, gbcButtonPanel);

        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 5;
        buttonPanel.add(btnSearchOrder, gbcButtonPanel);
        btnSearchOrder.addActionListener(e -> {});

        // הוספת כפתורי הניווט (Next/Previous) בשורה נוספת
        gbcButtonPanel.gridx = 0; gbcButtonPanel.gridy = 6;
        buttonPanel.add(btnPreviousOrder, gbcButtonPanel);

        gbcButtonPanel.gridx = 1; gbcButtonPanel.gridy = 6;
        buttonPanel.add(btnNextOrder, gbcButtonPanel);

        // הוספת panel הממשק לכפתורים
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCustomerDetails() {
        String customerIdStr = txtCustomerID.getText();
        if (customerIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int customerId = Integer.parseInt(customerIdStr);
            Customer customer = PersonManagement.getInstance().getCustomerDetailsById(customerId);
            if (customer != null) {
                txtCustomerName.setText(customer.getName());
                txtCustomerAddress.setText(customer.getDeliveryAddress());
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrderDetails() {
        if (orders == null || orders.isEmpty() || currentOrderIndex < 0 || currentOrderIndex >= orders.size()) {
            return;
        }
        Order order = orders.get(currentOrderIndex);
        txtOrderNumber.setText(Integer.toString(order.getOrderNumber()));
        txtAssignedSaleEmployeeID.setText(Integer.toString(order.getAssignedSaleEmployeeID()));
        orderDateChooser.setDate(order.getOrderDate());
        cmbOrderStatus.setSelectedItem(order.getStatus()); // עדכון הסטטוס בקומבו בוקס
    }

    private void nextOrder() {
        if (orders != null && currentOrderIndex < orders.size() - 1) {
            currentOrderIndex++;
            loadOrderDetails();
        }
    }

    private void previousOrder() {
        if (orders != null && currentOrderIndex > 0) {
            currentOrderIndex--;
            loadOrderDetails();
        }
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        currentOrderIndex = 0;
        loadOrderDetails();
    }

    private void openPreferencesScreen() {
        FrmWinesPreferences preferencesScreen = new FrmWinesPreferences(this);
        getParent().add(preferencesScreen);
        preferencesScreen.setVisible(true);
        preferencesScreen.toFront();
    }
}
