package boundary;

import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import control.ManufacturerManagment;
import control.WineManagment;
import entity.Manufacturer;
import java.util.List;

public class FrmManufacturer extends JInternalFrame {

	private JTextField uniqueIdentifier;
	private JTextField name;
	private JTextField phoneNumber;
	private JTextField address;
	private JTextField email;
	private JButton btnSave;
	private JButton btnDelete;
    private JButton btnCreate;
    private JButton btnNext;
    private JButton btnPrevious;
    private JTextField searchManufacturerField;
    private JButton btnSearchManufacturer;
    private JPanel mainPanel = new JPanel(new GridBagLayout());;
    private JPanel searchPanel = new JPanel(new FlowLayout());;

    
    private FrmWinesManufacturer winesForm; // הטופס המשני
    private List<Manufacturer> manufacturers;
    private int currentIndex = 0;

    
	public FrmManufacturer() {
		super("Manufacturer Management", true, true, true, true);

		//Design
		Color buttonBackgroundColor = Color.decode("#6f2936");
        Color buttonTextColor = Color.WHITE; // לבן

        UIManager.put("Button.background", buttonBackgroundColor);
        UIManager.put("Button.foreground", buttonTextColor);
        
        this.getContentPane().setBackground(Color.WHITE);
        mainPanel.setBackground(Color.WHITE);  // הגדרת צבע רקע לפאנל הראשי
        searchPanel.setBackground(Color.WHITE); // הגדרת צבע רקע לפאנל החיפוש

        
        // הגדרת פריסת המסך
        this.setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // שדות הקלט
        uniqueIdentifier = new JTextField(15);
        name = new JTextField(15);
        address = new JTextField(15);
        phoneNumber = new JTextField(15);
        email = new JTextField(15);

        // Buttons
        searchManufacturerField = new JTextField(15);
        btnSearchManufacturer = new JButton("Search");
        btnSave = new JButton("Save");
        btnDelete = new JButton("Delete");
        btnCreate = new JButton("Create");
        btnNext = new JButton(">>");
        btnPrevious = new JButton("<<");

        // הוספת שדות הקלט למסך
        int row = 0;
        addFieldWithLabel("Manufacturer ID:", uniqueIdentifier, gbc, mainPanel, row++);
        addFieldWithLabel("Manufacturer Name:", name, gbc, mainPanel, row++);
        addFieldWithLabel("Address:", address, gbc, mainPanel, row++);
        addFieldWithLabel("Phone Number:", phoneNumber, gbc, mainPanel, row++);
        addFieldWithLabel("Email:", email, gbc, mainPanel, row++);
        
        uniqueIdentifier.setEditable(false);
        
        searchPanel.add(new JLabel("Search by ID:"));
        searchPanel.add(searchManufacturerField);
        searchPanel.add(btnSearchManufacturer);
        
     // כפתורים - הוספת כפתורים למיקומים שונים ב- GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        mainPanel.add(searchPanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        mainPanel.add(btnSave, gbc);

        gbc.gridx = 1;
        mainPanel.add(btnDelete, gbc);

        gbc.gridx = 2;
        mainPanel.add(btnCreate, gbc);

        row++; // הגדלת שורה לאחר כפתורים

        // כפתורים "Next" ו-"Previous"
        gbc.gridx = 3;
        mainPanel.add(btnPrevious, gbc);

        gbc.gridx = 4;
        mainPanel.add(btnNext, gbc);

        // טופס משני
        this.add(mainPanel, BorderLayout.NORTH);
        this.setSize(800, 600);
        this.setVisible(true);

        // טעינת נתונים
        loadManufacturers();

        // פעולות כפתורים
        btnNext.addActionListener(e -> showNextManufacturer());
        btnPrevious.addActionListener(e -> showPreviousManufacturer());
        
        btnSearchManufacturer.addActionListener(e -> {
        	 String manufacturerIdText = searchManufacturerField.getText();
        	    Manufacturer foundManufacturer = findManufacturerById(manufacturerIdText); 
        	    if (foundManufacturer != null) {
        	        showManufacturer(foundManufacturer);
        	    }
        });
        
        btnSave.addActionListener(e -> {
            try {
                // הגדרת הנתונים שהוזנו
                int id = Integer.parseInt(uniqueIdentifier.getText());
                String nameText = name.getText();
                String phone = phoneNumber.getText();
                String addr = address.getText();
                String mail = email.getText();
                
                // יצירת אובייקט Manufacturer
                Manufacturer manufacturer = new Manufacturer(id, nameText, phone, addr, mail);
                
                if (currentIndex == -1) {
                    // אם אינדקס הוא -1, מדובר ביצרן חדש, נשמור אותו
                    ManufacturerManagment.getInstance().createManufacturer(manufacturer);
                    manufacturers.add(manufacturer); // הוספת היצרן החדש לרשימה המקומית
                    
                    currentIndex = manufacturers.size() - 1;
                    
                    JOptionPane.showMessageDialog(this, "Manufacturer created successfully.");
                } else {
                    // אם אינדקס לא -1, מדובר בעדכון של יצרן קיים
                    Manufacturer existingManufacturer = manufacturers.get(currentIndex);
                    existingManufacturer.setUniqueIdentifier(id);
                    existingManufacturer.setName(nameText);
                    existingManufacturer.setPhoneNumber(phone);
                    existingManufacturer.setAddress(addr);
                    existingManufacturer.setEmail(mail);
                    
                    ManufacturerManagment.getInstance().updateManufacturer(manufacturer); 

                    manufacturers.set(currentIndex, existingManufacturer); // עדכון הרשימה המקומית
                    
                    JOptionPane.showMessageDialog(this, "Manufacturer updated successfully.");
                }

                loadManufacturers(); // רענן את רשימת היצרנים לאחר השמירה
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving manufacturer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCreate.addActionListener(e -> {
            clearFields(); // נקה את השדות
            currentIndex = -1; // אינדקס לא קיים (הכנה ליצירת יצרן חדש)
            uniqueIdentifier.setEditable(true); // אם יש צורך, תוכל לאפשר עריכה של המזהה הייחודי
            
            JOptionPane.showMessageDialog(this, "Ready to create a new manufacturer.");
        });
        
        btnDelete.addActionListener(e -> {
            int manufacturerId = Integer.parseInt(uniqueIdentifier.getText());
            try {
                // מחיקת היצרן
                ManufacturerManagment.getInstance().deleteManufacturer(manufacturerId);
                JOptionPane.showMessageDialog(this, "Manufacturer deleted successfully.");
                
                // עדכון רשימת היצרנים על ידי הסרת היצרן שנמחק
                manufacturers.removeIf(manufacturer -> manufacturer.getUniqueIdentifier() == manufacturerId);

                // אם הרשימה ריקה, יש לנקות את השדות
                if (manufacturers.isEmpty()) {
                    clearFields();
                } else {
                    // עדכון התצוגה עם היצרן הבא ברשימה
                    currentIndex = Math.min(currentIndex, manufacturers.size() - 1); // עדכון אינדקס אם הוא אחרי הסוף
                    showManufacturer(manufacturers.get(currentIndex));  // הצגת היצרן החדש
                }

                // רענון התצוגה
                revalidate();
                repaint();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting manufacturer: " + ex.getMessage());
            }
        });
    }

    public void addFieldWithLabel(String labelText, JComponent field, GridBagConstraints gbc, JPanel panel, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public void loadManufacturers() {
        try {
            manufacturers = ManufacturerManagment.getAllManufacturers();
            if (!manufacturers.isEmpty()) {
                showManufacturer(manufacturers.get(currentIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading manufacturers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showManufacturer(Manufacturer manufacturer) {
    	uniqueIdentifier.setText(String.valueOf(manufacturer.getUniqueIdentifier()));
    	name.setText(manufacturer.getName());
        address.setText(manufacturer.getAddress());
        phoneNumber.setText(manufacturer.getPhoneNumber());
        email.setText(manufacturer.getEmail());

        if (currentIndex != -1) {
            if (winesForm != null) {
                this.remove(winesForm); // הסרת הטופס הקודם
            }

            // יצירת טופס היינות עבור היצרן החדש
            winesForm = new FrmWinesManufacturer(manufacturer.getUniqueIdentifier());
            this.add(winesForm, BorderLayout.CENTER); // הוספת טופס חדש
            winesForm.loadWinesForManufacturer(manufacturer.getUniqueIdentifier()); // טעינת היינות של היצרן
            this.revalidate(); // רענון הממשק הגרפי
            this.repaint(); // רענון הצגת הממשק
        }
    }

    public void showNextManufacturer() {
        if (currentIndex < manufacturers.size() - 1) {
            currentIndex++;
            showManufacturer(manufacturers.get(currentIndex));
        }
    }

    public void showPreviousManufacturer() {
        if (currentIndex > 0) {
            currentIndex--;
            showManufacturer(manufacturers.get(currentIndex));
        }
    }


    

    


    
    public void clearFields() {
        uniqueIdentifier.setText("");
        name.setText("");
        phoneNumber.setText("");
        address.setText("");
        email.setText("");
    }
    

    public Manufacturer findManufacturerById(String id) {
    	 if (id == null || id.trim().isEmpty()) {
    	        JOptionPane.showMessageDialog(null, "Please enter a manufacturer ID to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
    	        return null;
    	    }
    	 try {
    	        int manufacturerId = Integer.parseInt(id.trim());
    	        Manufacturer foundManufacturer = ManufacturerManagment.searchManufacturer(manufacturerId);
    	        if (foundManufacturer != null) {
    	            return foundManufacturer; // מחזירים את היצרן שנמצא
    	        } else {
    	            JOptionPane.showMessageDialog(null, "Manufacturer not found.", "Search Result", JOptionPane.WARNING_MESSAGE);
    	            return null;
    	        }
    	    } catch (NumberFormatException ex) {
    	        JOptionPane.showMessageDialog(null, "Please enter a valid manufacturer ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
    	        return null;
    	    }
    	}


}