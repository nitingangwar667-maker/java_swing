package com.college.view;

import com.college.dao.CourseDAO;
import com.college.dao.ApplicationDAO;
import com.college.model.Course;
import com.college.model.Application;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ApplicantRegisterDialog extends JDialog {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<Course> courseComboBox;
    private JButton submitButton, cancelButton;

    public ApplicantRegisterDialog(JFrame parent) {
        super(parent, "Apply for Course & Create Applicant Account", true);
        setSize(420, 350);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 244, 248));

        panel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email Address:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Select Course:"));
        courseComboBox = new JComboBox<>();
        loadCoursesIntoCombo();
        panel.add(courseComboBox);

        submitButton = new JButton("Submit Application");
        submitButton.setBackground(new Color(41, 128, 185));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        panel.add(cancelButton);
        panel.add(submitButton);

        add(panel);

        submitButton.addActionListener(e -> handleSubmit());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadCoursesIntoCombo() {
        courseComboBox.removeAllItems();
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            courseComboBox.addItem(c);
        }
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Application app = new Application(name, email, password, selectedCourse.getCourseCode());
        ApplicationDAO appDAO = new ApplicationDAO();
        if (appDAO.submitApplication(app)) {
            JOptionPane.showMessageDialog(this, "Application submitted successfully!\nOnce approved by Admin, you will receive your official student username and password.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit application. Email might already be registered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
