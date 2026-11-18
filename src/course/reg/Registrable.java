package course.reg;

public interface Registrable {
    void registerStudent(String rollNo) throws CourseFullException;
    boolean dropStudent(String rollNo);
    int getAvailableSeats();
}
