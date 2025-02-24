package boundary;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import control.PersonManagement;
import entity.Customer;
import java.util.Date;

public class CustomerRegistrationDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtId, txtName, txtPhone, txtEmail, txtCity, txtStreet, txtHouseNumber;
    private JButton btnRegister, btnCancel;
    private JDateChooser dateOfFirstContactChooser;

    public CustomerRegistrationDialog(Frame parent) {
        super(parent, "Customer Registration", true);
        setSize(400, 500); // גודל חלון גדול יותר כדי להכיל את כל השדות
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);  // הגדרת הרקע של החלון ללבן


        // שינוי לפריסה מסוג GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // רווחים בין השדות

        // שדה ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        txtId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtId.getText().length() >= 9) {
                    e.consume();
                }
            }
        });
        add(txtId, gbc);

        // שדה שם
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        add(txtName, gbc);

        // שדה טלפון
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        txtPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtPhone.getText().length() >= 9) {
                    e.consume();
                }
            }
        });
        add(txtPhone, gbc);

        // שדה דוא"ל
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        // שדה עיר
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        txtCity = new JTextField(20);
        add(txtCity, gbc);

        // שדה רחוב
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Street:"), gbc);
        gbc.gridx = 1;
        txtStreet = new JTextField(20);
        add(txtStreet, gbc);

        // שדה מספר בית
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("House Number:"), gbc);
        gbc.gridx = 1;
        txtHouseNumber = new JTextField(20);
        add(txtHouseNumber, gbc);

        // שדה תאריך תחילת הקשר
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Date of First Contact:"), gbc);
        gbc.gridx = 1;
        dateOfFirstContactChooser = new JDateChooser();
        dateOfFirstContactChooser.setDate(new Date());
        dateOfFirstContactChooser.setPreferredSize(new Dimension(200, 30));
        add(dateOfFirstContactChooser, gbc);

        // גישה לשינוי צבעי רכיבי הלוח שנה
        JCalendar calendar = dateOfFirstContactChooser.getJCalendar();
        calendar.getDayChooser().setForeground(Color.WHITE);  // צבע טקסט של הימים
    
        // כפתור רישום
        gbc.gridx = 0;
        gbc.gridy = 8;
        btnRegister = new JButton("Register");
        add(btnRegister, gbc);

        // כפתור ביטול
        gbc.gridx = 1;
        gbc.gridy = 8;
        btnCancel = new JButton("Cancel");
        add(btnCancel, gbc);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerCustomer();
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private void registerCustomer() {
        try {
            int id = Integer.parseInt(txtId.getText()); // המרה ל-ID כ- int
            String name = txtName.getText().trim();
            
            // המרה של הטלפון ל-int
            int phone = Integer.parseInt(txtPhone.getText().trim()); // המרה ל-phone כ- int
            
            String email = txtEmail.getText().trim();
            String city = txtCity.getText().trim();
            String street = txtStreet.getText().trim();
            String houseNumber = txtHouseNumber.getText().trim();

            String fullAddress = city + ", " + street + " " + houseNumber;
            Date dateOfFirstContact = dateOfFirstContactChooser.getDate();

            // בדיקת שדות ריקים
            if (name.isEmpty() || phone <= 0 || email.isEmpty() || city.isEmpty() || street.isEmpty() || houseNumber.isEmpty() || dateOfFirstContact == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // יצירת אובייקט לקוח
            Customer newCustomer = new Customer(id, name, phone, email, fullAddress, dateOfFirstContact);
            boolean success = PersonManagement.getInstance().addNewCustomer(newCustomer);

            if (success) {
                JOptionPane.showMessageDialog(this, "Customer registered successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Customer could not be registered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID or Phone format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
