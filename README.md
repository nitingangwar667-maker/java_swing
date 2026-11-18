# Online Course Registration System

A streamlined desktop portal built in **Java** featuring Java Swing/AWT user interfaces, JDBC (MySQL) backend persistence, and robust object-oriented design principles focusing strictly on the 5 core modules:

1. **Course Catalogue**
2. **Student Registration & Login**
3. **Course Enrolment**
4. **Seat Limit Check**
5. **Registered Course List**

---

## 🎯 The 5 Core Modules

1. **Course Catalogue**:
   - Browse available Core and Elective courses with code, name, duration, fees, schedule, max seats, and real-time available seats.

2. **Student Registration & Login**:
   - Secure login portal supporting Admin, Faculty, and Student roles, along with student registration forms.

3. **Course Enrolment**:
   - Interactive Swing form using `GridLayout` and `FlowLayout` with `ActionListener` for students to select and enrol in courses.

4. **Seat Limit Check**:
   - Real-time seat availability monitoring with thread-safe synchronized seat counters (`synchronized` methods) and custom **`CourseFullException`** handling.

5. **Registered Course List**:
   - View enrolled courses and schedule timetables in a dynamic `JTable` with options to drop courses.

---

## 📚 Syllabus Concepts Applied

- **Unit 1**: Classes (`Course`, `Student`, `Enrolment`), String handling, arrays/collections.
- **Unit 2**: Abstract class (`Course` -> `CoreCourse` / `ElectiveCourse`), interface (`Registrable`), package `course.reg`.
- **Unit 3**: Custom exception (`CourseFullException`), synchronized seat counter for thread safety.
- **Unit 4**: JDBC `PreparedStatement` for secure enrolment queries and transaction management.
- **Unit 5**: Java Swing `FlowLayout` and `GridLayout` forms, event handling with `ActionListener`.

---

## 🛠️ Setup & Execution

### 1. Database Configuration
The application connects to MySQL on `localhost:3306` and creates the `college_db` database and its tables from `Database_Setup.sql` when it starts. Make sure your local MySQL service is running.

The default MySQL user is `root`. When the application starts, enter your MySQL password in the terminal prompt; the characters will not appear as you type. Optionally set the Windows user environment variable `MYSQL_USER` if you use a different MySQL user. Run the app from Command Prompt or PowerShell so the password prompt can read from the terminal.

### 2. Compilation & Running
#### Option A: Using `run.bat` (Windows)
Run from Command Prompt or PowerShell:
```cmd
cd /d C:\Users\kapil\Desktop\java_project
run.bat
```

#### Option B: Using Command Line
```bash
javac -d bin -cp "lib/*" src/course/reg/*.java src/com/college/model/*.java src/com/college/util/*.java src/com/college/dao/*.java src/com/college/view/*.java src/com/college/*.java
java -cp "bin;lib/*" com.college.Main
```
The terminal password prompt is also shown when using this command-line option.
