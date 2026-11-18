package com.college.model;

public class Application {
    private int applicationId;
    private String fullName;
    private String email;
    private String password;
    private String courseCode;
    private String status;

    public Application() {}

    public Application(String fullName, String email, String password, String courseCode) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.courseCode = courseCode;
        this.status = "Pending";
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
