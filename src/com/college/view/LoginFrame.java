package com.college.view;

import com.college.dao.UserDAO;
import com.college.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton, applyButton;

    public LoginFrame() {
        setTitle("Online Course Registration System - Login");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 244, 248));

        // Title Label
        JLabel titleLabel = new JLabel("Online Course Portal Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setBounds(50, 15, 350, 30);
        panel.add(titleLabel);

        // Username Label & Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(50, 65, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 65, 230, 28);
        panel.add(usernameField);

        // Password Label & Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(50, 110, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 110, 230, 28);
        panel.add(passwordField);

        // Role Label & Combo
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setBounds(50, 155, 100, 25);
        panel.add(roleLabel);

        String[] roles = {"Admin", "Student"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 155, 230, 28);
        panel.add(roleComboBox);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 230, 34);
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        // Apply / Create Account Button for Prospective Students
        applyButton = new JButton("Apply for Course / Create Account");
        applyButton.setBounds(150, 245, 230, 32);
        applyButton.setBackground(new Color(46, 204, 113));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFont(new Font("Arial", Font.BOLD, 12));
        applyButton.setFocusPainted(false);
        panel.add(applyButton);

        // Hint Panel / Label
        JLabel hintLabel = new JLabel("<html><center><b>Default Credentials:</b> Admin: <code>admin</code> / <code>password123</code><br/>Student: <code>student1</code> / <code>password123</code> (or Roll No)</center></html>", JLabel.CENTER);
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        hintLabel.setForeground(new Color(100, 100, 100));
        hintLabel.setBounds(50, 285, 350, 65);
        panel.add(hintLabel);

        // Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        applyButton.addActionListener(e -> new ApplicantRegisterDialog(this).setVisible(true));

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null && user.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DashboardFrame(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Role!\nMake sure you select the correct Role (Admin or Student).", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
