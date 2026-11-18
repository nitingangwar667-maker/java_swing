package com.college.model;

public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;
    private String duration;
    private double fees;
    private int maxSeats;
    private int enrolledCount;
    private String courseType;
    private String scheduleTime;

    public Course() {}

    public Course(String courseCode, String courseName, String duration, double fees) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.duration = duration;
        this.fees = fees;
        this.maxSeats = 50;
        this.enrolledCount = 0;
        this.courseType = "Core";
        this.scheduleTime = "Mon/Wed 10:00 AM";
    }

    public Course(String courseCode, String courseName, String duration, double fees, int maxSeats, int enrolledCount, String courseType, String scheduleTime) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.duration = duration;
        this.fees = fees;
        this.maxSeats = maxSeats;
        this.enrolledCount = enrolledCount;
        this.courseType = courseType;
        this.scheduleTime = scheduleTime;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }

    public int getMaxSeats() { return maxSeats; }
    public void setMaxSeats(int maxSeats) { this.maxSeats = maxSeats; }

    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }

    public int getAvailableSeats() { return maxSeats - enrolledCount; }

    public String getCourseType() { return courseType; }
    public void setCourseType(String courseType) { this.courseType = courseType; }

    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " [" + (courseType != null ? courseType : "Core") + "]";
    }
}
