package com.college.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getServerConnection();
             Statement stmt = conn.createStatement();
             BufferedReader reader = Files.newBufferedReader(Paths.get("Database_Setup.sql"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                    continue;
                }
                sb.append(line).append("\n");
                if (trimmed.endsWith(";")) {
                    String sql = sb.toString().trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                    sb.setLength(0);
                }
            }
            System.out.println("Database and tables initialized successfully!");
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Database initialization failed: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
