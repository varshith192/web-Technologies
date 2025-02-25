import java.sql.*;
import java.util.Scanner;

public class CollegeManagementSystem {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/collegeDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sai";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connected to PostgreSQL database successfully..");
            createTable(conn);
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Course");
                System.out.println("2. Update Course");
                System.out.println("3. Delete Course");
                System.out.println("4. Display Courses");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        System.out.print("Enter Course Name: ");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        System.out.print("Enter Credits: ");
                        int credits = scanner.nextInt();
                        insertCourse(conn, name, credits);
                        break;
                    case 2:
                        System.out.print("Enter Course ID to update: ");
                        int updateId = scanner.nextInt();
                        System.out.print("Enter new Credits: ");
                        int newCredits = scanner.nextInt();
                        updateCourse(conn, updateId, newCredits);
                        break;
                    case 3:
                        System.out.print("Enter Course ID to delete: ");
                        int deleteId = scanner.nextInt();
                        deleteCourse(conn, deleteId);
                        break;
                    case 4:
                        displayCourses(conn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (CourseID SERIAL PRIMARY KEY, Name VARCHAR(255), Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' is ready.");
        }
    }

    private static void insertCourse(Connection conn, String name, int credits) throws SQLException {
        String sql = "INSERT INTO Courses (Name, Credits) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, int id, int newCredits) throws SQLException {
        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newCredits);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    private static void deleteCourse(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }
    
    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nCourses Table:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("CourseID") + ", Name: " + rs.getString("Name") + ", Credits: " + rs.getInt("Credits"));
            }
        }
    }
}
