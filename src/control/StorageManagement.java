package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.StorageLocation;
import entity.StoredIn;
import entity.Wine;

public class StorageManagement {

    private static StorageManagement instance;
    private static ArrayList<StorageLocation> storageLocationsList = new ArrayList<>();

    public static StorageManagement getInstance() {
        if (instance == null) {
            instance = new StorageManagement();
        }
        return instance;
    }

    public ArrayList<StorageLocation> getStorageLocationsList() {
        return storageLocationsList;
    }

    public void setStorageLocationsList(ArrayList<StorageLocation> storageLocationsList) {
        StorageManagement.storageLocationsList = storageLocationsList;
    }

    public static ArrayList<StorageLocation> getAllStorageLocations() {
        String query = "SELECT * FROM TblStorageLocation";
        
        try (Connection connection = DatabaseConnection.getConnection();  
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StorageLocation location = new StorageLocation(
                    rs.getInt("LocationNumber"),
                    rs.getString("Name")
                );
                storageLocationsList.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storageLocationsList;
    }

    public static HashMap<StorageLocation, HashMap<Integer, Integer>> getWineTypeQuantitiesInEachStorage() {
        HashMap<StorageLocation, HashMap<Integer, Integer>> storageWineQuantities = new HashMap<>();
        
        // ביצוע JOIN עם טבלת TblStorageLocation כדי לקבל את שם המיקום
        String query = "SELECT TblStoredIn.LocationNumber, TblStorageLocation.Name, TblStoredIn.catalogNumber, SUM(TblStoredIn.Quantity) AS TotalQuantity "
                     + "FROM TblStoredIn "
                     + "JOIN TblStorageLocation ON TblStoredIn.LocationNumber = TblStorageLocation.LocationNumber "
                     + "GROUP BY TblStoredIn.LocationNumber, TblStorageLocation.Name, TblStoredIn.catalogNumber";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int locationNumber = rs.getInt("LocationNumber");
                String locationName = rs.getString("Name"); // כאן אנחנו מקבלים את שם המיקום
                StorageLocation location = new StorageLocation(locationNumber, locationName);
                
                int wineID = rs.getInt("catalogNumber");
                int totalQuantity = rs.getInt("TotalQuantity");
                
                storageWineQuantities
                    .computeIfAbsent(location, k -> new HashMap<>())
                    .put(wineID, totalQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storageWineQuantities;
    }


}
