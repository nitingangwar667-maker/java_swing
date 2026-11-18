package course.reg;

import java.io.Serializable;

public abstract class Course implements Registrable, Serializable {
    protected String courseCode;
    protected String courseName;
    protected int maxSeats;
    protected int enrolledCount;
    protected double fees;
    protected String scheduleTime;

    public Course(String courseCode, String courseName, int maxSeats, double fees, String scheduleTime) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxSeats = maxSeats;
        this.enrolledCount = 0;
        this.fees = fees;
        this.scheduleTime = scheduleTime;
    }

    public abstract String getCourseType();

    @Override
    public synchronized void registerStudent(String rollNo) throws CourseFullException {
        if (enrolledCount >= maxSeats) {
            throw new CourseFullException("Course " + courseCode + " (" + courseName + ") is full! Maximum seats (" + maxSeats + ") reached.");
        }
        enrolledCount++;
    }

    @Override
    public synchronized boolean dropStudent(String rollNo) {
        if (enrolledCount > 0) {
            enrolledCount--;
            return true;
        }
        return false;
    }

    @Override
    public int getAvailableSeats() {
        return maxSeats - enrolledCount;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getMaxSeats() { return maxSeats; }
    public void setMaxSeats(int maxSeats) { this.maxSeats = maxSeats; }

    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }

    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }

    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " [" + getCourseType() + "] (" + getAvailableSeats() + "/" + maxSeats + " seats available)";
    }
}
