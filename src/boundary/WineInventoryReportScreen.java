package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}