package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

import control.GefenImport;
import entity.WineType;

import javax.swing.border.LineBorder;

import com.toedter.calendar.JYearChooser;

import javax.swing.border.Border;

import java.util.ArrayList;
import control.WineTypeManagement;

public class HomeScreen extends JFrame{

	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem menuItemImport = new JMenuItem("Import Data");
    private JMenuItem menuItemWines = new JMenuItem("Wines");
    private JMenuItem menuItemManufacturers = new JMenuItem("Manufacturers");
    private JMenuItem menuItemPreferencesReport = new JMenuItem(" - Preferences Report"); 
    private JMenuItem menuItemUnproductiveEmployeesReport = new JMenuItem(" - Unproductive Employees Report");
    private JMenuItem menuItemWineInventoryReport = new JMenuItem(" - Wine Inventory Report");
    private JMenu menuOrders = new JMenu("Orders");
    private JMenuItem menuItemRegularOrder = new JMenuItem(" - Regular Order");
    private JMenuItem menuItemUrgentOrder = new JMenuItem(" - Urgent Order");
    private JMenu menuReports = new JMenu("Reports");
    private JLabel logoLabel = new JLabel();
    private JDesktopPane desktopPane = new JDesktopPane(); 
    private JPanel buttonPanel;
    private String role;
    
    
    public static void main(String[] args) {
        new HomeScreen();
    }
	
