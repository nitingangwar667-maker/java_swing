package com.college.model;

public class Student {
    private int studentId;
    private String rollNo;
    private String fullName;
    private String gender;
    private String dob;
    private String email;
    private String phone;
    private String courseCode;
    private int semester;
    private String address;

    public Student() {}

    public Student(String rollNo, String fullName, String gender, String dob, String email, String phone, String courseCode, int semester, String address) {
        this.rollNo = rollNo;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.courseCode = courseCode;
        this.semester = semester;
        this.address = address;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
