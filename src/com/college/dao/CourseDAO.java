package com.college.dao;

import com.college.model.Course;
import com.college.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public boolean addCourse(Course course) {
        String query = "INSERT INTO courses (course_code, course_name, duration, fees, max_seats, course_type, schedule_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDuration());
            pstmt.setDouble(4, course.getFees());
            pstmt.setInt(5, course.getMaxSeats() > 0 ? course.getMaxSeats() : 50);
            pstmt.setString(6, course.getCourseType() != null ? course.getCourseType() : "Core");
            pstmt.setString(7, course.getScheduleTime() != null ? course.getScheduleTime() : "Mon/Wed 10:00 AM");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Course getCourseByCode(String courseCode) {
        String query = "SELECT * FROM courses WHERE course_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, courseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
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
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
