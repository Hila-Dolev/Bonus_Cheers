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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import control.*;
import control.*;
import entity.*;

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

	    JButton btnLogin = new JButton("Login");
	    btnLogin.setBounds(150, 150, 100, 30);
	    contentPane.add(btnLogin);

	    // Add action listener for the login button
	    btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
	                handleLogin();
	            }
				
        });
    }

	private void handleLogin() {
		 String username = usernameField.getText();
	     String password = new String(passwordField.getPassword());

	        if (username.equals("ADMIN") && password.equals("ADMIN")) {
	        	 HomeScreen homeScreen = new HomeScreen();
	             homeScreen.configureForRole("Admin");
	             homeScreen.setVisible(true);
	             this.dispose();
	             return;
	        }
		
		try {
            int userId = Integer.parseInt(username);
            int passId = Integer.parseInt(password);

            Employee employee = Main.hospital.getStaffMemberById(userId);

            if (staffMember != null && staffMember.getId() == userId && staffMember.getId() == passId) {
            	HomeScreen homeScreen = new HomeScreen();
            	if (staffMember instanceof Doctor) {
            		homeScreen.configureForRole("Doctor");
                } else if (staffMember instanceof Nurse) {
                	homeScreen.configureForRole("Nurse");
                }
            	homeScreen.setVisible(true);
            	this.dispose();
            
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or password");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric ID and password");
        }
    }


}
