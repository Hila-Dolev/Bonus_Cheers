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

    public WineInventoryReportScreen() {
        setTitle("Wine Inventory Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

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
    storageComboBox.setPreferredSize(new Dimension(300, 30)); // גודל תיבת הקומבו
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
    // שולף את כל המחסנים
    HashMap<StorageLocation, HashMap<Integer, Integer>> storageWineQuantities = StorageManagement.getInstance().getWineTypeQuantitiesInEachStorage();

    // אם אין נתונים למחסנים, תצא מהמתודה
    if (storageWineQuantities.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No storage data available.");
        return;
    }

    // יצירת JSONObject עבור כל הנתונים
    JSONObject allStoragesJson = new JSONObject();
    JSONArray storagesArray = new JSONArray();

    // יצירת רשימה של המחסנים כדי למיין אותם לפי locationNumber
    List<Map.Entry<StorageLocation, HashMap<Integer, Integer>>> sortedStorageEntries = new ArrayList<>(storageWineQuantities.entrySet());

    // מיין את המחסנים לפי locationNumber (סדר עולה)
    sortedStorageEntries.sort((entry1, entry2) -> Integer.compare(entry1.getKey().getLocationNumber(), entry2.getKey().getLocationNumber()));

    // עבור כל מחסן במערכת, אחרי מיון
    for (Map.Entry<StorageLocation, HashMap<Integer, Integer>> entry : sortedStorageEntries) {
        StorageLocation location = entry.getKey();
        HashMap<Integer, Integer> wineQuantities = entry.getValue();

        // יצירת JSONArray להכיל את כל היינות במחסן
        JSONArray wineInventoryArray = new JSONArray();

        // עבור כל יין במחסן
        for (Map.Entry<Integer, Integer> wineEntry : wineQuantities.entrySet()) {
            int wineTypeID = wineEntry.getKey();
            int quantity = wineEntry.getValue();

            // חיפוש היין לפי WineTypeID
            Wine wine = WineManagment.searchWineByCatalogNumber(wineTypeID);
            if (wine != null) {
                // יצירת JSONObject עבור כל יין
                JSONObject wineJson = new JSONObject();
                JSONObject wineDetailsJson = new JSONObject();
                wineDetailsJson.put("wineName", wine.getName());
                wineDetailsJson.put("wineTypeID", wine.getWineTypeID());
                wineDetailsJson.put("description", wine.getDescription());
                wineDetailsJson.put("productionYear", wine.getProductionYear());
                wineDetailsJson.put("sweetnessLevel", wine.getSweetnessLevel());
                wineDetailsJson.put("productImagePath", wine.getProductImagePath());

                // הוספת פרטי היין ל־wineJson
                wineJson.put("wineDetails", wineDetailsJson);
                wineJson.put("quantity", quantity);
                
                // הוספת מחיר
                JSONObject priceJson = new JSONObject();
                priceJson.put("pricePerBottle", wine.getPricePerBottle());
                wineJson.put("price", priceJson);

                // הוספת היין ל־JSONArray
                wineInventoryArray.put(wineJson);
            }
        }

        // יצירת JSONObject עבור כל מחסן - עכשיו כל פרט המחסן יגיע קודם
        JSONObject storageJson = new JSONObject();
        storageJson.put("storageLocation", location.getStorageName());
        storageJson.put("locationNumber", location.getLocationNumber());
        storageJson.put("wineInventory", wineInventoryArray);

        // הוספת המחסן ל־JSONArray של כל המחסנים
        storagesArray.put(storageJson);
    }

    // הוספת כל המחסנים ל־JSONObject הראשי
    allStoragesJson.put("storages", storagesArray);

    // יצירת קובץ JSON
    try (FileWriter file = new FileWriter("wine_inventory_report_all_storages_pretty.json")) {
        file.write(allStoragesJson.toString(4)); // יצוא ב-indentation של 4
        JOptionPane.showMessageDialog(this, "Export successful!");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "An error occurred during the export.");
        e.printStackTrace();
    }
}

}