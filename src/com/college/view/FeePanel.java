package com.college.view;

import com.college.dao.FeeDAO;
import com.college.model.Fee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FeePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField rollNoField, payAmountField;
    private FeeDAO feeDAO;

    public FeePanel() {
        feeDAO = new FeeDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Fee Payment Processing"));
        formPanel.setPreferredSize(new Dimension(350, 0));

        formPanel.add(new JLabel("Roll Number:"));
        rollNoField = new JTextField();
        formPanel.add(rollNoField);

        formPanel.add(new JLabel("Payment Amount:"));
        payAmountField = new JTextField();
        formPanel.add(payAmountField);

        JButton payButton = new JButton("Pay Fees");
        JButton refreshButton = new JButton("Refresh Data");

        formPanel.add(payButton);
        formPanel.add(refreshButton);

        add(formPanel, BorderLayout.WEST);

        // Table Panel
        String[] columns = {"Fee ID", "Roll Number", "Total Fees", "Paid Fees", "Due Fees", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Fee Records"));
        add(scrollPane, BorderLayout.CENTER);

        loadFeeData();

        payButton.addActionListener(e -> processPayment());
        refreshButton.addActionListener(e -> loadFeeData());

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                rollNoField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            }
        });
    }

    public void loadFeeData() {
        tableModel.setRowCount(0);
        List<Fee> list = feeDAO.getAllFees();
        for (Fee f : list) {
            Object[] row = {
                f.getFeeId(),
                f.getRollNo(),
                f.getTotalFees(),
                f.getPaidFees(),
                f.getDueFees(),
                f.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void processPayment() {
        String rollNo = rollNoField.getText().trim();
        String amountStr = payAmountField.getText().trim();

        if (rollNo.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Roll Number and Payment Amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Payment amount must be greater than zero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = feeDAO.updateFeePayment(rollNo, amount);
            if (success) {
                JOptionPane.showMessageDialog(this, "Payment processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFeeData();
                payAmountField.setText("");
                rollNoField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process payment. Check if Roll Number exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
