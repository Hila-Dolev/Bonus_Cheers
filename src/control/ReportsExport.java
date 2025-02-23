package control;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import entity.Employee;
import entity.Food;
import entity.StorageLocation;
import entity.Wine;
import entity.WineType;

import org.json.JSONArray;
import org.json.JSONObject;

import boundary.WineInventoryReportScreen;


public class ReportsExport {
	
	 private static ReportsExport instance;
	 
	 private ReportsExport() {}

	 public static ReportsExport getInstance() {
	        if (instance == null) {
	            instance = new ReportsExport();
	        }
	        return instance;
	    }
	
	public static ArrayList<Wine> filterWines(ArrayList<String> selectedWineTypes,ArrayList<String> selectedFoods, ArrayList<String> selectedOccasions) {

        // קבלת כל היינות במערכת
        ArrayList<Wine> allWines = WineManagment.getAllWines();

        // אם לא נבחר כלום, מחזירים את כל היינות
        if (selectedWineTypes.isEmpty() && selectedFoods.isEmpty() && selectedOccasions.isEmpty()) {
            return allWines;
        }

        // רשימה לסינון היינות
        LinkedHashSet<Wine> filteredWines = new LinkedHashSet<Wine>(allWines);
        System.out.println(filteredWines);
        
        // סינון לפי סוג יין
        if (!selectedWineTypes.isEmpty()) {
            filteredWines.removeIf(wine ->{
            	String wineTypeID = String.valueOf(wine.getWineTypeID());  
                return selectedWineTypes.stream().noneMatch(selected -> selected.startsWith(wineTypeID + " -"));
            });
            System.out.println("Filtered by Wine Types: " + filteredWines.size());
        }


        // סינון לפי סוג אוכל
        if (!selectedFoods.isEmpty()) {
            filteredWines.removeIf(wine -> {
            	ArrayList<Food> foodsForWineType = WineTypeManagement.getFoodsForWineType(wine.getWineTypeID());
            	 return foodsForWineType.stream().noneMatch(food -> selectedFoods.contains(food.getNameOfDish()));
            });
            System.out.println("Filtered by Foods: " + filteredWines.size());

        }

        // סינון לפי מאורע
        if (!selectedOccasions.isEmpty()) {
            filteredWines.removeIf(wine -> {
            	WineType wineType = WineTypeManagement.searchWineTypeBySerialNumber(wine.getWineTypeID());
            	if (wineType == null || !selectedOccasions.contains(wineType.getOccasion())) {
                    return true; // הסר את היין
                }
                return false; // השאר את היין ברשימה
            });
            System.out.println("Filtered by Occasions: " + filteredWines.size());

        }

        System.out.println(filteredWines);
        return new ArrayList<Wine>(filteredWines);
    }

	
	 // מתודה לייצוא דו"ח PDF
	/* 
    public boolean exportReport(ArrayList<Wine> filterdWines) {
    	try {
            // 1. יצירת מקור נתונים (JRDataSource) על בסיס רשימת היינות
            JRDataSource dataSource = new JRBeanCollectionDataSource(filterdWines);

         // 2. טעינת טמפלט Jasper (הקובץ JRXML) - שימוש ב-InputStream
            InputStream reportStream = null;
            try {
                reportStream = getClass().getResourceAsStream("/boundary/preferencesWinesReport.jrxml");

                if (reportStream == null) {
                    throw new FileNotFoundException("The JRXML was not found");
                }
            } catch (FileNotFoundException e) {
                System.err.println("The file was not found: " + e.getMessage());
                return false;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 3. הכנת פרמטרים (אם יש צורך - במקרה שלנו אין)
            HashMap<String, Object> parameters = new HashMap<>();

            // 4. יצירת JasperPrint על בסיס הדו"ח והנתונים
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 5. יצירת קובץ PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/wine_report.pdf");

            // 6. אם רוצים להציג את הדו"ח ב-Viewer
            JasperViewer.viewReport(jasperPrint, false);
            return true;
        } catch (JRException e) {
            e.printStackTrace();
            return false;
        }
    }

*/
	
	 // מתודה להחזרת עובדים לא פרודוקטיביים לפי תאריך
    public ArrayList<Employee> getNonProductiveEmployees(Date startDate, Date endDate) {
        ArrayList<Employee> allEmployees = PersonManagement.getInstance().getAllEmployees(); // השתמשנו ב- PersonManagement
        ArrayList<Employee> nonProductiveEmployees = new ArrayList<>();

        for (Employee employee : allEmployees) {
            int urgentOrders = PersonManagement.getInstance().countUrgentOrders(employee, startDate, endDate);
            int regularOrders = PersonManagement.getInstance().countRegularOrders(employee, startDate, endDate);

            // אם העובד עשה פחות מ-2 הזמנות דחופות או פחות מ-4 הזמנות רגילות
            if (urgentOrders < 2 || regularOrders < 4) {
                nonProductiveEmployees.add(employee);
            }
        }

        return nonProductiveEmployees;
    }

    
        // הפונקציה שתייצא את הדו"ח לקובץ JSON
    public void exportWineInventoryToFile(WineInventoryReportScreen winesInventoryScreen) {
    	// שולף את כל המחסנים
        HashMap<StorageLocation, HashMap<Integer, Integer>> storageWineQuantities = StorageManagement.getInstance().getWineTypeQuantitiesInEachStorage();

        // אם אין נתונים למחסנים, תצא מהמתודה
        if (storageWineQuantities.isEmpty()) {
            JOptionPane.showMessageDialog(winesInventoryScreen, "No storage data available.");
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
        try (FileWriter file = new FileWriter("wine_inventory_report.json")) {
            file.write(allStoragesJson.toString(4)); // יצוא ב-indentation של 4
            JOptionPane.showMessageDialog(winesInventoryScreen, "Export successful!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(winesInventoryScreen, "An error occurred during the export.");
            e.printStackTrace();
        }
    }

 
}


