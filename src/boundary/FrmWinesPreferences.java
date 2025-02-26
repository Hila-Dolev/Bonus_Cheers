package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

import control.WineTypeManagement;
import entity.Food;
import entity.Wine;
import entity.WineType;
import control.ReportsExport;

public class FrmWinesPreferences extends JInternalFrame {

    //private JButton btnExportReport;
    private JButton btnShowReport;
    private JLabel lblSelectedFoods;
    private JLabel lblSelectedOccasions;
    private JLabel lblSelectedWineTypes;
    private JButton btnUpdatePreferredWines;
    private FrmOrder orderScreen;

    // שמירת הבחירות עבור כל גורם סינון
    private ArrayList<String> selectedWineTypes = new ArrayList<>();
    private ArrayList<String> selectedFoods = new ArrayList<>();
    private ArrayList<String> selectedOccasions = new ArrayList<>();


    public FrmWinesPreferences(FrmOrder orderScreen) {
    	super("Wines Preferences", true, true, true, true);
    	this.orderScreen = orderScreen;
    	initializeUI();
    	
    }
    
    public FrmWinesPreferences() {
    	super("Wines Preferences", true, true, true, true);
    	this.orderScreen = null;
    	initializeUI();
    }

    public void initializeUI () {
    	
        setLayout(new BorderLayout());
        this.setSize(780, 520); 
        this.setClosable(true); 
        this.setResizable(true); 
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        getContentPane().setBackground(Color.WHITE);

        
        // פאנל מרכזי
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(Color.WHITE); 
        panelCenter.setLayout(new GridLayout(4, 2, 10, 10));

        // תוויות
        JLabel lblWineType = new JLabel("Select Wine Type:");
        JLabel lblFood = new JLabel("Select Food:");
        JLabel lblOccasion = new JLabel("Select Occasion:");

        // הוספת רכיבים לפאנל
        panelCenter.add(lblWineType);
        lblSelectedWineTypes = new JLabel("Selected Wine Types: None");
        panelCenter.add(lblSelectedWineTypes);
        panelCenter.add(lblFood);
        lblSelectedFoods = new JLabel("Selected Foods: None"); 
        panelCenter.add(lblSelectedFoods);
        panelCenter.add(lblOccasion);
        lblSelectedOccasions = new JLabel("Selected Occasions: None");
        panelCenter.add(lblSelectedOccasions);

        add(panelCenter, BorderLayout.CENTER);

        
        // פאנל כפתורים
        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(Color.WHITE); 
        panelButtons.setLayout(new GridBagLayout()); // השתמש ב-GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        
      //Wine Types Chooser
        JButton btnSelectWineTypes = new JButton("Select Wine Types");
        btnSelectWineTypes.addActionListener(e -> openWineTypeSelectionDialog());
        
      //Foods Chooser
        JButton btnSelectFoods = new JButton("Select Foods");
        btnSelectFoods.addActionListener(e -> openFoodSelectionDialog());
       
        //Occasions Chooser
        JButton btnSelectOccasions = new JButton("Select Occasions");
        btnSelectOccasions.addActionListener(e -> openOccasionSelectionDialog());
      
        // כפתור יצירת דו"ח להצגה
        btnShowReport = new JButton("Show Report");
        btnShowReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReport();
            }
        });
        
        
        
     // הגדרת הגבלה לכל כפתור עם הוספת רווחים
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.CENTER;
        panelButtons.add(btnSelectWineTypes, gbc);
        
        gbc.gridx = 1;
        panelButtons.add(btnSelectFoods, gbc);
        
        gbc.gridx = 2;
        panelButtons.add(btnSelectOccasions, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelButtons.add(btnShowReport, gbc);
        
      
        
     // הכפתור יוצג רק אם המסך נפתח מתוך FrmOrder
        if (orderScreen != null) {
            btnUpdatePreferredWines = new JButton("Update Preferred Wines");
            btnUpdatePreferredWines.addActionListener(e -> updatePreferredWinesInOrder());
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            panelButtons.add(btnUpdatePreferredWines, gbc);
        }

        // הוספת פאנל הכפתורים למסך
        add(panelButtons, BorderLayout.SOUTH);
        setVisible(true);

    	
    }
    public void updatePreferredWinesInOrder() {
        ArrayList<Wine> preferredWines = ReportsExport.filterWines(selectedWineTypes, selectedFoods, selectedOccasions);
        
        if (preferredWines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No wines match your preferences!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
             JOptionPane.showMessageDialog(this, "Preferred wines have been updated in the order!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
        if (orderScreen != null) {
            orderScreen.updateWineList(preferredWines); 
        }
        
        this.dispose();
        
    }
    
    public void openWineTypeSelectionDialog() {
    	LinkedHashSet<String> wineTypeStrings = new LinkedHashSet<>();
        for (WineType wineType : WineTypeManagement.getAllWineTypes()) {
            wineTypeStrings.add(wineType.getSerialNumber() + " - " + wineType.getName());
        }

        // המרה ל-ArrayList כדי לשמור על סדר
        ArrayList<String> uniqueWineTypes = new ArrayList<>(wineTypeStrings);

        // יצירת פאנל עם CheckBoxes
        JPanel wineTypePanel = new JPanel();
        wineTypePanel.setLayout(new BoxLayout(wineTypePanel, BoxLayout.Y_AXIS));

        // יצירת checkboxes לכל סוג יין
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String wineTypeString : uniqueWineTypes) {
            JCheckBox checkBox = new JCheckBox(wineTypeString);
            checkBoxes.add(checkBox);
            wineTypePanel.add(checkBox);
        }

        // הצגת דיאלוג הבחירה
        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(wineTypePanel), "Select Wine Types", JOptionPane.OK_CANCEL_OPTION);

        // עיבוד הבחירות
        if (result == JOptionPane.OK_OPTION) {
            selectedWineTypes.clear(); // נקה את הבחירות הקודמות

            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedWineTypes.add(checkBox.getText());
                }
            }

            // עדכון תווית סוגי היין שנבחרו
            if (selectedWineTypes.isEmpty()) {
                lblSelectedWineTypes.setText("Selected Wine Types: None");
            } else {
                lblSelectedWineTypes.setText("Selected Wine Types: " + String.join(", ", selectedWineTypes));
            }
        }
        
    }
    

    public void openFoodSelectionDialog() {
    	HashSet<String> foodStrings = new HashSet<>();
        for (WineType wineType : WineTypeManagement.getAllWineTypes()) {
            foodStrings.addAll(WineTypeManagement.getFoodsForWineType(wineType.getSerialNumber())
                                .stream()
                                .map(Food::getNameOfDish)
                                .toList());
        }

        // המרה ל-ArrayList כדי לשמור על סדר
        ArrayList<String> uniqueFoods = new ArrayList<>(foodStrings);

        // יצירת פאנל עם CheckBoxes
        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));

        // יצירת checkboxes לכל מאכל
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String food : uniqueFoods) {
            JCheckBox checkBox = new JCheckBox(food);
            checkBoxes.add(checkBox);
            foodPanel.add(checkBox);
        }


           int result = JOptionPane.showConfirmDialog(this, new JScrollPane(foodPanel), "Select Foods", JOptionPane.OK_CANCEL_OPTION);

            // עיבוד הבחירות
            if (result == JOptionPane.OK_OPTION) {
                selectedFoods.clear(); // נקה את הבחירות הקודמות

                for (JCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        selectedFoods.add(checkBox.getText());
                    }
                }

                
                // עדכון תווית המאכלים שנבחרו
                if (selectedFoods.isEmpty()) {
                    lblSelectedFoods.setText("Selected Foods: None");
                } else {
                    lblSelectedFoods.setText("Selected Foods: " + String.join(", ", selectedFoods));
                }
                // רענון תווית
                lblSelectedFoods.revalidate();
                lblSelectedFoods.repaint();
            }
        }

        
    public void openOccasionSelectionDialog() {
        // איסוף כל ה-occasions מתוך רשימת WineType
        HashSet<String> occasions = new HashSet<>();
        for (WineType wineType : WineTypeManagement.getAllWineTypes()) {
            occasions.add(wineType.getOccasion());
        }

        ArrayList<String> occasionList = new ArrayList<>(occasions);

        // יצירת פאנל עם CheckBoxes
        JPanel occasionPanel = new JPanel();
        occasionPanel.setLayout(new BoxLayout(occasionPanel, BoxLayout.Y_AXIS));

        // יצירת checkboxes לכל occasion
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String occasion : occasionList) {
            JCheckBox checkBox = new JCheckBox(occasion);
            checkBoxes.add(checkBox);
            occasionPanel.add(checkBox);
        }

        // הצגת דיאלוג הבחירה
        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(occasionPanel), "Select Occasions", JOptionPane.OK_CANCEL_OPTION);

        // עיבוד הבחירות
        if (result == JOptionPane.OK_OPTION) {
            selectedOccasions.clear(); // נקה את הבחירות הקודמות

            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedOccasions.add(checkBox.getText());
                }
            }

            // עדכון תווית ה-occasions שנבחרו
            if (selectedOccasions.isEmpty()) {
                lblSelectedOccasions.setText("Selected Occasions: None");
            } else {
                lblSelectedOccasions.setText("Selected Occasions: " + String.join(", ", selectedOccasions));
            }
        }
    }

    public ArrayList<Wine> showReport() {
    	ArrayList<Wine> filteredWines = new ArrayList<Wine>();
    	
    	try {
            // קבלת רשימת היינות המסוננים
            filteredWines = ReportsExport.filterWines(selectedWineTypes, selectedFoods, selectedOccasions);

            if (filteredWines.isEmpty()) {
                int option = JOptionPane.showConfirmDialog(null, 
                        "No wines match your selection. Would you like to modify your preferences?", 
                        "No Wines Found", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE);
                
                if (option == JOptionPane.YES_OPTION) {
                    // קריאה למסך הבחירה מחדש
                    openWineTypeSelectionDialog(); 
                    openFoodSelectionDialog();
                    openOccasionSelectionDialog();
                    
                    showReport();
                }
                return filteredWines; // יוצא מהמתודה אם הרשימה ריקה
            }
            // יצירת דיאלוג להצגת היינות
            JDialog dialog = new JDialog();
            dialog.setTitle("Filtered Wines");
            dialog.setLayout(new BorderLayout());

            // יצירת טבלת יינות
            String[] columnNames = {
                "Catalog Number", "Manufacturer ID", "Name", "Description", "Year", "Price", "Sweetness Level", "Image Path", "Wine Type ID"
            };

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // כל התאים לא ניתנים לעריכה
                }
            };

            // הוספת יינות לטבלה
            for (Wine wine : filteredWines) {
                int year = wine.getProductionYear().getYear() + 1900; // שנה מתאריך
                tableModel.addRow(new Object[]{
                    wine.getCatalogNumber(),
                    wine.getUniqueIdentifier(),
                    wine.getName(),
                    wine.getDescription(),
                    year,
                    wine.getPricePerBottle(),
                    wine.getSweetnessLevel(),
                    wine.getProductImagePath(),
                    wine.getWineTypeID()
                });
            }

            // יצירת JTable וגלילה
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // הוספת כפתור סגירה
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(event -> dialog.dispose());
            dialog.add(closeButton, BorderLayout.SOUTH);

            // הגדרות דיאלוג
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null); // מרכז למסך
            dialog.setVisible(true);
            
           
            

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error displaying report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    	
    	 return filteredWines;
    	
    }

    
    
}

