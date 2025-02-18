package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL;
    
    static {
        String path = "cheersDB.accdb";  // נתיב קובץ ה-ACCESS במערכת
        try {
            File localFile = new File(path);
            if (!localFile.exists()) {
                // אם הקובץ לא נמצא במערכת המקומית, נעתיק אותו מה-JAR
                InputStream resourceStream = DatabaseConnection.class.getResourceAsStream("/" + path);
                if (resourceStream != null) {
                    Files.copy(resourceStream, localFile.toPath());
                    System.out.println("הקובץ הועתק בהצלחה למערכת המקומית.");
                } else {
                    throw new RuntimeException("לא נמצא קובץ ה-ACCESS בתוך ה-JAR.");
                }
            } else {
                System.out.println("הקובץ כבר קיים במערכת המקומית.");
            }
            DATABASE_URL = "jdbc:ucanaccess://" + localFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("לא ניתן לגשת לקובץ ה-ACCESS", e);
        }
    }
    
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DATABASE_URL);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // טיפול בשגיאות סגירה
            }
        }
    }

    // פונקציה לבצע חיבור במסד הנתונים לבדיקה
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("החיבור למסד הנתונים הצליח!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

