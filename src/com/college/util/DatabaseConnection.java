package com.college.util;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/college_db?useSSL=false&serverTimezone=UTC";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String USER = System.getenv().getOrDefault("MYSQL_USER", "root");
    private static String password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found. Make sure mysql-connector-j is on the classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, getPassword());
    }

    public static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(SERVER_URL, USER, getPassword());
    }

    public static void promptForPassword() {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("No terminal is available for password input. Run the project from Command Prompt or PowerShell using run.bat.");
        }

        char[] enteredPassword = console.readPassword("Enter MySQL password for user '%s': ", USER);
        if (enteredPassword == null) {
            throw new IllegalStateException("MySQL password input was cancelled.");
        }
        try {
            password = new String(enteredPassword);
        } finally {
            Arrays.fill(enteredPassword, '\0');
        }
    }

    private static String getPassword() throws SQLException {
        if (password == null) {
            throw new SQLException("MySQL password has not been entered.");
        }
        return password;
    }
}
