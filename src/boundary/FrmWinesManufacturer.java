package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import control.WineManagment;
import entity.SweetnessLevel;
import entity.Wine;
import boundary.WineRow;

import com.toedter.calendar.JYearChooser;

public class FrmWinesManufacturer extends JInternalFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private ArrayList<WineRow> winesList;
    private int manufacturerId; // המזהה של היצרן 

    public FrmWinesManufacturer(int manufacturerId) {
        super("Wine Management for Manufacturer", true, true, true, true); // כותרת והגדרת אפשרויות סגירה
        this.manufacturerId = manufacturerId; // שמירה על מזהה היצרן
       
        //Design
        this.getContentPane().setBackground(Color.WHITE);

        
        this.setLayout(new BorderLayout());
        
        // יצירת טבלה
        String[] columnNames = {
        	    "Catalog Number", "Manufacturer ID", "Name", "Description", "Year", "Price", "Sweetness Level", "Image Path", "Wine Type ID"
        	};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // כל התאים לא ניתנים לעריכה
            }
        };
        
         // going to the correct wine form when a roe is clicked
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int selectedRow = table.getSelectedRow(); // מזהה את השורה שעליה לחצו
                System.out.println("Row clicked: " + selectedRow); // לוג לזיהוי לחיצה
                if (selectedRow != -1) {
                    int catalogNumber = (int) table.getValueAt(selectedRow, 0); // קבלת מספר הקטלוג
                    System.out.println("Catalog number: " + catalogNumber); // לוג עבור מספר הקטלוג
                    openWineForm(catalogNumber); // פתיחת מסך היינות עם היין שנבחר
                }
            }
        });

        Color burgundyColor = Color.decode("#e4d1c3");
        table.getTableHeader().setBackground(burgundyColor);  // הצגת רקע בורדו בכותרת
        table.getTableHeader().setForeground(Color.decode("#252525")); 

       /*
        // הוספת ComboBox עבור עמודת Sweetness Level
        JComboBox<SweetnessLevel> sweetnessComboBox = new JComboBox<>(SweetnessLevel.values());
        TableColumn sweetnessColumn = table.getColumnModel().getColumn(6);
        sweetnessColumn.setCellEditor(new DefaultCellEditor(sweetnessComboBox));
     
        // יצירת CellEditor מותאם אישית עבור JYearChooser
        TableColumn yearColumn = table.getColumnModel().getColumn(4);
        yearColumn.setCellEditor(new YearChooserCellEditor());
*/
        // שליפת היינות עבור היצרן הספציפי
        loadWinesForManufacturer(manufacturerId);

        this.setSize(800, 600);
        this.setClosable(true);
        this.setResizable(true);
        this.setVisible(true); // הצגת המסך
    }

    public void loadWinesForManufacturer(int manufacturerID) {
        try {
            // שליפת כל היינות מהמאגר עבור היצרן הספציפי
            ArrayList<Wine> wines = WineManagment.getWinesByManufacturer(manufacturerID);
         
         // ניקוי רשימת היינות הקודמת
            if (winesList == null) {
                winesList = new ArrayList<>();
            }
            winesList.clear();
            
            // סינון והמרת Wine ל-WineRow
            for (Wine wine : wines) {
                WineRow wineRow = new WineRow(
                    wine.getCatalogNumber(), 
                    wine.getUniqueIdentifier(), 
                    wine.getName(), 
                    wine.getDescription(),
                    wine.getProductionYear(), 
                    wine.getPricePerBottle(), 
                    wine.getSweetnessLevel(),  
                    wine.getProductImagePath(),
                    wine.getWineTypeID()
                );
                winesList.add(wineRow); // הוספת WineRow לרשימה
            }

            // עדכון טבלת היינות
            updateTable();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading wines: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    
    public void updateTable() {
    	tableModel.setRowCount(0); // מחיקת שורות ישנות

        // הוספת כל WineRow שנמצא ברשימה
        for (WineRow wine : winesList) {
        	int year = wine.getProductionYear().getYear() + 1900;
            tableModel.addRow(new Object[] {
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
    }
    

    
    
    public void openWineForm(int catalogNumber) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Opening form for catalog number: " + catalogNumber); 
                Wine selectedWine = WineManagment.searchWineByCatalogNumber(catalogNumber); 

                if (selectedWine != null) {
                    FrmWine wineForm = new FrmWine(); 
                    wineForm.displayWine(selectedWine); 

                    HomeScreen homeScreen = (HomeScreen) SwingUtilities.getWindowAncestor(this); 
                    JDesktopPane desktop = homeScreen.getDesktopPane();
                    
                    // הוספת הטופס ל-DesktopPane אם הוא נמצא
                    desktop.add(wineForm);
                    wineForm.moveToFront();  
                    wineForm.setVisible(true);

                    desktop.setSelectedFrame(wineForm); 
                    

                } else {
                    JOptionPane.showMessageDialog(this, "Wine not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening wine form: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }



}
