package boundary;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import control.*;
import entity.*;
import javax.swing.text.*;


public class LoginFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	//private HomeScreen homeScreen; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 450, 300);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);

	    JLabel lblUsername = new JLabel("Username:");
	    lblUsername.setBounds(50, 70, 100, 20);
	    contentPane.add(lblUsername);

	    usernameField = new JTextField();
	    usernameField.setBounds(150, 70, 200, 20);
	    contentPane.add(usernameField);

	    JLabel lblPassword = new JLabel("Password:");
	    lblPassword.setBounds(50, 110, 100, 20);
	    contentPane.add(lblPassword);

	    passwordField = new JPasswordField();
	    passwordField.setBounds(150, 110, 200, 20);
	    contentPane.add(passwordField);

	 // הוספת מסנן כדי לאפשר רק מספרים
	    ((AbstractDocument) usernameField.getDocument()).setDocumentFilter(new DocumentFilter() {
	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	            if (text.matches("\\d*")) { // רק מספרים מותרים
	                super.replace(fb, offset, length, text, attrs);
	            }
	        }
	    });

	    ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	            if (text.matches("\\d*")) { // רק מספרים מותרים
	                super.replace(fb, offset, length, text, attrs);
	            }
	        }
	    });
	    
	    
	    JButton btnEmployeeLogin = new JButton("Login as Employee");
        btnEmployeeLogin.setBounds(50, 160, 150, 30);
        contentPane.add(btnEmployeeLogin);

        JButton btnCustomerLogin = new JButton("Login as Customer");
        btnCustomerLogin.setBounds(220, 160, 150, 30);
        contentPane.add(btnCustomerLogin);

        btnEmployeeLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(true);
            }
        });

        btnCustomerLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(false);
            }
        });
    }

    private void handleLogin(boolean isEmployee) {
        String userIdText = usernameField.getText();
        String passwordText = new String(passwordField.getPassword());

        try {
            int userId = Integer.parseInt(userIdText);
            int passwordId = Integer.parseInt(passwordText);

            if (isEmployee) {
            	Employee employee = PersonManagement.getInstance().getEmployeeDetailsById(userId);
            	System.out.println(employee.getID());
                if (employee != null && employee.getID() == passwordId) {
                    determineEmployeeRole(employee);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Employee ID or password");
                }
            } else {
                Customer customer = PersonManagement.getInstance().getCustomerDetailsById(userId);
                if (customer != null && customer.getID() == passwordId) {
                    openHomeScreen("Customer");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Customer ID or password");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID and password");
        }
    }
    private void determineEmployeeRole(Employee employee) {
        String role = null;
        
        switch (employee.getType()) {
        case SALES -> role = "Sales";
        case MARKETING -> role = "Marketing";
        default -> {
            JOptionPane.showMessageDialog(this, "Unknown role");
            return;
        }
    }

        System.out.println("Role received in LoginFrame: " + role);

        openHomeScreen(role);
    }
    
    private void openHomeScreen(String role) {
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.configureForRole(role);
        homeScreen.setVisible(true);
        this.dispose();
    }



}
