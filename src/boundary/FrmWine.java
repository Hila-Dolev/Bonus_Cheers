package boundary;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import com.toedter.calendar.JYearChooser;

import control.ManufacturerManagment;
import control.WineManagment;
import control.WineTypeManagement;
import entity.*;






public class FrmWine extends JInternalFrame {

	private JTextField catalogNumber;
	private JTextField manufacturerNumber;
	private JTextField name;
	private JTextField description;
	private JYearChooser productionYear;
	private JTextField pricePerBottle;
	private JComboBox sweetnessLevel;
	private JTextField productImagePath;
	private JButton btnSaveWine;
	private JButton btnDeleteWine;
	private JButton btnUpdateWine;
	private JButton btnCreateWine;
	private JTextField searchWineField = new JTextField(15);
	private JButton btnSearchWine;
	private JPanel buttonPanel = new JPanel(new FlowLayout());
	
	
	private JComboBox<Manufacturer> manufacturerComboBox = new JComboBox<>(); 

	private JComboBox<Integer> wineTypeComboBox = new JComboBox<>();

	
	private int currentWineIndex = 0;
    private ArrayList<Wine> wines;
    
	public FrmWine() {
        super("Wine Management", true, true, true, true); // כותרת והגדרת אפשרויות סגירה
        
        //Design
        Color buttonBackgroundColor = Color.decode("#6f2936");
        Color buttonTextColor = Color.WHITE; 
        Color fieldBackgroundColor = Color.WHITE; 

        UIManager.put("Button.background", buttonBackgroundColor);
        UIManager.put("Button.foreground", buttonTextColor);
        UIManager.put("TextField.background", fieldBackgroundColor);
        UIManager.put("ComboBox.background", fieldBackgroundColor);
        UIManager.put("ComboBox.foreground", Color.decode("#252525")); // תוכל להוסיף גם צבע טקסט אם תרצה
		UIManager.put("ComboBox.selectionBackground", Color.decode("#6f2936"));
		UIManager.put("ComboBox.selectionForeground", Color.WHITE);
		
		manufacturerComboBox.setBackground(Color.WHITE);  //Since it is a customizes ComboBox, we need to deal it directly
		manufacturerComboBox.setForeground(Color.decode("#252525")); 
		
		buttonPanel.setBackground(Color.WHITE);  // הגדרת צבע רקע לפאנל הראשי
        
		this.getContentPane().setBackground(Color.WHITE);
        
        

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); 

      
     // יצירת השדות
        catalogNumber = new JTextField(15);
        manufacturerNumber = new JTextField(15);
        name = new JTextField(15);
        description = new JTextField(15);
        productionYear = new JYearChooser();
        pricePerBottle = new JTextField(15);
        productImagePath = new JTextField(15);
        sweetnessLevel = new JComboBox<>(SweetnessLevel.values());

        // הוספת שדה לבחור יצרן מתוך ComboBox
        manufacturerComboBox.setModel(new DefaultComboBoxModel<>(ManufacturerManagment.getAllManufacturers().toArray(new Manufacturer[0])));
        manufacturerComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            	 Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            	 if (value instanceof Manufacturer) {
                    Manufacturer manufacturer = (Manufacturer) value;
                    String displayText = manufacturer.getUniqueIdentifier() + ", " + manufacturer.getName(); // הצגת שם היצרן עם מזהה
                    component = super.getListCellRendererComponent(list, displayText, index, isSelected, cellHasFocus);
                 }  else if (value instanceof String) {
                    // הצגת היצרן שנמחק כטקסט בלבד
                    String displayText = (String) value;
                    component = super.getListCellRendererComponent(list, displayText, index, isSelected, cellHasFocus);
                 }
            	 
            	 if (isSelected) {
                     component.setBackground(Color.decode("#6f2936"));  // צבע רקע עבור פריט שנבחר
                     component.setForeground(Color.WHITE);  // צבע טקסט עבור פריט שנבחר
                 } else {
                     component.setBackground(Color.WHITE);  // צבע רקע עבור פריט שלא נבחר
                     component.setForeground(Color.BLACK);  // צבע טקסט עבור פריט שלא נבחר
                 }