	public HomeScreen() {
		super("Home Screen");


        // יצירת JDesktopPane עם תמונה כרקע
        desktopPane = new JDesktopPane() {
        private Image backgroundImage = new ImageIcon(getClass().getResource("/boundary/images/logo.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 100, 0, 600, 600, this);
            }
        };
        
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/boundary/images/logo.png"));
        this.setIconImage(logoIcon.getImage());
        
     // יצירת כפתור לוגו עם תמונה
        JButton btnHome = new JButton(new ImageIcon(logoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnHome.setBorderPainted(false); // הסרת גבולות
        btnHome.setContentAreaFilled(false); // הסרת רקע
        btnHome.addActionListener(e -> showHomeScreen()); // פעולה שמחזירה לדף הבית

        
        menuBar.add(btnHome);

        
        Color buttonColor = Color.decode("#6f2936"); // דוגמת צבע מתוך התמונה (שמור על התאמה)
        Color textColor = Color.WHITE;
        Border buttonBorder = new LineBorder(Color.WHITE, 2);

	    UIManager.put("Button.background", buttonColor);
	    UIManager.put("Button.foreground", textColor);

        desktopPane.setBackground(Color.WHITE);  // הגדרת הרקע של ה-DesktopPane ללבן

        
        menuItemImport.addActionListener(e -> {
            try {
                GefenImport.ImportAllXML();
                JOptionPane.showMessageDialog(this, "Data imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to import data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuBar.add(menuItemImport);

        menuItemWines.addActionListener(e -> openWinesScreen());
        menuBar.add(menuItemWines);

        menuItemManufacturers.addActionListener(e -> openManufacturersScreen());
        menuBar.add(menuItemManufacturers);

        menuItemPreferencesReport.addActionListener(e -> openPreferencesReportScreen());  
        menuReports.add(menuItemPreferencesReport); 
        
        menuItemUnproductiveEmployeesReport.addActionListener(e -> openNonProductiveEmployeeReportScreen());
        menuReports.add(menuItemUnproductiveEmployeesReport);
        
        menuItemWineInventoryReport.addActionListener(e -> openWineInventoryReportScreen());
        menuReports.add(menuItemWineInventoryReport);

        
        // הוספת אפשרויות לתפריט
        menuOrders.add(menuItemUrgentOrder);
        menuOrders.add(menuItemRegularOrder);
        
        
        menuBar.add(menuOrders);
        menuBar.add(menuReports);  
        

	     // חיבור כפתור "Regular Order" לפתיחת מסך הזמנה רגילה
	     menuItemRegularOrder.addActionListener(e -> openRegularOrderScreen());
	
	     // חיבור כפתור "Urgent Order" לפתיחת מסך הזמנה דחופה
	     menuItemUrgentOrder.addActionListener(e -> openUrgentOrderScreen());


        menuBar.setBackground(textColor);


        menuItemImport.setBackground(buttonColor);
        menuItemImport.setForeground(textColor);
        menuItemImport.setBorder(buttonBorder);
        
        menuItemWines.setBackground(buttonColor);
        menuItemWines.setForeground(textColor);
        menuItemWines.setBorder(buttonBorder);
        
        menuItemManufacturers.setBackground(buttonColor);
        menuItemManufacturers.setForeground(textColor);
        menuItemManufacturers.setBorder(buttonBorder);
        
        menuReports.setBackground(buttonColor);
        menuReports.setForeground(buttonColor);
        menuReports.setBorder(buttonBorder);
        
        menuOrders.setBackground(buttonColor);
        menuOrders.setForeground(buttonColor);
        menuOrders.setBorder(buttonBorder);
            
        menuItemPreferencesReport.setBackground(textColor);
        menuItemPreferencesReport.setForeground(buttonColor);

        menuItemUnproductiveEmployeesReport.setBackground(textColor);
        menuItemUnproductiveEmployeesReport.setForeground(buttonColor);
        
        menuItemWineInventoryReport.setBackground(textColor);
        menuItemWineInventoryReport.setForeground(buttonColor);
        
        menuItemRegularOrder.setBackground(textColor);
        menuItemRegularOrder.setForeground(buttonColor);
        
        menuItemUrgentOrder.setBackground(textColor);
        menuItemUrgentOrder.setForeground(buttonColor);
        
        menuBar.setPreferredSize(new Dimension(400, 40));
        
        this.setJMenuBar(menuBar);
        
        /*
        // פאנל לכפתורים בצד ימין
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // כפתור מעל כפתור עם רווח ביניהם

        
        JButton btnWines = new JButton("Wines");
        btnWines.setBackground(buttonColor); // הצבע שנבחר
        btnWines.setForeground(textColor);
        btnWines.setMaximumSize(new Dimension(10, 30));
        btnWines.addActionListener(e -> openWinesScreen());

        JButton btnManufacturers = new JButton("Manufacturers");
        btnManufacturers.setBackground(buttonColor); // הצבע שנבחר
        btnManufacturers.setForeground(textColor);  // צבע טקסט
        btnManufacturers.addActionListener(e -> openManufacturersScreen());

        buttonPanel.add(btnWines);
        buttonPanel.add(btnManufacturers);
*/
        
        
        // הוספת רכיבים למסגרת
        this.setLayout(new BorderLayout());
        this.add(desktopPane, BorderLayout.CENTER);
        //this.add(buttonPanel, BorderLayout.EAST);
        // הגדרת חלון
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null); // מרכז החלון
        this.setVisible(true);
        
        
    }
	
	private void openWinesScreen() {
		System.out.println("Navigating to Wines screen...");
		//removeButtonPanel();
		FrmWine winesScreen = new FrmWine(); 
        desktopPane.add(winesScreen);         
        winesScreen.moveToFront();            
        winesScreen.setVisible(true); 
        
        if ("Sales".equals(role)) {
            disableEditing(winesScreen);
        }
        
        /*
        // ברגע שנסגר המסך, הכפתורים יוחזרו
        winesScreen.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                showButtonPanel(); // להחזיר את הכפתורים למסך הבית
            }
        });
    */
	}

	private void openManufacturersScreen() {
		System.out.println("Navigating to Manufacturers screen...");
        //removeButtonPanel();
		FrmManufacturer manufacturerScreen = new FrmManufacturer(); 
        desktopPane.add(manufacturerScreen);         
        manufacturerScreen.moveToFront();            
        manufacturerScreen.setVisible(true);  
        
     /*// ברגע שנסגר המסך, הכפתורים יוחזרו
        manufacturerScreen.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                showButtonPanel(); // להחזיר את הכפתורים למסך הבית
            }
        });
        */
	}
	/*
	private void removeButtonPanel() {
        if (buttonPanel != null) {
            this.remove(buttonPanel); // הסרת כפתורים
            this.revalidate(); // עדכון הפריסה
            this.repaint(); // רענון המסך
        }
    }

    private void showButtonPanel() {
        if (buttonPanel != null) {
            this.add(buttonPanel, BorderLayout.EAST); // הוספת כפתורים מחדש
            this.revalidate();
            this.repaint();
        }
    }
    */
	 private void openPreferencesReportScreen() {
	        System.out.println("Navigating to Preferences Report screen...");
	        FrmWinesPreferences preferencesReportScreen = new FrmWinesPreferences(); // מסך הדו"חות
	        desktopPane.add(preferencesReportScreen);
	        preferencesReportScreen.moveToFront();
	        preferencesReportScreen.setVisible(true);
	    }
    public void showHomeScreen() {
        desktopPane.removeAll(); // מסיר את כל המסכים הפנימיים
        desktopPane.repaint();   // מעדכן את הרקע
     
    }
    
    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }
    
    
    public void configureForRole(String role) {
    	this.role = role;
    	menuItemUnproductiveEmployeesReport.setVisible(false);

        // הצגת מה שרלוונטי לתפקיד
        switch (role) {
            case "Marketing":
            	menuItemWines.setVisible(true);
                break;
            case "Sales":
            	menuItemUnproductiveEmployeesReport.setVisible(true);
                break;
            case "Customer":
            	menuItemImport.setVisible(false);
            	menuItemWines.setVisible(false);
            	menuItemManufacturers.setVisible(false);
            	menuReports.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unauthorized role: " + role);
                break;
        }
    }
    
    private void openRegularOrderScreen() {
        System.out.println("Opening Regular Order screen...");
        FrmRegularOrder regularOrderScreen = new FrmRegularOrder();
        desktopPane.add(regularOrderScreen);
        regularOrderScreen.moveToFront();
        regularOrderScreen.setVisible(true);
    }

    private void openUrgentOrderScreen() {
        System.out.println("Opening Urgent Order screen...");
        FrmUrgentOrder urgentOrderScreen = new FrmUrgentOrder();
        desktopPane.add(urgentOrderScreen);
        urgentOrderScreen.moveToFront();
        urgentOrderScreen.setVisible(true);
    }


    private void openNonProductiveEmployeeReportScreen() {
        System.out.println("Navigating to NonProductiveEmployeeReportScreen...");
        NonProductiveEmployeeReportScreen reportScreen = new NonProductiveEmployeeReportScreen(); 
        desktopPane.add(reportScreen);
        reportScreen.moveToFront();
        reportScreen.setVisible(true);
    }
    
    private void openWineInventoryReportScreen() {
        System.out.println("Opening Wine Inventory Report screen...");
        WineInventoryReportScreen inventoryReportScreen = new WineInventoryReportScreen();
        desktopPane.add(inventoryReportScreen);
        inventoryReportScreen.moveToFront();
        inventoryReportScreen.setVisible(true);
    }

    private void disableEditing(FrmWine winesScreen) {
        // מניעת עריכה של כל השדות במסך היין
        setEditableForAllComponents(winesScreen, false);
    }

    private void setEditableForAllComponents(Container container, boolean editable) {
        for (Component comp : container.getComponents()) {
            // אם הרכיב הוא JTextField, נבדוק אם זה שדה החיפוש
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                // נניח ששדה החיפוש הוא עם שם "searchField"
                if ("searchWineField".equals(textField.getName())) {
                    textField.setEditable(true);  // שדה החיפוש נשאר נגיש
                } else {
                    textField.setEditable(editable);  // שדות אחרים יהיו תלויים בתפקיד
                }
            } else if (comp instanceof JTextArea) {
                ((JTextArea) comp).setEditable(editable); // מבטל/מאפשר עריכה
            } else if (comp instanceof JPasswordField) {
                ((JPasswordField) comp).setEditable(editable); // מבטל/מאפשר עריכה
            } else if (comp instanceof JComboBox) {
                JComboBox comboBox = (JComboBox) comp;
                // מבטל את האפשרות לשנות את הערך ב- JComboBox
                comboBox.setEnabled(editable);  // ניתן להפעיל/לנטרל את ה- JComboBox
            } else if (comp instanceof JYearChooser) {
                JYearChooser yearChooser = (JYearChooser) comp;
                // מבטל את האפשרות לבחור שנה ב-JYearChooser
                yearChooser.setEnabled(editable);  // מבטל/מאפשר את השימוש ב-JYearChooser
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                // אם הכפתור הוא כפתור ה-Delete, נניח שאתה נותן לו מזהה ייחודי, לדוגמה:
                if (button.getText().equals("Delete")) {
                    button.setEnabled(editable);  // מבטל/מאפשר את כפתור ה-Delete
                }
                if (button.getText().equals("Save")) {
                    button.setEnabled(editable);  // מבטל/מאפשר את כפתור ה-Delete
                }
                if (button.getText().equals("Create")) {
                    button.setEnabled(editable);  // מבטל/מאפשר את כפתור ה-Delete
                }
            }

            // אם יש רכיבי פנימיים כמו JPanel, JScrollPane, או כל container אחר
            if (comp instanceof Container) {
                setEditableForAllComponents((Container) comp, editable);  // חוזר על רכיבי Container פנימיים
            }
        }
    }





}