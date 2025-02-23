package boundary;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import control.OrderManagement;
import control.PersonManagement;
import entity.Customer;
import entity.Order;
import entity.Wine;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class FrmOrder extends JInternalFrame {
    private JTextField txtCustomerID, txtCustomerName, txtCustomerAddress, txtOrderNumber, txtAssignedSaleEmployeeID;
    private JButton btnPreferences;
    JButton btnSaveOrder = new JButton("Save Order");
    JButton btnDeleteOrder = new JButton("Delete Order");
    JButton btnUpdateOrder = new JButton("Update Order");
    JButton btnCreateOrder = new JButton("Create Order");
    JButton btnSearchOrder = new JButton("Search Order");
    private JPanel wineSelectionPanel;
    private HashMap<String, JSpinner> wineQuantitySpinners;
    private JDateChooser orderDateChooser;

    // רשימה של הזמנות ומצב של הזמנה נוכחית
    private ArrayList<Order> orders;
    private int currentOrderIndex;

    public FrmOrder() {
        super("Order", true, true, true, true);
        setSize(790, 520);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
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
        txtAssignedSaleEmployeeID.setEditable(false);
        formPanel.add(txtAssignedSaleEmployeeID, gbc);

        // Order Date
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Order Date:"), gbc);
        gbc.gridx = 1;
        orderDateChooser = new JDateChooser();
        formPanel.add(orderDateChooser, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Wine Selection Panel
        wineSelectionPanel = new JPanel();
        wineSelectionPanel.setLayout(new GridLayout(0, 2, 5, 5));
        JScrollPane scrollPane = new JScrollPane(wineSelectionPanel);
        add(scrollPane, BorderLayout.CENTER);
        wineQuantitySpinners = new HashMap<>();

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        btnPreferences = new JButton("בחר העדפות יין");
        btnPreferences.addActionListener(e -> openPreferencesScreen());
        buttonPanel.add(btnPreferences);
        
        buttonPanel.add(btnSaveOrder);
        btnSaveOrder.addActionListener(e -> {
            // לוגיקה לשמירה
        });

        buttonPanel.add(btnDeleteOrder);
        btnDeleteOrder.addActionListener(e -> {
            // לוגיקה למחיקה
        });

        buttonPanel.add(btnUpdateOrder);
        btnUpdateOrder.addActionListener(e -> {
            // לוגיקה לעדכון
        });

        buttonPanel.add(btnCreateOrder);
        btnCreateOrder.addActionListener(e -> {
            // לוגיקה ליצירה חדשה
        });

        buttonPanel.add(btnSearchOrder);
        btnSearchOrder.addActionListener(e -> {
            // לוגיקה לחיפוש
        });

        // הוספת כפתורי ניווט בין הזמנות
        JButton btnNextOrder = new JButton(">>");
        btnNextOrder.addActionListener(e -> nextOrder());
        JButton btnPreviousOrder = new JButton("<<");
        btnPreviousOrder.addActionListener(e -> previousOrder());
        buttonPanel.add(btnPreviousOrder);
        buttonPanel.add(btnNextOrder);

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
        updateWineList(order.getWines());
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

    private void updateWineList(ArrayList<Wine> selectedWines) {
        wineSelectionPanel.removeAll();
        wineQuantitySpinners.clear();
        for (Wine wine : selectedWines) {
            JLabel wineName = new JLabel(wine.getName());
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            wineSelectionPanel.add(wineName);
            wineSelectionPanel.add(quantitySpinner);
            wineQuantitySpinners.put(wine.getName(), quantitySpinner);
        }
        wineSelectionPanel.revalidate();
        wineSelectionPanel.repaint();
    }

    private void openPreferencesScreen() {
        FrmWinesPreferences preferencesScreen = new FrmWinesPreferences(this);
        getParent().add(preferencesScreen);
        preferencesScreen.setVisible(true);
        preferencesScreen.toFront();
    }
}
