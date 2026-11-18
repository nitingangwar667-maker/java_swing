package com.college.view;

import com.college.dao.CourseDAO;
import com.college.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseCataloguePanel extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public CourseCataloguePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Header Panel using FlowLayout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel titleLabel = new JLabel("Online Course Catalogue (Core & Elective Courses)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(titleLabel);

        refreshButton = new JButton("Refresh Catalogue");
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(new Color(41, 128, 185));
        refreshButton.setForeground(Color.WHITE);
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Course Code", "Course Name", "Type", "Duration", "Fees (₹)", "Max Seats", "Enrolled", "Available Seats", "Schedule"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(24);
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        loadCourseData();

        refreshButton.addActionListener(e -> loadCourseData());
    }

    public void loadCourseData() {
        tableModel.setRowCount(0);
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            Object[] row = {
                c.getCourseCode(),
                c.getCourseName(),
                c.getCourseType(),
                c.getDuration(),
                String.format("₹%.2f", c.getFees()),
                c.getMaxSeats(),
                c.getEnrolledCount(),
                c.getAvailableSeats(),
                c.getScheduleTime()
            };
            tableModel.addRow(row);
        }
    }
}
