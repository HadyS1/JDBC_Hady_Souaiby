package org.example;

import java.sql.*;

public class App {
    private static final String URL = "jdbc:postgresql://localhost:5432/jdbc_hady";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static void main(String[] args) {
        System.out.println("=== JDBC PostgreSQL Demo ===");
        System.out.println("User proof: Hady Souaiby – JDBC PostgreSQL Assignment – 2026-01-31");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to PostgreSQL!");
            System.out.println("DB Product: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("DB Version: " + conn.getMetaData().getDatabaseProductVersion());

            // 1) INSERT (PreparedStatement)
            int newId = insertStudent(conn, "Student One", "student1@mail.com");
            System.out.println("Inserted student id = " + newId);

            // 2) SELECT
            System.out.println("\nAll students:");
            selectAllStudents(conn);

            // 3) UPDATE
            int updated = updateStudentName(conn, newId, "Student One Updated");
            System.out.println("\nUpdated rows = " + updated);

            // 4) DELETE (optionnel)
            //int deleted = deleteStudent(conn, newId);
            //System.out.println("Deleted rows = " + deleted);

        } catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
    }

    private static int insertStudent(Connection conn, String name, String email) throws SQLException {
        String sql = "INSERT INTO students(name, email) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    private static void selectAllStudents(Connection conn) throws SQLException {
        String sql = "SELECT id, name, email FROM students ORDER BY id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("id=%d | name=%s | email=%s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        }
    }

    private static int updateStudentName(Connection conn, int id, String newName) throws SQLException {
        String sql = "UPDATE students SET name=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    private static int deleteStudent(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}