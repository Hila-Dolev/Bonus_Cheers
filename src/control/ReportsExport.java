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

import javax.swing.table.DefaultTableModel;

import entity.Employee;
import entity.Food;
import entity.StorageLocation;
import entity.Wine;
import entity.WineType;

import org.json.JSONArray;
import org.json.JSONObject;


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
	/*
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
    public void exportWineInventoryToFile(ArrayList<StorageLocation> wineStorages) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("wine_inventory_report.txt"))) {
            // עבור כל מחסן
            for (StorageLocation storage : wineStorages) {
                // כותרת למחסן
                writer.write("פרטי מחסן: " + storage.getStorageLocation() + "\n");
                writer.write("-------------------------------\n");

                // פרטי יינות
                writer.write("פרטי יינות:\n");
                for (WineInventory inventory : storage.getWineInventory()) {
                    writer.write("סוג יין: " + inventory.getWineType() + " - כמות: " + inventory.getQuantity() + "\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private JSONObject createStorageJsonObject(StorageLocation storageLocation, HashMap<Integer, Integer> wineQuantities) {
        JSONObject storageObject = new JSONObject();
        
        // הוספת פרטי המחסן
        storageObject.put("StorageLocation", storageLocation.getLocationNumber() + " - " + storageLocation.getStorageName());
        
        // יצירת JSONArray עבור המלאי של כל היינות במחסן
        JSONArray winesArray = createWinesJsonArray(wineQuantities);

        // הוספת המלאי של היינות למחסן
        storageObject.put("WineInventory", winesArray);

        return storageObject;
    }

    private JSONArray createWinesJsonArray(HashMap<Integer, Integer> wineQuantities) {
        JSONArray winesArray = new JSONArray();
        
        // חזור על כל היינות וייצא את המידע שלהם
        for (HashMap.Entry<Integer, Integer> wineEntry : wineQuantities.entrySet()) {
            int wineTypeID = wineEntry.getKey();
            int quantity = wineEntry.getValue();

            // קבלת שם היין לפי WineTypeID
            Wine wine = WineManagment.searchWineByCatalogNumber(wineTypeID);
            String wineName = (wine != null) ? wine.getName() : "Unknown Wine";

            // יצירת אובייקט JSON עבור כל יין
            JSONObject wineObject = new JSONObject();
            wineObject.put("WineType", wineName);
            wineObject.put("Quantity", quantity);

            winesArray.put(wineObject);
        }
        
        return winesArray;
    }
*/
 
}


