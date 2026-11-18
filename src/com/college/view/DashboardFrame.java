package com.college.view;

import com.college.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User currentUser;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Online Course Registration System - Portal (" + currentUser.getRole() + ")");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));

        CourseCataloguePanel cataloguePanel = new CourseCataloguePanel();
        CourseEnrolmentPanel enrolmentPanel = new CourseEnrolmentPanel(currentUser);
        SeatCheckPanel seatCheckPanel = new SeatCheckPanel();
        RegisteredCoursesPanel registeredPanel = new RegisteredCoursesPanel(currentUser);

        if (currentUser.getRole().equalsIgnoreCase("Student")) {
            // Student sees only the 4 core modules (Catalogue, Enrolment, Seat Check, Registered Courses)
            tabbedPane.addTab("1. Course Catalogue", cataloguePanel);
            tabbedPane.addTab("2. Course Enrolment", enrolmentPanel);
            tabbedPane.addTab("3. Seat Limit Check", seatCheckPanel);
            tabbedPane.addTab("4. Registered Course List", registeredPanel);
            tabbedPane.setSelectedIndex(1); // Default to Course Enrolment
        } else {
            // Admin sees full administration + Course Applications approval tab
            StudentPanel studentPanel = new StudentPanel();
            ApplicationManagementPanel appPanel = new ApplicationManagementPanel();
            CoursePanel coursePanel = new CoursePanel();
            FeePanel feePanel = new FeePanel();

            tabbedPane.addTab("1. Course Catalogue", cataloguePanel);
            tabbedPane.addTab("2. Course Applications & Approvals", appPanel);
            tabbedPane.addTab("3. Student Seat Allotment / Records", studentPanel);
            tabbedPane.addTab("4. Course Enrolment", enrolmentPanel);
            tabbedPane.addTab("5. Seat Limit Check", seatCheckPanel);
            tabbedPane.addTab("6. Registered Course List", registeredPanel);
            tabbedPane.addTab("Manage Courses", coursePanel);
            tabbedPane.addTab("Fee Records", feePanel);
            tabbedPane.setSelectedIndex(0);
        }

        add(tabbedPane, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}
