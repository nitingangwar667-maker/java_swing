package course.reg;

import java.io.Serializable;
import java.util.Date;

public class Enrolment implements Serializable {
    private int enrolmentId;
    private String rollNo;
    private String courseCode;
    private Date enrolmentDate;
    private String status; // 'Enrolled', 'Dropped', 'Completed'

    public Enrolment(String rollNo, String courseCode) {
        this.rollNo = rollNo;
        this.courseCode = courseCode;
        this.enrolmentDate = new Date();
        this.status = "Enrolled";
    }

    public Enrolment(int enrolmentId, String rollNo, String courseCode, Date enrolmentDate, String status) {
        this.enrolmentId = enrolmentId;
        this.rollNo = rollNo;
        this.courseCode = courseCode;
        this.enrolmentDate = enrolmentDate;
        this.status = status;
    }

    public int getEnrolmentId() { return enrolmentId; }
    public void setEnrolmentId(int enrolmentId) { this.enrolmentId = enrolmentId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public Date getEnrolmentDate() { return enrolmentDate; }
    public void setEnrolmentDate(Date enrolmentDate) { this.enrolmentDate = enrolmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
