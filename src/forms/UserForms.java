package src.forms;

import src.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserForms extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton cencelButton;

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM addlibrain WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // If a record is found, the credentials are valid
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // If no record is found, return false
    }
    public UserForms() {
        setTitle("User Forms");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,600));
        setLocationRelativeTo(null);


        cencelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        loginButton.addActionListener(e -> {
            String username = textField1.getText().trim();
            String password = new String(passwordField1.getPassword()).trim();

            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // Here you can proceed to the next window or functionality
                // new MainMenu().setVisible(true);
                LibrainMenu librainMenu = new LibrainMenu();
                librainMenu.setVisible(true);
                this.dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                LoginForms loginForms = new LoginForms(null);
//                loginForms.setVisible(true);
//                dispose();
//            }
//        });
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                LoginForms loginForms = new LoginForms(null);
//                loginForms.setVisible(true);
//                dispose();
//            }
//        });
    }

}
