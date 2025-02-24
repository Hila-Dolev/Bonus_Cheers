package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import entity.RegularOrder;
import entity.UrgentOrder;
import entity.Wine;
import entity.Order;
import entity.OrderStatus;
import entity.PriorityLevel;

public class OrderManagement {

    private static OrderManagement instance;
    private static ArrayList<Order> ordersList = new ArrayList<>();

    public static OrderManagement getInstance() {
        if (instance == null) {
            instance = new OrderManagement();
        }
        return instance;
    }

    public ArrayList<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(ArrayList<Order> ordersList) {
        this.ordersList = ordersList;
    }

    /*public static ArrayList<Order> getAllOrders() {
        String query = "SELECT * FROM TblOrder";
        String regularOrderCheckQuery = "SELECT * FROM TblRegularOrder WHERE orderNumber = ?";
        String urgentOrderCheckQuery = "SELECT * FROM TblUrgentOrder WHERE orderNumber = ?";
        String wineQuantityQuery = "SELECT wineID, quantity FROM TblUrgentOrderWineQuantity WHERE orderNumber = ?"; // השאילתה להיינות והכמויות

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderNumber = rs.getInt("orderNumber");
                int assignedSaleEmployeeID = rs.getInt("AssignedSaleEmployeeID"); // קבלת המזהה של העובד

                // בדוק אם ההזמנה קיימת בטבלת הזמנות רגילות
                try (PreparedStatement checkRegularStmt = connection.prepareStatement(regularOrderCheckQuery)) {
                    checkRegularStmt.setInt(1, orderNumber);
                    try (ResultSet regularRs = checkRegularStmt.executeQuery()) {
                        if (regularRs.next()) {
                            // יצירת RegularOrder עם AssignedSaleEmployeeID
                            Order order = new RegularOrder(
                                orderNumber,
                                rs.getDate("orderDate"),
                                OrderStatus.valueOf(rs.getString("status")),
                                rs.getDate("shipmentDate"),
                                assignedSaleEmployeeID,
                                regularRs.getInt("mainCustomerID") // אם יש צורך במידע נוסף
                            );
                            ordersList.add(order);
                        }
                    }
                }

                // בדוק אם ההזמנה קיימת בטבלת הזמנות דחופות
                try (PreparedStatement checkUrgentStmt = connection.prepareStatement(urgentOrderCheckQuery)) {
                    checkUrgentStmt.setInt(1, orderNumber);
                    try (ResultSet urgentRs = checkUrgentStmt.executeQuery()) {
                        if (urgentRs.next()) {
                            // יצירת UrgentOrder עם AssignedSaleEmployeeID
                            // שאילתה להבאת היינות והכמויות עבור הזמנת דחופה
                            try (PreparedStatement wineStmt = connection.prepareStatement(wineQuantityQuery)) {
                                wineStmt.setInt(1, orderNumber);
                                try (ResultSet wineRs = wineStmt.executeQuery()) {
                                    HashMap<Integer, Integer> wineQuantities = new HashMap<>();
                                    while (wineRs.next()) {
                                        int wineID = wineRs.getInt("wineID");
                                        int quantity = wineRs.getInt("quantity");
                                        wineQuantities.put(wineID, quantity); // הוספת היין והכמות ל-HashMap
                                    }

                                    Order order = new UrgentOrder(
                                        orderNumber,
                                        rs.getDate("orderDate"),
                                        OrderStatus.valueOf(rs.getString("status")),
                                        rs.getDate("shipmentDate"),
                                        assignedSaleEmployeeID,
                                        wineQuantities // העברת ה-HashMap לאובייקט UrgentOrder
                                    );
                                    ordersList.add(order);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }

*/
    public void createNewOrder(Order order) throws SQLException {
    String checkSql = "SELECT COUNT(*) FROM TblOrder WHERE orderNumber = ?";
    String insertSql = "INSERT INTO TblOrder (orderNumber, orderDate, status, shipmentDate, AssignedSaleEmployeeID) VALUES (?, ?, ?, ?, ?)";
    String insertRegularOrderSql = "INSERT INTO TblRegularOrder (orderNumber, mainCustomerID) VALUES (?, ?)";
    String insertUrgentOrderSql = "INSERT INTO TblUrgentOrder (orderNumber) VALUES (?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, order.getOrderNumber());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                        stmt.setInt(1, order.getOrderNumber());
                        stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
                        stmt.setString(3, order.getStatus().toString());
                        stmt.setDate(4, new java.sql.Date(order.getShipmentDate().getTime()));
                        stmt.setInt(5, order.getAssignedSaleEmployeeID()); // הוספת ה-AssignedSaleEmployeeID
                        stmt.executeUpdate();
                    }

