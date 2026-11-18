package com.college.dao;

import com.college.model.Student;
import com.college.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student student) {
        String studentQuery = "INSERT INTO students (roll_no, full_name, gender, dob, email, phone, course_code, semester, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String feeQuery = "INSERT INTO fees (roll_no, total_fees, paid_fees, due_fees, status) VALUES (?, ?, ?, ?, ?)";
        String userQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Student')";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaction start

            // 1. Insert Student
            try (PreparedStatement pstmt1 = conn.prepareStatement(studentQuery)) {
                pstmt1.setString(1, student.getRollNo());
                pstmt1.setString(2, student.getFullName());
                pstmt1.setString(3, student.getGender());
                pstmt1.setString(4, student.getDob());
                pstmt1.setString(5, student.getEmail());
                pstmt1.setString(6, student.getPhone());
                pstmt1.setString(7, student.getCourseCode());
                pstmt1.setInt(8, student.getSemester());
                pstmt1.setString(9, student.getAddress());
                pstmt1.executeUpdate();
            }

            // 2. Fetch course fees
            double totalFees = 0.0;
            String courseFeeQuery = "SELECT fees FROM courses WHERE course_code = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(courseFeeQuery)) {
                pstmt2.setString(1, student.getCourseCode());
                try (ResultSet rs = pstmt2.executeQuery()) {
                    if (rs.next()) {
                        totalFees = rs.getDouble("fees");
                    }
                }
            }

            // 3. Insert initial fee record (0 paid, full due)
            try (PreparedStatement pstmt3 = conn.prepareStatement(feeQuery)) {
                pstmt3.setString(1, student.getRollNo());
                pstmt3.setDouble(2, totalFees);
                pstmt3.setDouble(3, 0.0);
                pstmt3.setDouble(4, totalFees);
                pstmt3.setString(5, "Pending");
                pstmt3.executeUpdate();
            }

            // 4. Automatically create student login accounts upon seat allotment / registration
            // Username = Student Full Name (or Roll No), Password = Roll Number given by admin
            try (PreparedStatement pstmt4 = conn.prepareStatement(userQuery)) {
                pstmt4.setString(1, student.getFullName());
                pstmt4.setString(2, student.getRollNo());
                pstmt4.executeUpdate();
            } catch (SQLException ignored) {}

            try (PreparedStatement pstmt5 = conn.prepareStatement(userQuery)) {
                pstmt5.setString(1, student.getRollNo());
                pstmt5.setString(2, student.getRollNo());
                pstmt5.executeUpdate();
            } catch (SQLException ignored) {}

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setRollNo(rs.getString("roll_no"));
                s.setFullName(rs.getString("full_name"));
                s.setGender(rs.getString("gender"));
                s.setDob(rs.getString("dob"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                s.setCourseCode(rs.getString("course_code"));
                s.setSemester(rs.getInt("semester"));
                s.setAddress(rs.getString("address"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET full_name = ?, gender = ?, dob = ?, email = ?, phone = ?, course_code = ?, semester = ?, address = ? WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getGender());
            pstmt.setString(3, student.getDob());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getCourseCode());
            pstmt.setInt(7, student.getSemester());
            pstmt.setString(8, student.getAddress());
            pstmt.setString(9, student.getRollNo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(String rollNo) {
        String query = "DELETE FROM students WHERE roll_no = ?";
        String userQuery = "DELETE FROM users WHERE username = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Delete student record (cascade deletes fee)
            try (PreparedStatement pstmt1 = conn.prepareStatement(query)) {
                pstmt1.setString(1, rollNo);
                pstmt1.executeUpdate();
            }

            // Delete associated user login account
            try (PreparedStatement pstmt2 = conn.prepareStatement(userQuery)) {
                pstmt2.setString(1, rollNo);
                pstmt2.executeUpdate();
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
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public Student getStudentByRollNo(String rollNo) {
        String query = "SELECT * FROM students WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student();
                    s.setStudentId(rs.getInt("student_id"));
                    s.setRollNo(rs.getString("roll_no"));
                    s.setFullName(rs.getString("full_name"));
                    s.setGender(rs.getString("gender"));
                    s.setDob(rs.getString("dob"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setCourseCode(rs.getString("course_code"));
                    s.setSemester(rs.getInt("semester"));
                    s.setAddress(rs.getString("address"));
                    return s;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
