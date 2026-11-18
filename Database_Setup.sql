-- Online Course Registration System Database Setup
CREATE DATABASE IF NOT EXISTS college_db;
USE college_db;

-- Users table for (Roles: Admin, Student)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'Admin', 'Student'
);

-- Insert default users (Admin, Student)
INSERT INTO users (username, password, role) VALUES ('admin', 'password123', 'Admin')
ON DUPLICATE KEY UPDATE password = 'password123';

INSERT INTO users (username, password, role) VALUES ('student1', 'password123', 'Student')
ON DUPLICATE KEY UPDATE password = 'password123';

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    duration VARCHAR(50) NOT NULL,
    fees DECIMAL(10, 2) NOT NULL,
    max_seats INT DEFAULT 50,
    enrolled_count INT DEFAULT 0,
    course_type VARCHAR(20) DEFAULT 'Core',
    schedule_time VARCHAR(50) DEFAULT 'Mon/Wed 10:00 AM'
);


INSERT INTO courses (course_code, course_name, duration, fees, max_seats, enrolled_count, course_type, schedule_time) VALUES
('CSE101', 'Data Structures & Algorithms', '1 Semester', 15000.00, 3, 1, 'Core', 'Mon/Wed 09:00 AM'),
('CSE102', 'Object Oriented Programming', '1 Semester', 15000.00, 40, 0, 'Core', 'Tue/Thu 11:00 AM'),
('ECE101', 'Digital Logic Design', '1 Semester', 12000.00, 30, 0, 'Core', 'Mon/Wed 02:00 PM'),
('ELE201', 'Artificial Intelligence & ML', '1 Semester', 18000.00, 2, 0, 'Elective', 'Fri 10:00 AM - 01:00 PM'),
('ELE202', 'Cloud Computing Architecture', '1 Semester', 16000.00, 25, 0, 'Elective', 'Tue/Thu 03:00 PM'),
('MBA104', 'Business Ethics & Management', '1 Semester', 20000.00, 35, 0, 'Elective', 'Wed 03:00 PM')
ON DUPLICATE KEY UPDATE course_name=COURSE_NAME;

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    dob VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    address TEXT NOT NULL,
    admission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_code) REFERENCES courses(course_code)
);

-- Insert sample student record matching student1 (roll_no: CSE001, full_name: John Doe)
INSERT INTO students (roll_no, full_name, gender, dob, email, phone, course_code, semester, address)
SELECT 'CSE001', 'John Doe', 'Male', '2003-05-15', 'john@college.edu', '9876543210', 'CSE101', 3, '123 Campus Street'
WHERE NOT EXISTS (SELECT * FROM students WHERE roll_no = 'CSE001');

-- Insert user accounts for John Doe (Name and RollNo both work as username, password is roll_no: CSE001)
INSERT INTO users (username, password, role) VALUES ('John Doe', 'CSE001', 'Student')
ON DUPLICATE KEY UPDATE password = 'CSE001';

INSERT INTO users (username, password, role) VALUES ('CSE001', 'CSE001', 'Student')
ON DUPLICATE KEY UPDATE password = 'CSE001';

-- Fees table
CREATE TABLE IF NOT EXISTS fees (
    fee_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(30) NOT NULL,
    total_fees DECIMAL(10, 2) NOT NULL,
    paid_fees DECIMAL(10, 2) NOT NULL,
    due_fees DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'Paid', 'Partial', 'Pending'
    last_payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE
);

-- Insert sample fee record
INSERT INTO fees (roll_no, total_fees, paid_fees, due_fees, status)
SELECT 'CSE001', 15000.00, 7500.00, 7500.00, 'Partial'
WHERE NOT EXISTS (SELECT * FROM fees WHERE roll_no = 'CSE001');

-- Course Applications table for prospective students
CREATE TABLE IF NOT EXISTS applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending', -- 'Pending', 'Approved', 'Rejected'
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE
);

-- Enrolments table for Online Course Registration System
CREATE TABLE IF NOT EXISTS enrolments (
    enrolment_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(30) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    enrolment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Enrolled', -- 'Enrolled', 'Dropped', 'Completed'
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE,
    UNIQUE KEY unique_student_course (roll_no, course_code)
);

-- Insert sample enrolment for student1 in CSE101
INSERT INTO enrolments (roll_no, course_code, status)
SELECT 'CSE001', 'CSE101', 'Enrolled'
WHERE NOT EXISTS (SELECT * FROM enrolments WHERE roll_no = 'CSE001' AND course_code = 'CSE101');
