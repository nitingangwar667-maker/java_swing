package course.reg;

public class CoreCourse extends Course {
    private String mandatoryDepartment;

    public CoreCourse(String courseCode, String courseName, int maxSeats, double fees, String scheduleTime, String mandatoryDepartment) {
        super(courseCode, courseName, maxSeats, fees, scheduleTime);
        this.mandatoryDepartment = mandatoryDepartment;
    }

    @Override
    public String getCourseType() {
        return "Core";
    }

    public String getMandatoryDepartment() { return mandatoryDepartment; }
    public void setMandatoryDepartment(String mandatoryDepartment) { this.mandatoryDepartment = mandatoryDepartment; }
}
