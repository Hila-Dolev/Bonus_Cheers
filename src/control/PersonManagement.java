package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import entity.Person;
import entity.Employee;
import entity.EmployeeType;
import entity.Customer;

public class PersonManagement {
    private static PersonManagement instance;
    
    // מנחה פרטי כדי למנוע יצירת מופע חדש
    private PersonManagement() {
    }

    // מתודת getInstance שמחזירה את האינסטנס היחיד של PersonManagement
    public static PersonManagement getInstance() {
        if (instance == null) {
            synchronized (PersonManagement.class) {
                if (instance == null) {
                    instance = new PersonManagement();
                }
            }
        }
        return instance;
    }

    public Employee getEmployeeDetailsById(int id) {
        String personQuery = "SELECT Name, PhoneNumber, Email FROM TblPerson WHERE ID = ?";
        String employeeQuery = "SELECT officeAddress, employmentStartDate, type FROM TblEmployee WHERE ID = ?";

        String name = null, email = null;
        int phoneNumber;
        String officeAddress = null;
        Date employmentStartDate = null;
        EmployeeType type = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement personStmt = connection.prepareStatement(personQuery)) {

            personStmt.setInt(1, id);
            try (ResultSet personRs = personStmt.executeQuery()) {
                if (personRs.next()) {
                    name = personRs.getString("Name");
                    phoneNumber = personRs.getInt("PhoneNumber");
                    email = personRs.getString("Email");
                } else {
                    return null; // אם אין נתונים בטבלת person, מחזירים null
                }
            }

            try (PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)) {
                employeeStmt.setInt(1, id);
                try (ResultSet employeeRs = employeeStmt.executeQuery()) {
                    if (employeeRs.next()) {
                        officeAddress = employeeRs.getString("officeAddress");
                        employmentStartDate = employeeRs.getDate("employmentStartDate");

                        // המרת `type` למשתנה מסוג Enum
                        String typeStr = employeeRs.getString("type");
                        if (typeStr != null) {
                            try {
                                type = EmployeeType.valueOf(typeStr.toUpperCase()); // מתאים לערכים שמוגדרים ב-Enum
                            } catch (IllegalArgumentException e) {
                                System.out.println("Unknown employee type: " + typeStr);
                                return null;
                            }
                        }
                        // יוצרים מופע של Employee ומחזירים
                        return new Employee(id, name, phoneNumber, email, employmentStartDate, officeAddress, type);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // אם לא נמצא עובד, מחזירים null
    }

    public Customer getCustomerDetailsById(int id) {
        String personQuery = "SELECT Name, PhoneNumber, Email FROM TblPerson WHERE ID = ?";
        String customerQuery = "SELECT delivaryAddress, dateOfFirstContact FROM TblCustomer WHERE ID = ?";

        String name = null, email = null;
        int phoneNumber;
        String delivaryAddress = null;
        Date dateOfFirstContact = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement personStmt = connection.prepareStatement(personQuery)) {

            personStmt.setInt(1, id);
            try (ResultSet personRs = personStmt.executeQuery()) {
                if (personRs.next()) {
                    name = personRs.getString("Name");
                    phoneNumber = personRs.getInt("PhoneNumber");
                    email = personRs.getString("Email");
                } else {
                    return null; // אם אין נתונים בטבלת person, מחזירים null
                }
            }

            try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {
                customerStmt.setInt(1, id);
                try (ResultSet customerRs = customerStmt.executeQuery()) {
                    if (customerRs.next()) {
                        delivaryAddress = customerRs.getString("delivaryAddress");
                        dateOfFirstContact = customerRs.getDate("dateOfFirstContact");

                        // יוצרים מופע של Customer ומחזירים
                        return new Customer(id, name, phoneNumber, email, delivaryAddress, dateOfFirstContact);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // אם לא נמצא לקוח, מחזירים null
    }

    public String getUserTypeById(int id) {
        String employeeQuery = "SELECT COUNT(*) FROM TblEmployee WHERE ID = ?";
        String customerQuery = "SELECT COUNT(*) FROM TblCustomer WHERE ID = ?";

        boolean isEmployee = false, isCustomer = false;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery);
             PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {

            employeeStmt.setInt(1, id);
            try (ResultSet employeeRs = employeeStmt.executeQuery()) {
                if (employeeRs.next() && employeeRs.getInt(1) > 0) {
                    isEmployee = true;
                }
            }

            customerStmt.setInt(1, id);
            try (ResultSet customerRs = customerStmt.executeQuery()) {
                if (customerRs.next() && customerRs.getInt(1) > 0) {
                    isCustomer = true;
                }
            }

            if (isEmployee && isCustomer) {
                return "Both"; // המשתמש הוא גם עובד וגם לקוח
            } else if (isEmployee) {
                return "Employee"; // המשתמש הוא עובד
            } else if (isCustomer) {
                return "Customer"; // המשתמש הוא לקוח
            } else {
                return "None"; // המשתמש לא נמצא
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error"; // במקרה של תקלה
    }
    
    public ArrayList<Employee> getAllEmployees() {
        String employeeQuery = "SELECT ID, Name, PhoneNumber, Email, officeAddress, employmentStartDate, type FROM TblEmployee JOIN TblPerson ON TblEmployee.ID = TblPerson.ID";
        ArrayList<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery);
             ResultSet employeeRs = employeeStmt.executeQuery()) {

            while (employeeRs.next()) {
                int id = employeeRs.getInt("ID");
                String name = employeeRs.getString("Name");
                int phoneNumber = employeeRs.getInt("PhoneNumber");
                String email = employeeRs.getString("Email");
                String officeAddress = employeeRs.getString("officeAddress");
                Date employmentStartDate = employeeRs.getDate("employmentStartDate");
                String typeStr = employeeRs.getString("type");
                EmployeeType type = null;

                if (typeStr != null) {
                    try {
                        type = EmployeeType.valueOf(typeStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Unknown employee type: " + typeStr);
                    }
                }

                Employee employee = new Employee(id, name, phoneNumber, email, employmentStartDate, officeAddress, type);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees; // מחזירים את רשימת העובדים
    }


    
    public ArrayList<Employee> getMarketingEmployees() {
        String employeeQuery = "SELECT ID, Name, PhoneNumber, Email, officeAddress, employmentStartDate, type FROM TblEmployee JOIN TblPerson ON TblEmployee.ID = TblPerson.ID WHERE type = ?";
        ArrayList<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)) {

            employeeStmt.setString(1, EmployeeType.MARKETING.name().toLowerCase()); // מגדירים את סוג העובד לשיווק
            try (ResultSet employeeRs = employeeStmt.executeQuery()) {
                while (employeeRs.next()) {
                    int id = employeeRs.getInt("ID");
                    String name = employeeRs.getString("Name");
                    int phoneNumber = employeeRs.getInt("PhoneNumber");
                    String email = employeeRs.getString("Email");
                    String officeAddress = employeeRs.getString("officeAddress");
                    Date employmentStartDate = employeeRs.getDate("employmentStartDate");
                    String typeStr = employeeRs.getString("type");
                    EmployeeType type = null;

                    if (typeStr != null) {
                        try {
                            type = EmployeeType.valueOf(typeStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Unknown employee type: " + typeStr);
                        }
                    }

                    Employee employee = new Employee(id, name, phoneNumber, email, employmentStartDate, officeAddress, type);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees; // מחזירים את רשימת עובדי השיווק
    }

    
    public ArrayList<Employee> getSalesEmployees() {
        String employeeQuery = "SELECT ID, Name, PhoneNumber, Email, officeAddress, employmentStartDate, type FROM TblEmployee JOIN TblPerson ON TblEmployee.ID = TblPerson.ID WHERE type = ?";
        ArrayList<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)) {

            employeeStmt.setString(1, EmployeeType.SALES.name().toLowerCase()); // מגדירים את סוג העובד למכירות
            try (ResultSet employeeRs = employeeStmt.executeQuery()) {
                while (employeeRs.next()) {
                    int id = employeeRs.getInt("ID");
                    String name = employeeRs.getString("Name");
                    int phoneNumber = employeeRs.getInt("PhoneNumber");
                    String email = employeeRs.getString("Email");
                    String officeAddress = employeeRs.getString("officeAddress");
                    Date employmentStartDate = employeeRs.getDate("employmentStartDate");
                    String typeStr = employeeRs.getString("type");
                    EmployeeType type = null;

                    if (typeStr != null) {
                        try {
                            type = EmployeeType.valueOf(typeStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Unknown employee type: " + typeStr);
                        }
                    }

                    Employee employee = new Employee(id, name, phoneNumber, email, employmentStartDate, officeAddress, type);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees; // מחזירים את רשימת עובדי המכירות
    }

    public int countUrgentOrders(Employee employee, Date startDate, Date endDate) {
        String query = "SELECT COUNT(IIF(TblOrder.orderDate BETWEEN ? AND ? AND TblUrgentOrder.orderNumber IS NOT NULL, 1, NULL)) AS UrgentOrders " +
                       "FROM TblUrgentOrder RIGHT JOIN (SalesEmployees INNER JOIN (TblRegularOrder RIGHT JOIN TblOrder ON TblRegularOrder.orderNumber = TblOrder.orderNumber) " +
                       "ON SalesEmployees.ID = TblOrder.AssignedSaleEmployeeID) ON TblUrgentOrder.orderNumber = TblOrder.orderNumber " +
                       "WHERE SalesEmployees.ID = ?";

        int urgentOrders = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(startDate.getTime()));  // StartDate
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));    // EndDate
            stmt.setInt(3, employee.getID());  // Employee ID מתוך האובייקט

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    urgentOrders = rs.getInt("UrgentOrders");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urgentOrders; // מחזירים את מספר ההזמנות הדחופות
    }
    
    public int countRegularOrders(Employee employee, Date startDate, Date endDate) {
        String query = "SELECT COUNT(IIF(TblOrder.orderDate BETWEEN ? AND ? AND TblRegularOrder.orderNumber IS NOT NULL, 1, NULL)) AS RegularOrders " +
                       "FROM TblUrgentOrder RIGHT JOIN (SalesEmployees INNER JOIN (TblRegularOrder RIGHT JOIN TblOrder ON TblRegularOrder.orderNumber = TblOrder.orderNumber) " +
                       "ON SalesEmployees.ID = TblOrder.AssignedSaleEmployeeID) ON TblUrgentOrder.orderNumber = TblOrder.orderNumber " +
                       "WHERE SalesEmployees.ID = ?";

        int regularOrders = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(startDate.getTime()));  // StartDate
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));    // EndDate
            stmt.setInt(3, employee.getID());  // Employee ID מתוך האובייקט

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    regularOrders = rs.getInt("RegularOrders");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return regularOrders; // מחזירים את מספר ההזמנות הרגילות
    }

    

}
