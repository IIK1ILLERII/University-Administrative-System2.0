import model.DatabaseConnection;
import java.sql.*;

public class SimpleQueries {

    public static void main(String[] args) {
        showAllData();
    }

    public static void showAllData() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            System.out.println("=== ESTUDIANTES ===");
            String studentsSQL = "SELECT student_id, first_name, last_name, email, career, semester, gpa FROM students ORDER BY student_id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(studentsSQL)) {
                while (rs.next()) {
                    System.out.printf("ID: %d | %s %s | %s | %s | Sem: %d | GPA: %.2f%n",
                            rs.getInt("student_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("career"),
                            rs.getInt("semester"),
                            rs.getDouble("gpa"));
                }
            }

            System.out.println("\n=== PROFESORES ===");
            String professorsSQL = "SELECT professor_id, first_name, last_name, email, department, specialty FROM professors ORDER BY professor_id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(professorsSQL)) {
                while (rs.next()) {
                    System.out.printf("ID: %d | %s %s | %s | %s | %s%n",
                            rs.getInt("professor_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("department"),
                            rs.getString("specialty"));
                }
            }

            System.out.println("\n=== MATERIAS ===");
            String subjectsSQL = "SELECT subject_id, code, subject_name, credits, department FROM subjects ORDER BY subject_id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(subjectsSQL)) {
                while (rs.next()) {
                    System.out.printf("ID: %d | %s - %s | %d creditos | %s%n",
                            rs.getInt("subject_id"),
                            rs.getString("code"),
                            rs.getString("subject_name"),
                            rs.getInt("credits"),
                            rs.getString("department"));
                }
            }

            System.out.println("\n=== INSCRIPCIONES ===");
            String enrollmentsSQL = "SELECT enrollment_id, student_id, subject_id, grade, status, semester FROM student_subjects ORDER BY enrollment_id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(enrollmentsSQL)) {
                while (rs.next()) {
                    String grade = rs.getDouble("grade") > 0 ? String.format("%.2f", rs.getDouble("grade")) : "N/A";
                    System.out.printf("ID: %d | Est: %d | Mat: %d | Nota: %s | %s | Sem: %s%n",
                            rs.getInt("enrollment_id"),
                            rs.getInt("student_id"),
                            rs.getInt("subject_id"),
                            grade,
                            rs.getString("status"),
                            rs.getString("semester"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}