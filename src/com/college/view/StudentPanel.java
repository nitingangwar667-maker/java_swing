package com.college.view;

import com.college.dao.CourseDAO;
import com.college.dao.StudentDAO;
import com.college.model.Course;
import com.college.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField rollNoField, nameField, emailField, phoneField, dobField, addressField;
    private JComboBox<String> genderCombo, courseCombo, semesterCombo;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    public StudentPanel() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top / Left Form Panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        formPanel.setPreferredSize(new Dimension(350, 0));

        formPanel.add(new JLabel("Roll Number:"));
        rollNoField = new JTextField();
        formPanel.add(rollNoField);

        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        formPanel.add(genderCombo);

        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        formPanel.add(dobField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Course:"));
        courseCombo = new JComboBox<>();
        loadCoursesIntoCombo();
        formPanel.add(courseCombo);

        formPanel.add(new JLabel("Semester:"));
        semesterCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
        formPanel.add(semesterCombo);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        // Buttons Panel inside Form
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        btnPanel.add(addButton);
        btnPanel.add(updateButton);
        btnPanel.add(deleteButton);

        JPanel leftContainer = new JPanel(new BorderLayout());
        leftContainer.add(formPanel, BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        bottomButtons.add(deleteButton);
        bottomButtons.add(clearButton);

        JPanel westPanel = new JPanel(new BorderLayout(5, 5));
        westPanel.add(formPanel, BorderLayout.CENTER);
        westPanel.add(bottomButtons, BorderLayout.SOUTH);

        add(westPanel, BorderLayout.WEST);

        // Right Table Panel
        String[] columns = {"Roll No", "Name", "Gender", "DOB", "Email", "Phone", "Course", "Sem", "Address"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Students List"));
        add(scrollPane, BorderLayout.CENTER);

        // Load Data
        loadStudentData();

        // Button Listeners
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());

        // Table Selection Listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                rollNoField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                rollNoField.setEditable(false); // Roll no primary key identifier
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                genderCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                dobField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                courseCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());
                semesterCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 8).toString());
            }
        });
    }

    private void loadCoursesIntoCombo() {
        courseCombo.removeAllItems();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            courseCombo.addItem(c.getCourseCode());
        }
    }

    public void loadStudentData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            Object[] row = {
                s.getRollNo(),
                s.getFullName(),
                s.getGender(),
                s.getDob(),
                s.getEmail(),
                s.getPhone(),
                s.getCourseCode(),
                s.getSemester(),
                s.getAddress()
            };
            tableModel.addRow(row);
        }
    }

    private void addStudent() {
        String rollNo = rollNoField.getText().trim();
        String name = nameField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String dob = dobField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String courseCode = (String) courseCombo.getSelectedItem();
        int semester = Integer.parseInt((String) semesterCombo.getSelectedItem());
        String address = addressField.getText().trim();

        if (rollNo.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(rollNo, name, gender, dob, email, phone, courseCode, semester, address);
        if (studentDAO.addStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student added successfully and fee account initialized!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudentData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Roll No might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        String rollNo = rollNoField.getText().trim();
        String name = nameField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String dob = dobField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String courseCode = (String) courseCombo.getSelectedItem();
        int semester = Integer.parseInt((String) semesterCombo.getSelectedItem());
        String address = addressField.getText().trim();

        if (rollNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(rollNo, name, gender, dob, email, phone, courseCode, semester, address);
        if (studentDAO.updateStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudentData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        String rollNo = rollNoField.getText().trim();
        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student: " + rollNo + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (studentDAO.deleteStudent(rollNo)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        rollNoField.setText("");
        rollNoField.setEditable(true);
        nameField.setText("");
        dobField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        table.clearSelection();
    }
}