                return component;
                }
        });

        

        //Validation Check
        if (catalogNumber == null || manufacturerNumber == null || name == null ||
            description == null || productionYear == null || pricePerBottle == null ||
            sweetnessLevel == null || productImagePath == null) {
            System.err.println("Error: One or more fields are null");
        }
        
        // הוספת השדות לפאנל
        int row = 0;
        addFieldWithLabel("Catalog Number:", catalogNumber, gbc, row++);
        addFieldWithLabel("Manufacturer:", manufacturerComboBox, gbc, row++);
        addFieldWithLabel("Name:", name, gbc, row++);
        addFieldWithLabel("Description:", description, gbc, row++);
        addFieldWithLabel("Production Year:", productionYear, gbc, row++);
        addFieldWithLabel("Price Per Bottle:", pricePerBottle, gbc, row++);
        addFieldWithLabel("Sweetness Level:", sweetnessLevel, gbc, row++);
        addFieldWithLabel("Product Image Path:", productImagePath, gbc, row++);        initializeWineTypeComboBox();
        addFieldWithLabel("Wine Type:", wineTypeComboBox, gbc, row++);


        // יצירת כפתורים
        btnSaveWine = new JButton("Save");
        btnDeleteWine = new JButton("Delete");
        btnUpdateWine = new JButton("Update");
        btnCreateWine = new JButton("Create");
        btnSearchWine = new JButton("Search");
        
        // הוספת כפתורים
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        
        buttonPanel.add(new JLabel("Search:"));
        searchWineField.setName("searchWineField");
        buttonPanel.add(searchWineField);
        
        buttonPanel.add(btnSearchWine);
        btnSearchWine.addActionListener(e -> {
            String catalogNumberText = searchWineField.getText().trim();
            if (!catalogNumberText.isEmpty()) {
                try {
                    int catalogNumber = Integer.parseInt(catalogNumberText);
                    Wine foundWine = WineManagment.searchWineByCatalogNumber(catalogNumber);
                    if (foundWine != null) {
                        displayWine(foundWine);  // הצגת היין שמצאנו
                        JOptionPane.showMessageDialog(this, "Wine found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Wine not found.", "Search Result", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid catalog number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        buttonPanel.add(btnSaveWine);
        btnSaveWine.addActionListener(e -> {
            try {
            	
            	 // בדיקת מספרים חוקיים
                int catalogNum = Integer.parseInt(catalogNumber.getText());
                Manufacturer selectedManufacturer = (Manufacturer) manufacturerComboBox.getSelectedItem();
                int manufacturerId = selectedManufacturer.getUniqueIdentifier();
                double price = Double.parseDouble(pricePerBottle.getText());
                int selectedWineType = (Integer) wineTypeComboBox.getSelectedItem();
               

                Date productionDate = new Date(productionYear.getYear() - 1900, 0, 1); 

                if (currentWineIndex == -1) {
                    // יצירת יין חדש
                    Wine newWine = new Wine(
                        Integer.parseInt(catalogNumber.getText()),
                        manufacturerId,
                        name.getText(),
                        description.getText(),
                        productionDate,
                        Double.parseDouble(pricePerBottle.getText()),
                        SweetnessLevel.valueOf(sweetnessLevel.getSelectedItem().toString()),
                        productImagePath.getText(),
                        selectedWineType
                    );

                    // יצירת יין חדש במערכת
                    WineManagment.getInstance().createWine(newWine);

                    // עדכון רשימת היינות
                    wines.add(newWine);

                    // עדכון ה-indices
                    currentWineIndex = wines.size() - 1;  // עדכון האינדקס למיקום של היין החדש

                    catalogNumber.setEditable(false); 
                    
                    JOptionPane.showMessageDialog(this, "Wine created successfully.");
                } else {
                    // עדכון יין קיים
                    Wine updatedWine = wines.get(currentWineIndex);
                    updatedWine.setCatalogNumber(catalogNum);
                    updatedWine.setUniqueIdentifier(manufacturerId); 
                    updatedWine.setName(name.getText());
                    updatedWine.setDescription(description.getText());
                    updatedWine.setProductionYear(productionDate); // שימוש ב-Date
                    updatedWine.setPricePerBottle(price);
                    updatedWine.setSweetnessLevel(SweetnessLevel.valueOf(sweetnessLevel.getSelectedItem().toString()));
                    updatedWine.setProductImagePath(productImagePath.getText());
                    updatedWine.setWineTypeID(selectedWineType);
                    
                    // שמירת העדכון
                    WineManagment.getInstance().updateWine(updatedWine);

                    wines.set(currentWineIndex, updatedWine);

                    catalogNumber.setEditable(false);
                    
                    JOptionPane.showMessageDialog(this, "Wine updated successfully.");
                }

                loadWineData(); // רענון הנתונים בטופס
            } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving wine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        
        
        buttonPanel.add(btnDeleteWine);
        btnDeleteWine.addActionListener(e -> {
            int catalogNum = Integer.parseInt(catalogNumber.getText());
            try {
                WineManagment.getInstance().deleteWine(catalogNum);
                JOptionPane.showMessageDialog(this, "Wine deleted successfully.");
                // עדכון רשימת היינות על ידי הסרת היין שנמחק
                wines.removeIf(wine -> wine.getCatalogNumber() == catalogNum);

                // אם הרשימה ריקה, יש לנקות את השדות
                if (wines.isEmpty()) {
                    clearFields();
                } else {
                    // עדכון התצוגה עם היין הבא ברשימה
                    currentWineIndex = Math.min(currentWineIndex, wines.size() - 1); // עדכון אינדקס אם הוא אחרי הסוף
                    displayWine(wines.get(currentWineIndex));  // הצגת היין החדש
                }

                // רענון התצוגה
                revalidate();
                repaint();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting wine: " + ex.getMessage());
            }
        });

        
        
        
        buttonPanel.add(btnCreateWine);
        btnCreateWine.addActionListener(e -> {
           clearFields();
           currentWineIndex = -1; // אינדקס של יין חדש
           catalogNumber.setEditable(true);
           
           JOptionPane.showMessageDialog(this, "Ready to create a new wine.");
        });
        
        
       

        
        
        this.add(buttonPanel, gbc);

        // הוספת כפתורים למעבר בין היינות
        JButton btnNextWine = new JButton(">>");
        btnNextWine.addActionListener(e -> nextWine());
        JButton btnPreviousWine = new JButton("<<");
        btnPreviousWine.addActionListener(e -> previousWine());
        buttonPanel.add(btnPreviousWine);
        buttonPanel.add(btnNextWine);
        

        
     // שליפת כל היינות
        loadWineData();

        this.setSize(800, 600); 
        this.setClosable(true); 
        this.setResizable(true); 
        
        try {
            this.setIconifiable(true); // אפשרות למזער את המסך
            this.setMaximum(true); // הפוך את החלון למקסימלי
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while maximizing window: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.setVisible(true); // הצגת המסך
	}
	
	public void addFieldWithLabel(String labelText, JComponent field, GridBagConstraints gbc, int row) {
        if (labelText == null || field == null) {
            throw new IllegalArgumentException("Label text or field cannot be null");
        }

        // הוספת תווית
        gbc.gridx = 0; // עמודה 0
        gbc.gridy = row; // השורה
        this.add(new JLabel(labelText), gbc);

        // הוספת שדה קלט
        gbc.gridx = 1; // עמודה 1
        this.add(field, gbc);
    }
	
	public void loadWineData() {
        wines = WineManagment.getAllWines();
        if (wines == null) {
            wines = new ArrayList<>(); // יצירת רשימה ריקה אם אין נתונים
        }
        if (!wines.isEmpty()) {
            displayWine(wines.get(currentWineIndex)); 
        }
    }
	
	public void displayWine(Wine wine) {
		  initializeWineTypeComboBox();
        // הצגת הנתונים בשדות
        catalogNumber.setText(String.valueOf(wine.getCatalogNumber()));
        Manufacturer manufacturer = ManufacturerManagment.getManufacturerById(wine.getUniqueIdentifier());

        // אם היצרן לא נמצא (הוא נמחק), נוסיף יצרן שנמחק
        if (manufacturer == null) {
            // היצרן נמחק - יצירת אובייקט יצרן שנמחק עם מזהה לא תקני
            Manufacturer deletedManufacturer = new Manufacturer();
            deletedManufacturer.setUniqueIdentifier(-1);  // מזהה לא חוקי
            deletedManufacturer.setName("Manufacturer Deleted (ID: " + wine.getUniqueIdentifier() + ")");
            
            // הצגת היצרן שנמחק ב-ComboBox
            manufacturerComboBox.removeAllItems();  // נוודא שאין פריטים ישנים
            manufacturerComboBox.addItem(deletedManufacturer);
            manufacturerComboBox.setEnabled(false); // השבתת אפשרות לבחור ביצרן שנמחק
        } else {
            // היצרן קיים - הצגת היצרן ב-ComboBox
            // נטען את כל היצרנים הקיימים
            manufacturerComboBox.setModel(new DefaultComboBoxModel<>(ManufacturerManagment.getAllManufacturers().toArray(new Manufacturer[0])));
            manufacturerComboBox.setSelectedItem(manufacturer);
            manufacturerComboBox.setEnabled(true); // אפשרות לבחור ביצרן קיים
        }
        name.setText(wine.getName());
        description.setText(wine.getDescription());
        int year = wine.getProductionYear().getYear() + 1900;
        productionYear.setValue(year);
        pricePerBottle.setText(String.valueOf(wine.getPricePerBottle()));
        sweetnessLevel.setSelectedItem(wine.getSweetnessLevel());
        productImagePath.setText(wine.getProductImagePath());
        wineTypeComboBox.setSelectedItem(wine.getWineTypeID());
        catalogNumber.setEditable(false);
    }
	
	public void nextWine() {
	        // מעבר ליין הבא
	        if (wines != null && wines.size() > currentWineIndex + 1) {
	            currentWineIndex++;
	            displayWine(wines.get(currentWineIndex));
	        }
	    }
	
	public void previousWine() {
	        // מעבר ליין הקודם
	        if (wines != null && currentWineIndex > 0) {
	            currentWineIndex--;
	            displayWine(wines.get(currentWineIndex));
	        }
	    }
	 
	public void clearFields() {
		    catalogNumber.setText("");
		    manufacturerNumber.setText("");
		    name.setText("");
		    description.setText("");
		    productionYear.setValue(2023); // שנה ברירת מחדל (או כל שנה שתבחרי)
		    pricePerBottle.setText("");
		    sweetnessLevel.setSelectedIndex(0); // בחירה הראשונה בקומבו
		    productImagePath.setText("");
		}

	
	// אתחול הקומבו בוקס של סוגי יין
	private void initializeWineTypeComboBox() {
	    // שליפת רשימת סוגי היינות
	    ArrayList<WineType> wineTypes = WineTypeManagement.getAllWineTypes();
	    
	    // יצירת רשימת מזהי סוגי היין בלבד
	    Integer[] wineTypeIds = wineTypes.stream()
	                                     .map(WineType::getSerialNumber)
	                                     .toArray(Integer[]::new);
	    
	    // הגדרת המודל ל-ComboBox
	    wineTypeComboBox.setModel(new DefaultComboBoxModel<>(wineTypeIds));
	    
	    wineTypeComboBox.setBackground(Color.WHITE); 
	    wineTypeComboBox.setForeground(Color.decode("#252525")); 
	  
	    wineTypeComboBox.setRenderer(new DefaultListCellRenderer() {
	        @Override
	        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	            if (value instanceof Integer) {
	                setText("Type ID:" + value.toString()); 
	            }
	            if (isSelected) {
	                component.setBackground(Color.decode("#6f2936"));  // צבע רקע לפריט שנבחר
	                component.setForeground(Color.WHITE);             // צבע טקסט לפריט שנבחר
	            } else {
	                component.setBackground(Color.WHITE);             // צבע רקע לפריט שלא נבחר
	                component.setForeground(Color.BLACK);             // צבע טקסט לפריט שלא נבחר
	            }
	            
	            return component;
	        }
	    });
	}
	

	

}