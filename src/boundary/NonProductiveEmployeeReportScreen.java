package boundary;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import control.PersonManagement;
import control.ReportsExport;
import entity.Employee;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.*;

import java.beans.PropertyChangeListener;



public class NonProductiveEmployeeReportScreen extends JInternalFrame {

    private JDateChooser startDateChooser;  // עליך להשתמש ב-JDateChooser (מתוך JCalendar)
    private JDateChooser endDateChooser;    // JDateChooser נוסף לתאריך סיום
    private JButton generateReportButton;
    private JTable reportTable;
    private JPanel panel;

    public NonProductiveEmployeeReportScreen() {
        setTitle("Unproductive Employee Report");
        setSize(780, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setClosable(true);  // מאפשר סגירה
        setIconifiable(true);  // מאפשר הקטנה
        setMaximizable(true);  // מאפשר הגדלה
        setResizable(true);  // מאפשר שינוי גודל

        
        // יצירת ממשק משתמש
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.WHITE);

        JLabel startDateLabel = new JLabel("Pick Start Date");
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("dd/MM/yyyy");
        startDateChooser.setBackground(Color.WHITE);
        startDateChooser.setFont(new Font("Arial", Font.PLAIN, 12));
        startDateChooser.setForeground(Color.decode("#6f2936"));
        
     // קביעת מגבלה לתאריך התחלה (לא תאריך עתידי)
        startDateChooser.setMaxSelectableDate(new Date());  // הגדרת התאריך המרבי שניתן לבחור (היום)

        
     // גישה לשינוי צבעי רכיבי הלוח שנה
        JCalendar calendar = startDateChooser.getJCalendar();
        calendar.getDayChooser().setForeground(Color.WHITE);  // צבע טקסט של הימים
        
        JLabel endDateLabel = new JLabel("Pick End Date");
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("dd/MM/yyyy");
        endDateChooser.setBackground(Color.WHITE);
        endDateChooser.setFont(new Font("Arial", Font.PLAIN, 12));
        endDateChooser.setForeground(Color.decode("#6f2936"));

     // קביעת מגבלה לתאריך סיום (לא תאריך עתידי)
        endDateChooser.setMaxSelectableDate(new Date());  // הגדרת התאריך המרבי שניתן לבחור (היום)

        JCalendar calendarEnd = endDateChooser.getJCalendar();
        calendarEnd.getDayChooser().setForeground(Color.WHITE);  // צבע טקסט של הימים
       
     // הגדרת Listener לתאריך התחלה
        startDateChooser.getDateEditor().getUiComponent().addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date startDate = startDateChooser.getDate();
                if (startDate != null) {
                    // הגדרת התאריך הסופי המינימלי
                    endDateChooser.setMinSelectableDate(startDate);
                }
            }
        });

        // הגדרת Listener לתאריך סיום
        endDateChooser.getDateEditor().getUiComponent().addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date endDate = endDateChooser.getDate();
                Date startDate = startDateChooser.getDate();
                if (startDate != null && endDate != null && endDate.before(startDate)) {
                    // אם התאריך סיום לפני התחלה, מחזירים את התאריך הסיום לתאריך התחלה
                    endDateChooser.setDate(startDate);
                }
            }
        });

        
        
        generateReportButton = new JButton("Generate Report");

        panel.add(startDateLabel);
        panel.add(startDateChooser);
        panel.add(endDateLabel);
        panel.add(endDateChooser);
        panel.add(generateReportButton);

        // JTable להצגת הדו"ח
        reportTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);
        reportTable.setPreferredScrollableViewportSize(new Dimension(750, 400));  // הגדל את הגובה

        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        // הוספת פעולה לכפתור
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();
                
                if (startDate != null && endDate != null) {
                    // קריאה למתודת הפילטור ב-ReportExport
                    ArrayList<Employee> nonProductiveEmployees = ReportsExport.getInstance().getNonProductiveEmployees(startDate, endDate);

                    // הצגת התוצאות בטבלה
                    displayReport(nonProductiveEmployees, startDate, endDate);
                } else {
                    JOptionPane.showMessageDialog(null, "יש לבחור תאריכים");
                }
            }
        });
    }

    private void displayReport(ArrayList<Employee> nonProductiveEmployees, Date startDate, Date endDate) {
        // הכנה להצגת הדו"ח בטבלה עם העמודות החדשות
        String[] columns = {"ID", "שם", "תאריך התחלה", "כתובת משרד", "סוג עובד", "מספר הזמנות רגילות", "מספר הזמנות דחופות"};
        Object[][] data = new Object[nonProductiveEmployees.size()][7];  // 7 עמודות

        for (int i = 0; i < nonProductiveEmployees.size(); i++) {
            Employee employee = nonProductiveEmployees.get(i);
            data[i][0] = employee.getID();  // הוספת ID של העובד
            data[i][1] = employee.getName();  // שם העובד
            data[i][2] = new SimpleDateFormat("dd/MM/yyyy").format(employee.getEmploymentStartDate()); // תאריך התחלה
            data[i][3] = employee.getOfficeAddress(); // כתובת משרד
            data[i][4] = employee.getType().toString(); // סוג עובד
            data[i][5] = PersonManagement.getInstance().countRegularOrders(employee, startDate, endDate);
            data[i][6] = PersonManagement.getInstance().countUrgentOrders(employee, startDate, endDate);
        }

        reportTable.setModel(new DefaultTableModel(data, columns));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NonProductiveEmployeeReportScreen().setVisible(true);
            }
        });
    }
}
