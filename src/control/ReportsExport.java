package control;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import entity.Food;
import entity.Wine;
import entity.WineType;



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
}


