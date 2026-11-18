package com.college.view;

import com.college.dao.CourseDAO;
import com.college.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, durationField, feesField;
    private CourseDAO courseDAO;

    public CoursePanel() {
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        formPanel.setPreferredSize(new Dimension(350, 0));

        formPanel.add(new JLabel("Course Code:"));
        codeField = new JTextField();
        formPanel.add(codeField);

        formPanel.add(new JLabel("Course Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Duration:"));
        durationField = new JTextField();
        formPanel.add(durationField);

        formPanel.add(new JLabel("Fees (INR):"));
        feesField = new JTextField();
        formPanel.add(feesField);

        JButton addButton = new JButton("Add Course");
        formPanel.add(new JLabel(""));
        formPanel.add(addButton);

        add(formPanel, BorderLayout.WEST);

        // Table Panel
        String[] columns = {"Course Code", "Course Name", "Duration", "Fees (INR)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        add(scrollPane, BorderLayout.CENTER);

        loadCourseData();

        addButton.addActionListener(e -> addCourse());
    }

    public void loadCourseData() {
        tableModel.setRowCount(0);
        List<Course> list = courseDAO.getAllCourses();
        for (Course c : list) {
            Object[] row = {
                c.getCourseCode(),
                c.getCourseName(),
                c.getDuration(),
                c.getFees()
            };
            tableModel.addRow(row);
        }
    }

    private void addCourse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String duration = durationField.getText().trim();
        String feesStr = feesField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || duration.isEmpty() || feesStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double fees = Double.parseDouble(feesStr);
            Course course = new Course(code, name, duration, fees);
            if (courseDAO.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourseData();
                codeField.setText("");
                nameField.setText("");
                durationField.setText("");
                feesField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course. Code might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for fees!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
