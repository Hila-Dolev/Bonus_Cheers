package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Manufacturer;
import entity.Wine;

public class ManufacturerManagment {
	
	private static ManufacturerManagment instance;
	private ArrayList<Manufacturer> manufacturersList = new ArrayList<Manufacturer>();
	private static HashMap<String, String> deletedManufacturerData = new HashMap<>();

	public ArrayList<Manufacturer> getManufacturersList() {
		return manufacturersList;
	}

	public void setManufacturersList(ArrayList<Manufacturer> manufacturersList) {
		this.manufacturersList = manufacturersList;
	}
	
	public static ManufacturerManagment getInstance() {
        if (instance == null) {
            // אם האובייקט לא קיים, יוצר אותו
            instance = new ManufacturerManagment();
        }
        return instance;
    }
	
	public static ArrayList<Manufacturer> getAllManufacturers() {
	    ArrayList<Manufacturer> manufacturersList = new ArrayList<>();
	    String query = "SELECT * FROM TblManufacturer"; // שם הטבלה עבור היצרנים

	    try (Connection connection = DatabaseConnection.getConnection(); // חיבור למסד הנתונים
	         PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Manufacturer manufacturer = new Manufacturer(
	                rs.getInt("UniqueIdentifier"), // מזהה היצרן
	                rs.getString("Name"), // שם היצרן
	                rs.getString("phoneNumber"),
	                rs.getString("address"), 
	                rs.getString("email") // אימייל
	            );
	            manufacturersList.add(manufacturer);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return manufacturersList;
	}

	public void createNewManufacturer() throws SQLException {
		String checkSql = "SELECT COUNT(*) FROM TblManufacturer WHERE UniqueIdentifier = ?";
	    String insertSql = "INSERT INTO TblManufacturer (uniqueIdentifier, name, phoneNumber, address, email) VALUES (?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection()) {
	        
	        for (Manufacturer manufacturer : manufacturersList) {
	            // בדוק אם ה-uniqueIdentifier כבר קיים
	            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	                checkStmt.setInt(1, manufacturer.getUniqueIdentifier());
	                try (ResultSet rs = checkStmt.executeQuery()) {
	                    if (rs.next() && rs.getInt(1) == 0) {
	                        // אם לא קיים, הוסף את היצרן החדש
	                        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
	                            stmt.setInt(1, manufacturer.getUniqueIdentifier());
	                            stmt.setString(2, manufacturer.getName());
	                            stmt.setString(3, manufacturer.getPhoneNumber());
	                            stmt.setString(4, manufacturer.getAddress());
	                            stmt.setString(5, manufacturer.getEmail());
	                            stmt.executeUpdate();
	                        }
	                    } else {
	                        // אם קיים, אפשר להחליט אם להדפיס אזהרה או לדלג
	                        System.out.println("Manufacturer with uniqueIdentifier " + manufacturer.getUniqueIdentifier() + " already exists.");
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static HashMap<String, String> getDeletedManufacturerData() {
	    return deletedManufacturerData;
	}

	


	public static void deleteManufacturer(int uniqueIdentifier) {
	    // יצירת משתנה שמכיל את המידע של היצרן לפני המחיקה
	    HashMap<String, String> manufacturerData = null;
	    
	    try {
	        // שמירת המידע של היצרן לפני המחיקה
	        manufacturerData = getManufacturerInfoBeforeDeletion(uniqueIdentifier);
	        
	        // שלב 2: הוספת המידע למפה של היצרנים שנמחקו
	        if (manufacturerData != null && !manufacturerData.isEmpty()) {
	            String manufacturerId = manufacturerData.get("id");
	            String manufacturerName = manufacturerData.get("name");
	            ManufacturerManagment.getDeletedManufacturerData().put(manufacturerId, manufacturerName);
	        }
	        
	        // שלב 3: ביצוע המחיקה ממסד הנתונים
	        String query = "DELETE FROM TblManufacturer WHERE uniqueIdentifier = ?";
	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement stmt = connection.prepareStatement(query)) {
	            
	            stmt.setInt(1, uniqueIdentifier);  // הגדרת המזהה הייחודי של היצרן למחיקה
	            int rowsAffected = stmt.executeUpdate();
	            
	            if (rowsAffected == 0) {
	                System.out.println("No manufacturer found with unique identifier " + uniqueIdentifier);
	            } else {
	                System.out.println("Manufacturer with unique identifier " + uniqueIdentifier + " was deleted successfully.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    } catch (Exception e) {
	        System.out.println("Error retrieving manufacturer information before deletion: " + e.getMessage());
	        e.printStackTrace();
	    }
	}




	public static Manufacturer  searchManufacturer(int uniqueIdentifier) {
	ArrayList<Manufacturer> manufacturers = getAllManufacturers();  // קבלת כל היינות מהרשימה
	    for (Manufacturer manufacturer : manufacturers) {
	        if (manufacturer.getUniqueIdentifier() == uniqueIdentifier) {
	            return manufacturer;  
	        }
	    }
	    return null;
	}

	
	public static Manufacturer getManufacturerById(int uniqueIdentifier) {
        ArrayList<Manufacturer> manufacturers = getAllManufacturers();
        
        // חיפוש היצרן המתאים
        for (Manufacturer manufacturer : manufacturers) {
            if (manufacturer.getUniqueIdentifier() == uniqueIdentifier) {
                return manufacturer; // מחזירים את היצרן שמתאים למזהה
            }
        }
        
        return null; 
        }
	
	public void createManufacturer(Manufacturer manufacturer) throws SQLException {
	    String insertSql = "INSERT INTO TblManufacturer (UniqueIdentifier, Name, PhoneNumber, address, email) " +
	                       "VALUES (?, ?, ?, ?, ?)";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertSql)) {
	        stmt.setInt(1, manufacturer.getUniqueIdentifier());
	        stmt.setString(2, manufacturer.getName());
	        stmt.setString(3, manufacturer.getPhoneNumber());
	        stmt.setString(4, manufacturer.getAddress());
	        stmt.setString(5, manufacturer.getEmail());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SQLException("Error while inserting manufacturer: " + e.getMessage());
	    }
	}

	public static void updateManufacturer(Manufacturer manufacturer) throws Exception {
	    if (manufacturer == null) {
	        throw new IllegalArgumentException("Manufacturer cannot be null.");
	    }

	    // חיבור למסד הנתונים
	    try (Connection connection = DatabaseConnection.getConnection()) {
	        String sql = "UPDATE TblManufacturer SET Name = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE UniqueIdentifier = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setString(1, manufacturer.getName());
	            stmt.setString(2, manufacturer.getPhoneNumber());
	            stmt.setString(3, manufacturer.getAddress());
	            stmt.setString(4, manufacturer.getEmail());
	            stmt.setInt(5, manufacturer.getUniqueIdentifier());

	            int rowsUpdated = stmt.executeUpdate();
	            if (rowsUpdated == 0) {
	                throw new Exception("No manufacturer found with the specified uniqueIdentifier to update.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new Exception("Error while updating manufacturer: " + e.getMessage());
	    }
	}

	public static HashMap<String, String> getManufacturerInfoBeforeDeletion(int manufacturerId) {
	    String query = "SELECT UniqueIdentifier, Name FROM TblManufacturer WHERE UniqueIdentifier = ?";
	    HashMap<String, String> manufacturerData = new HashMap<>();
	    
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(query)) {
	         
	        stmt.setInt(1, manufacturerId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            String id = rs.getString("UniqueIdentifier");
	            String name = rs.getString("Name");
	            manufacturerData.put("id", id);
	            manufacturerData.put("name", name);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return manufacturerData;
	}

	

}