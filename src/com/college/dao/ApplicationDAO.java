package com.college.dao;

import com.college.model.Application;
import com.college.model.Student;
import com.college.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public boolean submitApplication(Application app) {
        String query = "INSERT INTO applications (full_name, email, password, course_code, status) VALUES (?, ?, ?, ?, 'Pending')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, app.getFullName());
            pstmt.setString(2, app.getEmail());
            pstmt.setString(3, app.getPassword());
            pstmt.setString(4, app.getCourseCode());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Application authenticateApplicant(String email, String password) {
        String query = "SELECT * FROM applications WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Application app = new Application();
                    app.setApplicationId(rs.getInt("application_id"));
                    app.setFullName(rs.getString("full_name"));
                    app.setEmail(rs.getString("email"));
                    app.setPassword(rs.getString("password"));
                    app.setCourseCode(rs.getString("course_code"));
                    app.setStatus(rs.getString("status"));
                    return app;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Application> getAllApplications() {
        List<Application> list = new ArrayList<>();
        String query = "SELECT * FROM applications";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Application app = new Application();
                app.setApplicationId(rs.getInt("application_id"));
                app.setFullName(rs.getString("full_name"));
                app.setEmail(rs.getString("email"));
                app.setPassword(rs.getString("password"));
                app.setCourseCode(rs.getString("course_code"));
                app.setStatus(rs.getString("status"));
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Application getApplicationById(int appId) {
        String query = "SELECT * FROM applications WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, appId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Application app = new Application();
                    app.setApplicationId(rs.getInt("application_id"));
                    app.setFullName(rs.getString("full_name"));
                    app.setEmail(rs.getString("email"));
                    app.setPassword(rs.getString("password"));
                    app.setCourseCode(rs.getString("course_code"));
                    app.setStatus(rs.getString("status"));
                    return app;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Approve application, allot seat, create official student record and login credentials
    public boolean approveAndAllotSeat(int applicationId, String rollNo) {
        Application app = getApplicationById(applicationId);
        if (app == null || !app.getStatus().equalsIgnoreCase("Pending")) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert into students table
            String studentQuery = "INSERT INTO students (roll_no, full_name, gender, dob, email, phone, course_code, semester, address) VALUES (?, ?, 'Other', '2000-01-01', ?, '0000000000', ?, 1, 'Campus')";
            try (PreparedStatement pstmt1 = conn.prepareStatement(studentQuery)) {
                pstmt1.setString(1, rollNo);
                pstmt1.setString(2, app.getFullName());
                pstmt1.setString(3, app.getEmail());
                pstmt1.setString(4, app.getCourseCode());
                pstmt1.executeUpdate();
            }

            // 2. Fetch course fees and insert fee record
            double totalFees = 0.0;
            String feeQuery = "INSERT INTO fees (roll_no, total_fees, paid_fees, due_fees, status) VALUES (?, ?, 0.0, ?, 'Pending')";
            String courseFeeQuery = "SELECT fees FROM courses WHERE course_code = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(courseFeeQuery)) {
                pstmt2.setString(1, app.getCourseCode());
                try (ResultSet rs = pstmt2.executeQuery()) {
                    if (rs.next()) {
                        totalFees = rs.getDouble("fees");
                    }
                }
            }

            try (PreparedStatement pstmt3 = conn.prepareStatement(feeQuery)) {
                pstmt3.setString(1, rollNo);
                pstmt3.setDouble(2, totalFees);
                pstmt3.setDouble(3, totalFees);
                pstmt3.executeUpdate();
            }

            // 3. Create official student login account in users (Username = FullName or RollNo, Password = RollNo)
            String userQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Student')";
            try (PreparedStatement pstmt4 = conn.prepareStatement(userQuery)) {
                pstmt4.setString(1, app.getFullName());
                pstmt4.setString(2, rollNo);
                pstmt4.executeUpdate();
            } catch (SQLException ignored) {}

            try (PreparedStatement pstmt5 = conn.prepareStatement(userQuery)) {
                pstmt5.setString(1, rollNo);
                pstmt5.setString(2, rollNo);
                pstmt5.executeUpdate();
            } catch (SQLException ignored) {}

            // 4. Update application status to Approved
            String updateAppQuery = "UPDATE applications SET status = 'Approved' WHERE application_id = ?";
            try (PreparedStatement pstmt6 = conn.prepareStatement(updateAppQuery)) {
                pstmt6.setInt(1, applicationId);
                pstmt6.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return false;
    }
}
