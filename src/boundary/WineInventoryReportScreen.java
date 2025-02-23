package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.ReportsExport;
import control.StorageManagement;
import control.WineManagment;
import entity.*;


public class WineInventoryReportScreen extends JInternalFrame {
    private JPanel panel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<StorageLocation> storageComboBox;
    private Color burgundyColor;

    public WineInventoryReportScreen() {
        setTitle("Wine Inventory Report");
        setSize(780, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

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
		
		burgundyColor = Color.decode("#e4d1c3");
        
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        loadStorageComboBox();

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
        
     // הוספת כפתור יצוא JSON
        JButton exportButton = new JButton("Export to JSON");
        exportButton.addActionListener(e -> exportToJSON());
        panel.add(exportButton, BorderLayout.SOUTH);
        
    }
    
private void loadStorageComboBox() {
    // שולף את רשימת המחסנים
    HashMap<StorageLocation, HashMap<Integer, Integer>> storageWineQuantities = StorageManagement.getInstance().getWineTypeQuantitiesInEachStorage();
    
    // יצירת רשימה עם כל המחסנים
    List<StorageLocation> storageLocations = new ArrayList<>(storageWineQuantities.keySet());
    storageLocations.sort((s1, s2) -> Integer.compare(s1.getLocationNumber(), s2.getLocationNumber())); // מסדר את המחסנים לפי מספר המחסן

    // יצירת רשימה חדשה שתכיל את הטקסט המורחב של כל מחסן
    List<String> storageDisplayNames = new ArrayList<>();
    
    for (StorageLocation location : storageLocations) {
        String displayName = "Storage Number (" + location.getLocationNumber() + "): " + location.getStorageName();
        storageDisplayNames.add(displayName);
    }

    // יצירת JComboBox עם השמות המורחבים
    storageComboBox = new JComboBox<>(storageLocations.toArray(new StorageLocation[0]));
    storageComboBox.setPreferredSize(new Dimension(300, 20)); // גודל תיבת הקומבו
    storageComboBox.addActionListener(e -> {
	StorageLocation selectedStorage = (StorageLocation) storageComboBox.getSelectedItem();
        if (selectedStorage != null) {
            loadWineDataForStorage(selectedStorage); // טוען את המידע של המחסן הנבחר
        }
    });

    // הוספת JComboBox לפאנל העליון
    JPanel topPanel = new JPanel();
    topPanel.add(new JLabel("Select Storage:"));
    topPanel.add(storageComboBox);

    panel.add(topPanel, BorderLayout.NORTH);

    // יצירת טבלה שתתמלא כאשר נבחר מחסן
    String[] columns = {"Wine Type", "Quantity"};
    tableModel = new DefaultTableModel(columns, 0);
    table = new JTable(tableModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.getTableHeader().setBackground(burgundyColor);
    table.setFillsViewportHeight(true);

    JScrollPane tableScrollPane = new JScrollPane(table);
    panel.add(tableScrollPane, BorderLayout.CENTER);
}

private void loadWineDataForStorage(StorageLocation location) {
    // שולף את כמויות היין של המחסן הנבחר
    HashMap<StorageLocation, HashMap<Integer, Integer>> storageWineQuantities = StorageManagement.getInstance().getWineTypeQuantitiesInEachStorage();
    HashMap<Integer, Integer> wineQuantities = storageWineQuantities.get(location);
    
    if (wineQuantities == null) {
        return; // אם לא נמצאו כמויות ליין, יצא מהמתודה
    }

    // נקה את הטבלה
    tableModel.setRowCount(0);
    
    // הוסף את נתוני היין בטבלה
    for (Map.Entry<Integer, Integer> wineEntry : wineQuantities.entrySet()) {
        int wineTypeID = wineEntry.getKey();
        int quantity = wineEntry.getValue();

        // קבלת שם היין לפי WineTypeID
        Wine wine = WineManagment.searchWineByCatalogNumber(wineTypeID);
        String wineName = (wine != null) ? wine.getName() : "Unknown Wine";

        tableModel.addRow(new Object[]{wineName, quantity});
    }
}


private void exportToJSON() {
	ReportsExport.getInstance().exportWineInventoryToFile(this);
    
}

}