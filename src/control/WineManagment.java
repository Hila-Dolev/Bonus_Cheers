package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import boundary.WineRow;
import entity.Manufacturer;
import entity.SweetnessLevel;
import entity.Wine;

public class WineManagment {

	private static WineManagment instance;
	private static ArrayList<Wine> winesList = new ArrayList<Wine>();
	

	public static WineManagment getInstance() {
        if (instance == null) {
            // אם האובייקט לא קיים, יוצר אותו
            instance = new WineManagment();
        }
        return instance;
    }

	
	public ArrayList<Wine> getWinesList() {
		return winesList;
	}

	public void setWinesList(ArrayList<Wine> winesList) {
		this.winesList = winesList;
	}

	
	public static ArrayList<Wine> getAllWines() {
		String query = "SELECT * FROM TblWine";
		
		 try (Connection connection = DatabaseConnection.getConnection();  // חיבור דרך DatabaseConnection
	             PreparedStatement stmt = connection.prepareStatement(query);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                Wine wine = new Wine(
	                    rs.getInt("catalogNumber"),
	                    rs.getInt("uniqueIdentifier"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getDate("productionYear"),
	                    rs.getDouble("pricePerBottle"),
	                    SweetnessLevel.valueOf(rs.getString("sweetnessLevel")),
	                    rs.getString("productImagePath"),
	                    rs.getInt("WineTypeID")
	                );
	                winesList.add(wine);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return winesList;
	    }
	

	public void createNewWine() throws SQLException {
	    String checkSql = "SELECT COUNT(*) FROM TblWine WHERE catalogNumber = ?";  
	    String insertSql = "INSERT INTO TblWine (catalogNumber, name, description, productionYear, sweetnessLevel, productImagePath, uniqueIdentifier, WineTypeID, pricePerBottle) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


	    try (Connection conn = DatabaseConnection.getConnection()) {
	        
	        for (Wine wine : winesList) {
	            // בדוק אם ה-catalogNumber כבר קיים
	            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	                checkStmt.setInt(1, wine.getCatalogNumber());  
	                try (ResultSet rs = checkStmt.executeQuery()) {
	                    if (rs.next() && rs.getInt(1) == 0) {
	                        // אם לא קיים, הוסף את היין החדש
	                        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
	                            stmt.setInt(1, wine.getCatalogNumber());
	                            stmt.setString(2, wine.getName());
	                            stmt.setString(3, wine.getDescription());
	                            stmt.setDate(4, new java.sql.Date(wine.getProductionYear().getTime()));
	                            stmt.setString(5, wine.getSweetnessLevel().toString());  
	                            stmt.setString(6, wine.getProductImagePath());
	                            stmt.setInt(7, wine.getUniqueIdentifier());  
	                            stmt.setInt(8, wine.getWineTypeID());
	                            stmt.setDouble(9, wine.getPricePerBottle());  
	                            stmt.executeUpdate();
	                        }
	                    } else {
	                        // אם קיים, אפשר להחליט אם להדפיס אזהרה או לדלג
	                        System.out.println("Wine with catalogNumber " + wine.getCatalogNumber() + " already exists.");
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void updateWine(Wine wine) throws Exception {
	    if (wine == null) {
	        throw new IllegalArgumentException("Wine cannot be null.");
	    }

	    // חיבור למסד הנתונים
	    try (Connection connection = DatabaseConnection.getConnection()) {
	    	String sql = "UPDATE TblWine SET Name = ?, Description = ?, ProductionYear = ?, PricePerBottle = ?, SweetnessLevel = ?, ProductImagePath = ?, WineTypeID = ?, UniqueIdentifier = ? WHERE catalogNumber = ?";
	    	try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	    	    stmt.setString(1, wine.getName());
	    	    stmt.setString(2, wine.getDescription());
	    	    stmt.setDate(3, new java.sql.Date(wine.getProductionYear().getTime()));
	    	    stmt.setDouble(4, wine.getPricePerBottle());
	    	    stmt.setString(5, wine.getSweetnessLevel().name());
	    	    stmt.setString(6, wine.getProductImagePath());
	    	    stmt.setInt(7, wine.getUniqueIdentifier());
	    	    stmt.setInt(8, wine.getWineTypeID());  
	            stmt.setInt(9, wine.getCatalogNumber());

	            int rowsUpdated = stmt.executeUpdate();
	            if (rowsUpdated == 0) {
	                throw new Exception("No wine found with the specified details to update.");
	            }
	        }
	    }
	    }

	
	public static void updateWine(WineRow wineRow) throws Exception {
	    if (wineRow == null) {
	        throw new IllegalArgumentException("WineRow cannot be null.");
	    }

	    // המרת WineRow ל-Wine
	    Wine wine = new Wine(
	        wineRow.getCatalogNumber(),
	        wineRow.getUniqueIdentifier(),
	        wineRow.getName(),
	        wineRow.getDescription(),
	        wineRow.getProductionYear(), // assuming it's Date, adjust if needed
	        wineRow.getPricePerBottle(),
	        SweetnessLevel.valueOf(wineRow.getSweetnessLevel().toString()),
	        wineRow.getProductImagePath(),
	        wineRow.getWineTypeID()
	    );

	    // קריאה למתודה הראשית
	    updateWine(wine);
	}
	
	public void createWine(Wine wine) throws SQLException {
	    String insertSql = "INSERT INTO TblWine (catalogNumber, uniqueIdentifier, name, description, productionYear, pricePerBottle, sweetnessLevel, productImagePath, WineTypeID) " +
	                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertSql)) {
	        stmt.setInt(1, wine.getCatalogNumber());
	        stmt.setInt(2, wine.getUniqueIdentifier());
	        stmt.setString(3, wine.getName());
	        stmt.setString(4, wine.getDescription());
	        stmt.setDate(5, new java.sql.Date(wine.getProductionYear().getTime()));
	        stmt.setDouble(6, wine.getPricePerBottle());
	        stmt.setString(7, wine.getSweetnessLevel().toString());
	        stmt.setString(8, wine.getProductImagePath());
	        stmt.setInt(9, wine.getWineTypeID());
	        stmt.executeUpdate();
	    }
	}


	public void deleteWine(int catalogNumber) {
	    String query = "DELETE FROM TblWine WHERE catalogNumber = ?";
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        stmt.setInt(1, catalogNumber);
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected == 0) {
	            System.out.println("No wine found with catalog number " + catalogNumber);
	        } else {
	            System.out.println("Wine with catalog number " + catalogNumber + " was deleted successfully.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static ArrayList<Wine> getWinesByManufacturer(int manufacturerId) {
	    ArrayList<Wine> winesList = new ArrayList<>();
	    String query = "SELECT * FROM TblWine WHERE UniqueIdentifier = ?";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        // הגדרת פרמטר השאילתה
	        stmt.setInt(1, manufacturerId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Wine wine = new Wine(
	                    rs.getInt("catalogNumber"),
	                    rs.getInt("uniqueIdentifier"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getDate("productionYear"),
	                    rs.getDouble("pricePerBottle"),
	                    SweetnessLevel.valueOf(rs.getString("sweetnessLevel")),
	                    rs.getString("productImagePath"),
	                    rs.getInt("WineTypeID")
	                );
	                winesList.add(wine);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return winesList;
	}

	public static Wine searchWineByCatalogNumber(int catalogNumber) {
	    ArrayList<Wine> wines = getAllWines();  // קבלת כל היינות מהרשימה
	    for (Wine wine : wines) {
	        if (wine.getCatalogNumber() == catalogNumber) {
	            return wine;  
	        }
	    }
	    return null;
	}


	
}