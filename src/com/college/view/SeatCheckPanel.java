package com.college.view;

import com.college.dao.CourseDAO;
import com.college.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SeatCheckPanel extends JPanel {
    private JTable seatTable;
    private DefaultTableModel tableModel;
    private JButton checkButton;

    public SeatCheckPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Header Panel (FlowLayout)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel titleLabel = new JLabel("Real-Time Seat Limit Check & Availability");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(titleLabel);

        checkButton = new JButton("Check Seat Status");
        checkButton.setFocusPainted(false);
        checkButton.setBackground(new Color(41, 128, 185));
        checkButton.setForeground(Color.WHITE);
        topPanel.add(checkButton);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Course Code", "Course Name", "Course Type", "Max Seats", "Enrolled Count", "Available Seats", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        seatTable = new JTable(tableModel);
        seatTable.setRowHeight(24);
        seatTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(seatTable);
        add(scrollPane, BorderLayout.CENTER);

        loadSeatData();

        checkButton.addActionListener(e -> loadSeatData());
    }

    public void loadSeatData() {
        tableModel.setRowCount(0);
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            int available = c.getAvailableSeats();
            String status = available > 0 ? "Available" : "FULL (CourseFullException)";
            Object[] row = {
                c.getCourseCode(),
                c.getCourseName(),
                c.getCourseType(),
                c.getMaxSeats(),
                c.getEnrolledCount(),
                available,
                status
            };
            tableModel.addRow(row);
        }
    }
}
