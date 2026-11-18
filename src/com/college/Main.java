package com.college;
import com.college.util.DatabaseInitializer;
import com.college.util.DatabaseConnection;
import com.college.view.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.promptForPassword();

        // Initialize Database tables automatically on startup
        DatabaseInitializer.initializeDatabase();

        // Set Look and Feel to System Look and Feel (or Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Launch Login Frame on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
