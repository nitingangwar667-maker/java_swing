package com.college.view;

import com.college.dao.CourseDAO;
import com.college.dao.EnrolmentDAO;
import com.college.model.Course;
import com.college.model.User;
import course.reg.CourseFullException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CourseEnrolmentPanel extends JPanel {
    private JTextField rollNoField;
    private JComboBox<Course> courseComboBox;
    private JButton enrollButton;
    private JTextArea infoArea;
    private User currentUser;

    public CourseEnrolmentPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel (FlowLayout)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Student Course Enrolment Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel using GridLayout (Unit 5 requirement)
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enrolment Form"));

        JLabel rollLabel = new JLabel("Student Roll No:");
        rollLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rollNoField = new JTextField();
        if (currentUser.getRole().equalsIgnoreCase("Student")) {
            // If logged in as student, try to pre-fill roll no based on username or default sample 'CSE001'
            rollNoField.setText("CSE001"); // default mapping for demo student1
            rollNoField.setEditable(false);
        }

        JLabel courseLabel = new JLabel("Select Course:");
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        courseComboBox = new JComboBox<>();
        loadCoursesIntoCombo();

        formPanel.add(rollLabel);
        formPanel.add(rollNoField);
        formPanel.add(courseLabel);
        formPanel.add(courseComboBox);

        // Button Panel (FlowLayout)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        enrollButton = new JButton("Confirm Enrolment");
        enrollButton.setFont(new Font("Arial", Font.BOLD, 14));
        enrollButton.setBackground(new Color(46, 204, 113));
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setFocusPainted(false);
        buttonPanel.add(enrollButton);

        JPanel centerContainer = new JPanel(new BorderLayout(10, 10));
        centerContainer.add(formPanel, BorderLayout.CENTER);
        centerContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(centerContainer, BorderLayout.CENTER);

        // Info Area for feedback / logs
        infoArea = new JTextArea(6, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setText("Instructions:\n1. Verify your Student Roll Number.\n2. Choose an available Core or Elective Course.\n3. Click 'Confirm Enrolment' to check seat limit and register via PreparedStatement.");
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Status & Seat Check Log"));
        add(scrollPane, BorderLayout.SOUTH);

        // ActionListener (Unit 5)
        enrollButton.addActionListener(e -> handleEnrolment());
    }

    private void loadCoursesIntoCombo() {
        courseComboBox.removeAllItems();
        CourseDAO courseDAO = new CourseDAO();
        List<Course> list = courseDAO.getAllCourses();
        for (Course c : list) {
            courseComboBox.addItem(c);
        }
    }

    private void handleEnrolment() {
        String rollNo = rollNoField.getText().trim();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();

        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student Roll Number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to enrol!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EnrolmentDAO enrolmentDAO = new EnrolmentDAO();
        try {
            boolean success = enrolmentDAO.enrollStudent(rollNo, selectedCourse.getCourseCode());
            if (success) {
                String msg = "Successfully enrolled student " + rollNo + " into " + selectedCourse.getCourseName() + " (" + selectedCourse.getCourseCode() + ")!\n";
                infoArea.append("\n[SUCCESS] " + msg);
                JOptionPane.showMessageDialog(this, msg, "Enrolment Successful", JOptionPane.INFORMATION_MESSAGE);
                loadCoursesIntoCombo(); // refresh available seats
            }
        } catch (CourseFullException cfe) {
            infoArea.append("\n[COURSE FULL EXCEPTION] " + cfe.getMessage());
            JOptionPane.showMessageDialog(this, cfe.getMessage(), "Course Full", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException sqle) {
            infoArea.append("\n[DATABASE ERROR] " + sqle.getMessage());
            JOptionPane.showMessageDialog(this, sqle.getMessage(), "Enrolment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
