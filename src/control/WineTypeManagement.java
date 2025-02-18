package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Food;
import entity.OccasionLocation;
import entity.Season;
import entity.WineType;

public class WineTypeManagement {

    private static WineTypeManagement instance;
    private static ArrayList<WineType> wineTypesList = new ArrayList<>();

    public static WineTypeManagement getInstance() {
        if (instance == null) {
            instance = new WineTypeManagement();
        }
        return instance;
    }

    public static ArrayList<WineType> getWineTypesList() {
        return wineTypesList;
    }

    public void setWineTypesList(ArrayList<WineType> wineTypesList) {
        this.wineTypesList = wineTypesList;
    }

    public static ArrayList<WineType> getAllWineTypes() {
        String query = "SELECT * FROM TblWineType";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                WineType wineType = new WineType(
                    rs.getInt("SerialNumber"),
                    rs.getString("Name"),
                    rs.getString("Occasion"),
                    OccasionLocation.valueOf(rs.getString("Location")),
                    Season.valueOf(rs.getString("Season"))
                    
                );
                wineTypesList.add(wineType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wineTypesList;
    
    }

    public void createNewWineType(WineType wineType) throws SQLException {
        String insertSql = "INSERT INTO TblWineType (SerialNumber, Name, Occasion, Location, Season) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setInt(1, wineType.getSerialNumber());
            stmt.setString(2, wineType.getName());
            stmt.setString(3, wineType.getOccasion());
            stmt.setString(4, wineType.getLocation().toString());
            stmt.setString(5, wineType.getSeason().toString());
            stmt.executeUpdate();
        }
    }

    public void updateWineType(WineType wineType) throws SQLException {
        String updateSql = "UPDATE TblWineType SET Name = ?, Occasion = ?, OccasionLocation = ?, Season = ? WHERE SerialNumber = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, wineType.getName());
            stmt.setString(2, wineType.getOccasion());
            stmt.setString(3, wineType.getLocation().toString());
            stmt.setString(4, wineType.getSeason().toString());
            stmt.setInt(5, wineType.getSerialNumber());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No wine type found with the specified ID to update.");
            }
        }
    }

    public void deleteWineType(int serialNumber) {
        String query = "DELETE FROM TblWineType WHERE SerialNumber = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, serialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No wine type found with SerialNumber " + serialNumber);
            } else {
                System.out.println("Wine type with SerialNumber " + serialNumber + " was deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static WineType searchWineTypeBySerialNumber(int serialNumber) {
        for (WineType wineType : wineTypesList) {
            if (wineType.getSerialNumber() == serialNumber) {
                return wineType;
            }
        }
        return null;
    }

    public static ArrayList<Food> getFoodsForWineType(int wineTypeId) {
        ArrayList<Food> foodsList = new ArrayList<>();
        
        // שלב ראשון: שלוף את מזהי המאכלים המתאימים מסוג היין מטבלת הקשר
        String query = "SELECT FoodID FROM TblFoodInWineType WHERE SerialNumber = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, wineTypeId);
            ResultSet rs = stmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No food entries found for wine type ID: " + wineTypeId);
            }
            // שלב שני: עבור כל מזהה אוכל שנמצא, שלוף את שם המאכל מתוך טבלת ה-TblFood
            while (rs.next()) {
                int foodSerialNumber = rs.getInt("FoodID");
                

                // שאילתה לשליפת נתוני המאכל המלאים
                String foodDetailsQuery = """
                    SELECT foodID, nameOfDish, RecipeLink1, RecipeLink2, RecipeLink3, RecipeLink4, RecipeLink5 
                    FROM TblFood 
                    WHERE foodID = ?
                """;
                try (PreparedStatement foodStmt = connection.prepareStatement(foodDetailsQuery)) {
                    foodStmt.setInt(1, foodSerialNumber);
                    ResultSet foodRs = foodStmt.executeQuery();
                    
                    if (foodRs.next()) {
                        int id = foodRs.getInt("foodID");
                        String name = foodRs.getString("nameOfDish");
                        
                        // שליפת הקישורים והמרת המחרוזות ל-URL
                        String recipeLink1 = foodRs.getString ("RecipeLink1");
                        String recipeLink2 = foodRs.getString("RecipeLink2");
                        String recipeLink3 = foodRs.getString("RecipeLink3");
                        String recipeLink4 = foodRs.getString("RecipeLink4");
                        String recipeLink5 = foodRs.getString("RecipeLink5");
                        
                        // יצירת אובייקט Food והוספתו לרשימה
                        Food food = new Food(id, name, recipeLink1, recipeLink2, recipeLink3, recipeLink4, recipeLink5);
                        foodsList.add(food);
                    } else {
                        System.out.println("No food details found for FoodID: " + foodSerialNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodsList;
    }
   

    


}