                    // אם ההזמנה היא RegularOrder
                    if (order instanceof RegularOrder) {
                        try (PreparedStatement regularStmt = conn.prepareStatement(insertRegularOrderSql)) {
                            regularStmt.setInt(1, order.getOrderNumber());
                            regularStmt.setInt(2, ((RegularOrder) order).getMainCustomerID());
                            regularStmt.executeUpdate();
                        }
                    }
                    // אם ההזמנה היא UrgentOrder
                    else if (order instanceof UrgentOrder) {
                        try (PreparedStatement urgentStmt = conn.prepareStatement(insertUrgentOrderSql)) {
                            urgentStmt.setInt(1, order.getOrderNumber());
                            urgentStmt.executeUpdate();
                        }
                    }
                } else {
                    System.out.println("Order with orderNumber " + order.getOrderNumber() + " already exists.");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static void updateOrder(Order order) throws SQLException {
    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null.");
    }

    try (Connection connection = DatabaseConnection.getConnection()) {
        String sql = "UPDATE TblOrder SET orderDate = ?, status = ?, shipmentDate = ?, AssignedSaleEmployeeID = ? WHERE orderNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setString(2, order.getStatus().toString());
            stmt.setDate(3, new java.sql.Date(order.getShipmentDate().getTime()));
            stmt.setInt(4, order.getAssignedSaleEmployeeID()); // עדכון AssignedSaleEmployeeID
            stmt.setInt(5, order.getOrderNumber());

            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void deleteOrder(int orderNumber) {
        String query = "DELETE FROM TblOrder WHERE orderNumber = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, orderNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No order found with orderNumber " + orderNumber);
            } else {
                System.out.println("Order with orderNumber " + orderNumber + " was deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public ArrayList<UrgentOrder> getAllUrgentOrders() {
        ArrayList<UrgentOrder> urgentOrders = new ArrayList<>();
        String query = "SELECT o.orderNumber, o.orderDate, o.status, o.shipmentDate, o.AssignedSaleEmployeeID, " +
                       "u.priorityLevel, u.expectedDeliveryTime, u.customerID " +
                       "FROM TblOrder o " +
                       "JOIN TblUrgentOrder u ON o.orderNumber = u.orderNumber";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int orderNumber = rs.getInt("orderNumber");
                Date orderDate = rs.getDate("orderDate");
                String statusString = rs.getString("status");

                OrderStatus status;
                try {
                    status = OrderStatus.valueOf(statusString.replace(" ", "_"));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid OrderStatus value: " + statusString);
                    status = null;
                }

                Date shipmentDate = rs.getDate("shipmentDate");
                int assignedSaleEmployeeID = rs.getInt("AssignedSaleEmployeeID");

                PriorityLevel priorityLevel = PriorityLevel.fromInt(rs.getInt("priorityLevel"));
                int expectedDeliveryTime = rs.getInt("expectedDeliveryTime");
                int customerID = rs.getInt("customerID");

                // הדפסת ה-`customerID` על מנת לוודא שהוא נכון
                System.out.println("Fetched Customer ID for order " + orderNumber + ": " + customerID);

                HashMap<Wine, Integer> wineQuantities = getWinesInUrgentOrder(orderNumber);

                UrgentOrder urgentOrder = new UrgentOrder(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID,
                                                          priorityLevel, expectedDeliveryTime, customerID, wineQuantities);
                urgentOrders.add(urgentOrder);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching urgent orders: " + e.getMessage());
        }

        return urgentOrders;
    }


    
    public HashMap<Wine, Integer> getWinesInUrgentOrder(int orderNumber) {
        HashMap<Wine, Integer> wineQuantities = new HashMap<>();
        String query = "SELECT CatalogNumber, Quantity FROM TblWinesInUrgentOrder WHERE OrderNumber = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, orderNumber);  // הצבת ה-OrderNumber בשאילתא
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int catalogNumber = rs.getInt("CatalogNumber");
                    int quantity = rs.getInt("Quantity");
                    
                    // חיפוש אובייקט היין לפי ה-CatalogNumber
                    Wine wine = WineManagment.getInstance().searchWineByCatalogNumber(catalogNumber);
                    wineQuantities.put(wine, quantity);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving wines for urgent order: " + e.getMessage());
        }

        return wineQuantities;  // החזרת ה-HashMap המלא
    }

    

}
