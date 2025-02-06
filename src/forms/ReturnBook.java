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

public class ReturnBook extends JFrame {
    private JPanel MainPanel;
    private JTextField BookID;
    private JTextField nameBook;
    private JTextField BT;
    private JComboBox<KeyValue> cboCU;
    private JPanel Date;
    private JButton returnButton;
    private JButton searchButton;
    private JButton resetButton;
    private JButton backButton;
    private JTable DataReturnbook;
    private JPanel panelData2;
    private JPanel panelControls;
    private DatePicker dobPicker;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private KeyValue[] items = {
            new KeyValue(0, ""),
            new KeyValue(1, "IT"),
            new KeyValue(2, "ITE"),
    };

    private void initializeComboBox() {
        for (KeyValue kw : items) cboCU.addItem(kw);
    }

    private void initializeDatePicker() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dobPicker = new DatePicker(dateSettings);
        Date.setLayout(new BorderLayout());
        Date.add(dobPicker, BorderLayout.CENTER);
        dobPicker.setDate(LocalDate.now().minusYears(18));
    }

    private void initializeTable() {
        tableModel.addColumn("Code");
        tableModel.addColumn("Name title");
        tableModel.addColumn("Author ");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Date of Date");
        loadReturnBooks(tableModel);
        DataReturnbook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DataReturnbook.setModel(tableModel);
        DataReturnbook.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(DataReturnbook);
        scrollPane.setPreferredSize(new Dimension(DataReturnbook.getPreferredScrollableViewportSize().width, 300));
        panelData2.setLayout(new BorderLayout());
        panelData2.add(scrollPane, BorderLayout.CENTER);
    }


    private static void setSelectedItemByKey(@NotNull JComboBox<KeyValue> comboBox, int key) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            KeyValue item = comboBox.getItemAt(i);
            if (item.getKey() == key) {
                comboBox.setSelectedItem(item);
                break;
            }
        }
    }
    private void searchBookByCode(String code) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT code, name, book_title, genre_id, due_date FROM returnbook WHERE code = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BookID.setText(resultSet.getString("code"));
                nameBook.setText(resultSet.getString("name"));
                BT.setText(resultSet.getString("book_title"));
                setSelectedItemByKey(cboCU, resultSet.getInt("genre_id"));
                dobPicker.setDate(resultSet.getObject("due_date", LocalDate.class));
            } else {
                JOptionPane.showMessageDialog(this, "No book found with the given code.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                clearControls(panelControls); // Clear fields if no result found
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching book: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getKeyFromSelectedItem(@NotNull JComboBox<KeyValue> comboBox) {
        KeyValue selectedItem = (KeyValue) comboBox.getSelectedItem();
        return selectedItem != null ? selectedItem.getKey() : 0;
    }

    private void clearControls(@NotNull Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JComboBox) {
                setSelectedItemByKey((JComboBox) component, 0);
            }
        }
        dobPicker.setDate(null);  // Reset to default or appropriate date
    }

    private void loadIssueBookByCode(String code) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT code, name, book_title, genre_id, due_date FROM returnbook WHERE code = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BookID.setText(resultSet.getString("code"));
                nameBook.setText(resultSet.getString("name"));
                BT.setText(resultSet.getString("book_title"));
                setSelectedItemByKey(cboCU, resultSet.getInt("genre_id"));
                dobPicker.setDate(resultSet.getObject("due_date", LocalDate.class));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReturnBooks(@NotNull DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT code, name, book_title, genre_name, due_date FROM returnbook INNER JOIN genre ON returnbook.genre_id = genre.id";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String code = resultSet.getString("code");
                    String name = resultSet.getString("name");
                    String bookTitle = resultSet.getString("book_title");
                    String genreName = resultSet.getString("genre_name");
                    Date dueDate = resultSet.getDate("due_date");
                    tableModel.addRow(new Object[]{code, name, bookTitle, genreName, dueDate});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook(String code) {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Move book from IssueBook to ReturnBook
            String moveQuery = "INSERT INTO returnbook (code, name, book_title, genre_id, due_date) " +
                    "SELECT code, name, book_title, genre_id, issue_date FROM genrebook WHERE code = ?";
            try (PreparedStatement moveStatement = connection.prepareStatement(moveQuery)) {
                moveStatement.setString(1, code);
                int rowsInserted = moveStatement.executeUpdate();

                if (rowsInserted > 0) {
                    // Delete book from IssueBook
                    String deleteQuery = "DELETE FROM genrebook WHERE code = ?";
                    try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                        deleteStatement.setString(1, code);
                        int rowsDeleted = deleteStatement.executeUpdate();

                        if (rowsDeleted > 0) {
                            connection.commit(); // Commit transaction
                            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadReturnBooks(tableModel); // Refresh the ReturnBook table
                        } else {
                            connection.rollback(); // Rollback transaction
                            JOptionPane.showMessageDialog(this, "Failed to delete book from IssueBook.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    connection.rollback(); // Rollback transaction
                    JOptionPane.showMessageDialog(this, "Failed to move book to ReturnBook.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error during return process: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ReturnBook() {
        initializeComboBox();
        initializeDatePicker();
        initializeTable();
        setTitle("Return Book");
        setContentPane(MainPanel);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DataReturnbook.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = DataReturnbook.getSelectedRow();
                    if (selectedRow != -1) {
                        Object selectedCode = DataReturnbook.getValueAt(selectedRow, 0);
                        loadIssueBookByCode(selectedCode.toString());
                    }
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = BookID.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a code to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                searchBookByCode(code);
            }
        });


        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = BookID.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Code is required for adding new records.");
                    return;
                }
                returnBook(code);
                clearControls(panelControls);
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearControls(panelControls);
                loadReturnBooks(tableModel);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibrainMenu librainMenu = new LibrainMenu();
                librainMenu.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReturnBook frame = new ReturnBook();
            frame.setVisible(true);
        });
    }
}
