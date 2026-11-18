package com.college.view;

import com.college.dao.ApplicationDAO;
import com.college.model.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton approveButton, refreshButton;

    public ApplicationManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel titleLabel = new JLabel("Pending Course Applications & Seat Allotment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(titleLabel);

        approveButton = new JButton("Approve & Allot Seat");
        approveButton.setBackground(new Color(46, 204, 113));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFocusPainted(false);
        topPanel.add(approveButton);

        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(41, 128, 185));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"App ID", "Full Name", "Email", "Course Code", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadApplications();

        refreshButton.addActionListener(e -> loadApplications());
        approveButton.addActionListener(e -> handleApprove());
    }

    public void loadApplications() {
        tableModel.setRowCount(0);
        ApplicationDAO dao = new ApplicationDAO();
        List<Application> list = dao.getAllApplications();
        for (Application app : list) {
            Object[] row = {
                app.getApplicationId(),
                app.getFullName(),
                app.getEmail(),
                app.getCourseCode(),
                app.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void handleApprove() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application to approve!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 4);

        if (!status.equalsIgnoreCase("Pending")) {
            JOptionPane.showMessageDialog(this, "This application is already " + status + "!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rollNo = JOptionPane.showInputDialog(this, "Enter official Student Roll Number to allot (e.g., CSE002):", "Seat Allotment", JOptionPane.QUESTION_MESSAGE);
        if (rollNo == null || rollNo.trim().isEmpty()) {
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();
        if (dao.approveAndAllotSeat(appId, rollNo.trim())) {
            JOptionPane.showMessageDialog(this, "Seat successfully allotted! Student account created with Roll No as password.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadApplications();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to allot seat. Roll No might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
