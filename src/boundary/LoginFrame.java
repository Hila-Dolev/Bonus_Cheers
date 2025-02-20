package boundary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;

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

	    //DESIGN
	    // הוספת לוגו כ-Icon של החלון
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/boundary/images/logo.png"));
        this.setIconImage(logoIcon.getImage());
        
        // יצירת פאנל רקע עם תמונה
        contentPane = new JPanel();
        contentPane.setBackground(Color.decode("#e4d1c3")); 
        

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
     // צבעים וגבולות כפתורים
        Color buttonColor = Color.decode("#6f2936");
        Color textColor = Color.WHITE;
        Border buttonBorder = new LineBorder(Color.WHITE, 2);

        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", textColor);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("PasswordField.foreground", Color.BLACK);
	    
	    
	    
	    
	    //FIELDS
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
            	
            	if (employee == null) {
                    JOptionPane.showMessageDialog(this, "Sorry, no employee found with this ID. Please try again");
                    return;
                }
            	
            	if (employee != null && employee.getID() == passwordId) {
            		 showWelcomeMessage(employee.getName());
            		 determineEmployeeRole(employee);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Employee ID or password");
                }
            	
            	
            } else {
                Customer customer = PersonManagement.getInstance().getCustomerDetailsById(userId);
                
                if (customer == null) {
                    JOptionPane.showMessageDialog(this, "Sorry, no customer found with this ID. Please try again");
                    return;
                }
                
                if (customer != null && customer.getID() == passwordId) {
                	showWelcomeMessage(customer.getName());
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
    
    private void showWelcomeMessage(String name) {
        JDialog dialog = new JDialog(this, "Welcome", true);
        dialog.setUndecorated(true);
        JLabel label = new JLabel("Welcome, " + name + "!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.decode("#6f2936"));
        dialog.add(label);
        dialog.setSize(250, 100);
        dialog.setLocationRelativeTo(this);
        
        // יצירת תזמון לסגירת הדיאלוג לאחר 2 שניות
        new Timer(2000, e -> dialog.dispose()).start();
        
        dialog.setVisible(true);
    }




}
