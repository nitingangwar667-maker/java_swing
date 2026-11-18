package course.reg;

public class ElectiveCourse extends Course {
    private String domain;

    public ElectiveCourse(String courseCode, String courseName, int maxSeats, double fees, String scheduleTime, String domain) {
        super(courseCode, courseName, maxSeats, fees, scheduleTime);
        this.domain = domain;
    }

    @Override
    public String getCourseType() {
        return "Elective";
    }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
