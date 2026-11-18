package com.college.dao;

import com.college.model.Course;
import com.college.util.DatabaseConnection;
import course.reg.CourseFullException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrolmentDAO {

    // Enroll student in a course using PreparedStatement and checking seat limits
    public synchronized boolean enrollStudent(String rollNo, String courseCode) throws CourseFullException, SQLException {
        String checkSeatQuery = "SELECT max_seats, enrolled_count FROM courses WHERE course_code = ?";
        String insertEnrolmentQuery = "INSERT INTO enrolments (roll_no, course_code, status) VALUES (?, ?, 'Enrolled')";
        String updateCourseCountQuery = "UPDATE courses SET enrolled_count = enrolled_count + 1 WHERE course_code = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaction start

            int maxSeats = 0;
            int enrolledCount = 0;

            // 1. Check seat limits using PreparedStatement
            try (PreparedStatement pstmt = conn.prepareStatement(checkSeatQuery)) {
                pstmt.setString(1, courseCode);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        maxSeats = rs.getInt("max_seats");
                        enrolledCount = rs.getInt("enrolled_count");
                    } else {
                        conn.rollback();
                        throw new SQLException("Course not found: " + courseCode);
                    }
                }
            }

            if (enrolledCount >= maxSeats) {
                conn.rollback();
                throw new CourseFullException("Course " + courseCode + " is full! Max seats: " + maxSeats + ", Enrolled: " + enrolledCount);
            }

            // 2. Insert enrolment record
            try (PreparedStatement pstmt = conn.prepareStatement(insertEnrolmentQuery)) {
                pstmt.setString(1, rollNo);
                pstmt.setString(2, courseCode);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback();
                if (e.getMessage().contains("Duplicate") || e.getErrorCode() == 1062) {
                    throw new SQLException("You are already enrolled in this course!");
                }
                throw e;
            }

            // 3. Increment course enrolled count
            try (PreparedStatement pstmt = conn.prepareStatement(updateCourseCountQuery)) {
                pstmt.setString(1, courseCode);
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } catch (CourseFullException cfe) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw cfe;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Drop course enrolment
    public synchronized boolean dropCourse(String rollNo, String courseCode) throws SQLException {
        String deleteQuery = "DELETE FROM enrolments WHERE roll_no = ? AND course_code = ?";
        String updateCourseCountQuery = "UPDATE courses SET enrolled_count = GREATEST(0, enrolled_count - 1) WHERE course_code = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setString(1, rollNo);
                pstmt.setString(2, courseCode);
                int rows = pstmt.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(updateCourseCountQuery)) {
                pstmt.setString(1, courseCode);
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Get registered courses for a student
    public List<Course> getRegisteredCourses(String rollNo) {
        List<Course> list = new ArrayList<>();
        String query = "SELECT c.* FROM courses c JOIN enrolments e ON c.course_code = e.course_code WHERE e.roll_no = ? AND e.status = 'Enrolled'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course();
                    c.setCourseId(rs.getInt("course_id"));
                    c.setCourseCode(rs.getString("course_code"));
                    c.setCourseName(rs.getString("course_name"));
                    c.setDuration(rs.getString("duration"));
                    c.setFees(rs.getDouble("fees"));
                    c.setMaxSeats(rs.getInt("max_seats"));
                    c.setEnrolledCount(rs.getInt("enrolled_count"));
                    c.setCourseType(rs.getString("course_type"));
                    c.setScheduleTime(rs.getString("schedule_time"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
