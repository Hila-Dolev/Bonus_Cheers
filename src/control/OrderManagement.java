package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import entity.Order;
import entity.OrderStatus;
import entity.PriorityLevel;
import entity.RegularOrder;
import entity.UrgentOrder;

public class OrderManagement {
    
    private static OrderManagement instance;
    private ArrayList<Order> ordersList = new ArrayList<Order>();
    
    public ArrayList<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(ArrayList<Order> ordersList) {
        this.ordersList = ordersList;
    }

    public static OrderManagement getInstance() {
        if (instance == null) {
            // אם האובייקט לא קיים, יוצר אותו
            instance = new OrderManagement();
        }
        return instance;
    }

    public static ArrayList<Order> getAllOrders() {
        ArrayList<Order> ordersList = new ArrayList<>();
        String query = "SELECT * FROM TblOrder"; // שם הטבלה עבור ההזמנות

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

        	while (rs.next()) {
                int orderNumber = rs.getInt("orderNumber");
                Date orderDate = rs.getDate("orderDate");

                String statusFromDb = rs.getString("status");
                OrderStatus status = OrderStatus.valueOf(statusFromDb.replace(" ", "_")); // כאן המרה

                Date shipmentDate = rs.getDate("shipmentDate");

                // בודקים אם ההזמנה היא רגילה או דחופה
                RegularOrder regularOrder = getRegularOrder(orderNumber, orderDate, status, shipmentDate);
                if (regularOrder != null) {
                    ordersList.add(regularOrder);
                } else {
                    UrgentOrder urgentOrder = getUrgentOrder(orderNumber, orderDate, status, shipmentDate);
                    if (urgentOrder != null) {
                        ordersList.add(urgentOrder);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    
    private static RegularOrder getRegularOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate) {
        String query = "SELECT * FROM TblRegularOrder WHERE orderNumber = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, orderNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int mainCustomerID = rs.getInt("MainCustomerID");
                // ניצור את ההזמנה הרגילה
                return new RegularOrder(orderNumber, orderDate, status, shipmentDate, mainCustomerID, new ArrayList<>());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static UrgentOrder getUrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate) {
        String query = "SELECT * FROM TblUrgentOrder WHERE orderNumber = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, orderNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	 int priorityLevelInt = rs.getInt("PriorityLevel");
        	    PriorityLevel priorityLevel = PriorityLevel.fromInt(priorityLevelInt);  // המרת int ל-enum
        	    int expectedDeliveryTime = rs.getInt("ExpectedDeliveryTime");
                int customerID = rs.getInt("CustomerID");
                return new UrgentOrder(orderNumber, orderDate, status, shipmentDate, priorityLevel, expectedDeliveryTime, customerID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateOrder(Order order) throws SQLException {
        String updateSql = "UPDATE TblOrder SET orderDate = ?, status = ?, shipmentDate = ? WHERE orderNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setString(2, order.getStatus().toString());
            stmt.setDate(3, new java.sql.Date(order.getShipmentDate().getTime()));
            stmt.setInt(4, order.getOrderNumber());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while updating order: " + e.getMessage());
        }
    }

    public static void createNewOrder(Order order) throws SQLException {
        String insertSql = "INSERT INTO TblOrder (orderNumber, orderDate, status, shipmentDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setInt(1, order.getOrderNumber());
            stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setString(3, order.getStatus().toString());
            stmt.setDate(4, new java.sql.Date(order.getShipmentDate().getTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while inserting order: " + e.getMessage());
        }

        if (order instanceof RegularOrder) {
            createRegularOrder((RegularOrder) order);
        } else if (order instanceof UrgentOrder) {
            createUrgentOrder((UrgentOrder) order);
        }
    }

    private static void createRegularOrder(RegularOrder regularOrder) throws SQLException {
        String insertSql = "INSERT INTO TblRegularOrder (orderNumber, MainCustomerID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setInt(1, regularOrder.getOrderNumber());
            stmt.setInt(2, regularOrder.getMainCustomerID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while inserting regular order: " + e.getMessage());
        }
    }

    private static void createUrgentOrder(UrgentOrder urgentOrder) throws SQLException {
        String insertSql = "INSERT INTO TblUrgentOrder (orderNumber, PriorityLevel, ExpectedDeliveryTime, CustomerID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setInt(1, urgentOrder.getOrderNumber());
            stmt.setInt(2, urgentOrder.getPriorityLevel().getValue());
            stmt.setInt(3, urgentOrder.getExpectedDeliveryTime());
            stmt.setInt(4, urgentOrder.getCustomerID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while inserting urgent order: " + e.getMessage());
        }
    }

    public static ArrayList<RegularOrder> getAllRegularOrders() {
        ArrayList<RegularOrder> regularOrdersList = new ArrayList<>();
        String query = "SELECT * FROM TblRegularOrder"; // שינוי שם הטבלה אם יש צורך

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderNumber = rs.getInt("orderNumber");
                Date orderDate = rs.getDate("orderDate");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
                Date shipmentDate = rs.getDate("shipmentDate");
                int mainCustomerID = rs.getInt("MainCustomerID");

                // יצירת אובייקט RegularOrder והוספה לרשימה
                RegularOrder regularOrder = new RegularOrder(orderNumber, orderDate, status, shipmentDate, mainCustomerID, new ArrayList<>());
                regularOrdersList.add(regularOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return regularOrdersList;
    }


}
