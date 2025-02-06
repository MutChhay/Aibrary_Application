package src.forms;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jetbrains.annotations.NotNull;
import src.cls.KeyValue;
import src.db.DBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class AddLibrain extends JFrame {
    private JTextField ID;
    private JTextField Username;
    private JTextField Password;
    private JTextField PhoneNumber;
    private JButton addButton;
    private JButton updateButton;
    private JButton resetButton;
    private JPanel MainPanel;
    private JPanel DOB;
    private JTable tbluser;
    private JButton deleteButton;
    private JButton backButton;
    private JPanel dob;
    private DatePicker dobPicker;
    private JComboBox<KeyValue> COG; // Assuming KeyValue is used for ComboBox items
    private JPanel panelData;
    private JPanel panelControls;
    DefaultTableModel tableModel = new DefaultTableModel();

    private void initializeDatePicker() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dobPicker = new DatePicker(dateSettings);
        dob.setLayout(new BorderLayout()); // Ensure dob panel has layout
        dob.add(dobPicker, BorderLayout.CENTER);
        dobPicker.setDate(LocalDate.now().minusYears(18));
    }

    private static void setSelectedItemByKey(@NotNull JTextField textField, int key, @NotNull List<KeyValue> keyValuePairs) {
        keyValuePairs.stream()
                .filter(item -> item.getKey() == key)
                .findFirst()
                .ifPresent(item -> textField.setText(item.getValue()));
    }

    private void clearControls(@NotNull Container container) {
            for (Component component : container.getComponents()) {
                if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                }
            }
            dobPicker.setDate(null);
    }

    private int getKeyFromTextField(@NotNull JTextField textField) {
        try {
            return Integer.parseInt(textField.getText().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer format: " + e.getMessage());
            return 0;
        }
    }

    private void initializedTable() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Password");
        tableModel.addColumn("Date of Birth");

        loadAddUser(tableModel);
        tbluser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbluser.setModel(tableModel);
        tbluser.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tbluser);
        scrollPane.setPreferredSize(new Dimension(tbluser.getPreferredScrollableViewportSize().width, 300));

        JPanel panelAddUser = new JPanel(new BorderLayout());
        panelAddUser.add(scrollPane, BorderLayout.CENTER);
        panelData.add(panelAddUser);
    }

    private void loadAddUser(@NotNull DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT librain_id, username, phone_number, password, date_of_birth FROM addlibrain")) {

            while (resultSet.next()) {
                String librain_id = resultSet.getString("librain_id");
                String username = resultSet.getString("username");
                String phone_number = resultSet.getString("phone_number");
                String password = resultSet.getString("password");
                Date date_of_birth = resultSet.getDate("date_of_birth");
                tableModel.addRow(new Object[]{librain_id, username, phone_number, password, date_of_birth});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAddUserByCode(String code) {
        String query = "SELECT username, phone_number, password, librain_id, date_of_birth FROM addlibrain WHERE librain_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ID.setText(resultSet.getString("librain_id"));
                    Username.setText(resultSet.getString("username"));
                    PhoneNumber.setText(resultSet.getString("phone_number"));
                    Password.setText(resultSet.getString("password"));
                    dobPicker.setDate(resultSet.getObject("date_of_birth", LocalDate.class));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertAddUser(String id, String username, String phone_number, String password, LocalDate date_of_birth) {
        String insertSQL = "INSERT INTO addlibrain(librain_id, username, phone_number, password, date_of_birth) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(insertSQL)) {

            statement.setString(1, id);
            statement.setString(2, username);
            statement.setString(3, phone_number);
            statement.setString(4, password);
            statement.setDate(5, Date.valueOf(date_of_birth));
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record inserted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to insert the record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage());
        }
    }

    private void updateAddUser(String id, String username, String phone_number, String password, LocalDate date_of_birth) {
        String updateSQL = "UPDATE addlibrain SET username = ?, phone_number = ?, password = ?, date_of_birth = ? WHERE librain_id = ?";
        try (   Connection con = DBConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(updateSQL)) {

            statement.setString(1, username);
            statement.setString(2, phone_number);
            statement.setString(3, password);
            statement.setDate(4, Date.valueOf(date_of_birth));
            statement.setString(5, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update the record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating record: " + e.getMessage());
        }
    }

        private void deleteAddUser(String id) {
            String deleteSQL = "DELETE FROM addlibrain WHERE librain_id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement statement = con.prepareStatement(deleteSQL)) {

                statement.setString(1, id);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete the record.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting record: " + e.getMessage());
            }
        }

    public AddLibrain() {
        initializedTable();
        initializeDatePicker();
        setTitle("Manage Librarian");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(MainPanel);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearControls(panelControls);
                loadAddUser(tableModel);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = ID.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ID is required for adding new records.");
                    return;
                }

                String username = Username.getText().trim();
                String phone_number = PhoneNumber.getText().trim();
                String password = Password.getText().trim();
                LocalDate date_of_birth = dobPicker.getDate();
                if (date_of_birth == null) {
                    JOptionPane.showMessageDialog(null, "Date of birth is required.");
                    return;
                }

                insertAddUser(id, username, phone_number, password, date_of_birth);
                clearControls(panelControls);
                loadAddUser(tableModel);
            }
        });

        tbluser.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tbluser.getSelectedRow();
                    if (selectedRow != -1) {
                        Object selectedCode = tbluser.getValueAt(selectedRow, 0);
                        loadAddUserByCode(selectedCode.toString());
                    }
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ID.getText().isEmpty()) {
                    String id = ID.getText().trim();
                    String username = Username.getText().trim();
                    String phone_number = PhoneNumber.getText().trim();
                    String password = Password.getText().trim();
                    LocalDate date_of_birth = dobPicker.getDate();
                    updateAddUser(id, username, phone_number, password, date_of_birth);
                    clearControls(panelControls);
                    loadAddUser(tableModel);
                } else {
                    JOptionPane.showMessageDialog(null, "ID is required for updating records.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ID.getText().isEmpty()) {
                    String id = ID.getText().trim();
                    deleteAddUser(id);
                    clearControls(panelControls);
                    loadAddUser(tableModel);
                } else {
                    JOptionPane.showMessageDialog(null, "ID is required for deleting records.");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminMenu adminMenu = new AdminMenu();
                adminMenu.setVisible(true);
                dispose();
            }
        });
    }
}
