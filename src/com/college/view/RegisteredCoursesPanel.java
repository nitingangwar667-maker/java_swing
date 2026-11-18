package com.college.view;

import com.college.dao.EnrolmentDAO;
import com.college.model.Course;
import com.college.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RegisteredCoursesPanel extends JPanel {
    private JTable enrolledTable;
    private DefaultTableModel tableModel;
    private JTextField rollNoField;
    private JButton loadButton;
    private JButton dropButton;
    private User currentUser;

    public RegisteredCoursesPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Control Panel (FlowLayout)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel titleLabel = new JLabel("Registered Courses & Timetable Viewer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(titleLabel);

        topPanel.add(new JLabel("Roll No:"));
        rollNoField = new JTextField(12);
        rollNoField.setText("CSE001");
        topPanel.add(rollNoField);

        loadButton = new JButton("View Registered Courses");
        loadButton.setBackground(new Color(41, 128, 185));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        topPanel.add(loadButton);

        dropButton = new JButton("Drop Selected Course");
        dropButton.setBackground(new Color(231, 76, 60));
        dropButton.setForeground(Color.WHITE);
        dropButton.setFocusPainted(false);
        topPanel.add(dropButton);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Course Code", "Course Name", "Type", "Duration", "Fees (₹)", "Schedule Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrolledTable = new JTable(tableModel);
        enrolledTable.setRowHeight(24);
        enrolledTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(enrolledTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Enrolled Courses & Timetable Schedule"));
        add(scrollPane, BorderLayout.CENTER);

        loadEnrolledCourses();

        loadButton.addActionListener(e -> loadEnrolledCourses());
        dropButton.addActionListener(e -> handleDropCourse());
    }

    private void loadEnrolledCourses() {
        tableModel.setRowCount(0);
        String rollNo = rollNoField.getText().trim();
        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student Roll Number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EnrolmentDAO enrolmentDAO = new EnrolmentDAO();
        List<Course> list = enrolmentDAO.getRegisteredCourses(rollNo);
        for (Course c : list) {
            Object[] row = {
                c.getCourseCode(),
                c.getCourseName(),
                c.getCourseType(),
                c.getDuration(),
                String.format("₹%.2f", c.getFees()),
                c.getScheduleTime()
            };
            tableModel.addRow(row);
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No registered courses found for roll no: " + rollNo, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleDropCourse() {
        int selectedRow = enrolledTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course from the table to drop!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rollNo = rollNoField.getText().trim();
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to drop course " + courseCode + "?", "Confirm Drop", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            EnrolmentDAO enrolmentDAO = new EnrolmentDAO();
            try {
                boolean success = enrolmentDAO.dropCourse(rollNo, courseCode);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Successfully dropped course " + courseCode + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEnrolledCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to drop course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
