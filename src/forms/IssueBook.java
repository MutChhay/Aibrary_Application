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

public class IssueBook extends JFrame {
    private JPanel MainPanel;
    private JTable tblStudent;
    private JTextField txtCode;
    private JTextField nameBook;
    private JTextField Genre;
    private JPanel Date;
    private JButton addButton;
    private JButton updateButton;
    private JButton resetButton;
    private JPanel panelData;
    private JPanel panelControls;
    private JComboBox cboCU;
    private JButton backButton;
    private JButton searchButton;
    private DatePicker dobPicker;
    DefaultTableModel tableModel = new DefaultTableModel();

    //ComboBook
    private KeyValue[] items ={
            new KeyValue(0,""),
            new KeyValue(1,"IT"),
            new KeyValue(2, "ITE"),
            new KeyValue(3, "History"),
            new KeyValue(4, "English"),
            new KeyValue(5, "Khmer"),
            new KeyValue(6, "Biology"),
            new KeyValue(7, "Math"),
            new KeyValue(8, "BIOE"),
            new KeyValue(9, ""),

    };
    private void initializedComboBox(){
        for(KeyValue kw:items) cboCU.addItem(kw);
    }
    //date of day
    private void initializeDatePicker() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
// Add the date picker to your JPanel
        dobPicker = new DatePicker(dateSettings);
        Date.add(dobPicker);
        dobPicker.setDate(LocalDate.now().minusYears(18));
    }

    private void initializedTable(){
        // Add columns to the table model
        tableModel.addColumn("Code");
        tableModel.addColumn("Name title");
        tableModel.addColumn("Author ");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Date of addition");
        // Load data from the database
        loadIssueBooks(tableModel);
        tblStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //set data to JTable
        tblStudent.setModel(tableModel);
        tblStudent.setRowHeight(30);
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(tblStudent);
        scrollPane.setPreferredSize(new Dimension(tblStudent.getPreferredScrollableViewportSize().width,300));
        JPanel panelBook = new JPanel(new BorderLayout());
        panelBook.add(scrollPane,BorderLayout.CENTER);
        panelData.add(panelBook);
    }
    private void searchBookByCode(String code) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT code, name, book_title, genre_id, issue_date FROM genrebook WHERE code = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                txtCode.setText(resultSet.getString("code"));
                nameBook.setText(resultSet.getString("name"));
                Genre.setText(resultSet.getString("book_title"));
                setSelectedItemByKey(cboCU, resultSet.getInt("genre_id"));
                dobPicker.setDate(resultSet.getObject("issue_date", LocalDate.class));
            } else {
                JOptionPane.showMessageDialog(this, "No book found with the given code.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                clearControls(panelControls); // Clear fields if no result found
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching book: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // select keyValue by Item
    private static void setSelectedItemByKey(@NotNull JComboBox<KeyValue> comboBox, int key) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            KeyValue item = comboBox.getItemAt(i);
            if (item.getKey() == key) {
                comboBox.setSelectedItem(item);
                break;
            }
        }
    }
    // get key from select item
    private int getKeyFromSelectedItem(@NotNull JComboBox<KeyValue> comboBox){
        KeyValue selectedItem = (KeyValue) comboBox.getSelectedItem();
        int selectedKey = 0;
        if (selectedItem != null) {
            selectedKey = selectedItem.getKey();
        }
        return selectedKey;
    }
    // clear control
    private void clearControls(@NotNull Container container){
        //txtCode.setText("");
        //txtNameLatin.setText("");
        for(Component component:container.getComponents()){
            if(component instanceof JTextField){
                ((JTextField)component).setText("");
            }
            else if(component instanceof JComboBox){
                setSelectedItemByKey((JComboBox)component,0);
            }
            dobPicker.setDate(null);
        }
    }


    private void loadIssueBooks(@NotNull DefaultTableModel tableModel){
        // Clear the existing data
        tableModel.setRowCount(0);
        try{
            Connection connection = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, name, book_title, genre_name,issue_date");
            query.append(" FROM genrebook INNER JOIN genre");
            query.append(" ON genrebook.genre_id=genre.id");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()){
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                String book_title = resultSet.getString("book_title");
                String genre_name = resultSet.getString("genre_name");
                Date issue_date = resultSet.getDate("issue_date");
                tableModel.addRow(new Object[]{code, name, book_title, genre_name,issue_date});
            }
            connection.close();
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Error loading data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadIssueBookByCode(String code) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT code, name, book_title, genre_id, issue_date FROM genrebook WHERE code = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                txtCode.setText(resultSet.getString("code"));
//                BookID.setEditable(false);
                nameBook.setText(resultSet.getString("name"));
                Genre.setText(resultSet.getString("book_title"));
                setSelectedItemByKey(cboCU, resultSet.getInt("genre_id"));
                dobPicker.setDate(resultSet.getObject("issue_date", LocalDate.class));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private int insertIssueBook(String code, String name, String genre, int book_id, LocalDate date_of_day) {
        StringBuilder insertSQL = new StringBuilder();
        insertSQL.append("INSERT INTO genrebook( code,name,book_title,genre_id,issue_date)")
                .append("values(").append("'")
                .append(code).append("','")
                .append(name).append("','")
                .append(genre).append("',")
                .append(book_id).append(",'")
                .append(date_of_day).append("')");
        int recordID=0;
        Connection con = DBConnection.getConnection();
        try{
            Statement statement = con.createStatement();
            int rowsAffected = statement.executeUpdate(insertSQL.toString(),Statement.RETURN_GENERATED_KEYS);
            if (rowsAffected > 0) {
                ResultSet genKeys = statement.getGeneratedKeys();
                if (genKeys.next()){
                    recordID = genKeys.getInt(1);
                }
                JOptionPane.showMessageDialog(null, "Record inserted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to insert the record.");
            }
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage());
        }
        finally {
            return recordID;
        }
    }
    private void updateIssueBook(String code, String name, String book_title, int genre_id, LocalDate issue_date) {
        StringBuilder updateSQL = new StringBuilder();

        updateSQL.append("UPDATE genrebook SET")
                .append(" name='").append(name).append("',")
                .append(" book_title='").append(book_title).append("',")
                .append(" genre_id=").append(genre_id).append(",")
                .append(" issue_date='").append(issue_date).append("'")
                .append(" WHERE code='").append(code).append("'");
        Connection con = DBConnection.getConnection();
        try{
            Statement statement = con.createStatement();
            int rowsAffected = statement.executeUpdate(updateSQL.toString());
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update the record.");
            }
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
    }
//    private void deleteBook(String code) {
//        StringBuilder deleteSQL = new StringBuilder();
//        deleteSQL.append("DELETE FROM value")
//                .append(" WHERE code='").append(code).append("'");
//        Connection con = DBConnection.getConnection();
//        try{
//            Statement statement = con.createStatement();
//            int rowsAffected = statement.executeUpdate(deleteSQL.toString());
//            if (rowsAffected > 0) {
//                JOptionPane.showMessageDialog(null, "Record deleted successfully!");
//            } else {
//                JOptionPane.showMessageDialog(null, "Failed to delete the record.");
//            }
//            con.close();
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
//        }
//    }


    public IssueBook() {
        initializedComboBox();
        initializeDatePicker();
        initializedTable();
        setTitle("Issue Book");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setContentPane(MainPanel);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearControls(panelControls);
                loadIssueBooks(tableModel);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = txtCode.getText().trim(); // Use the code entered by the user
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Code is required for adding new records.");
                    return;
                }
                String book_name = nameBook.getText();
                String genre = Genre.getText();
                int book_id = getKeyFromSelectedItem(cboCU);
                LocalDate date_of_day = dobPicker.getDate();
                int recordID = insertIssueBook(code, book_name, genre, book_id, date_of_day);
                clearControls(panelControls);
                loadIssueBooks(tableModel);
            }
        });

        tblStudent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    int selectedRow = tblStudent.getSelectedRow();
                    if(selectedRow != -1){
                        Object selectedCode = tblStudent.getValueAt(selectedRow,0);
                        loadIssueBookByCode(selectedCode.toString());
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtCode.getText().equals("")){
                    String code = txtCode.getText();
                    String name = nameBook.getText();
                    String book_title = Genre.getText();
                    int genre_id = getKeyFromSelectedItem(cboCU);
                    LocalDate issue_date = dobPicker.getDate();
                    updateIssueBook(code, name, book_title, genre_id, issue_date);
                    clearControls(panelControls);
                    loadIssueBooks(tableModel);
                }
            }
        });
//        deleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(!txtCode.getText().equals("")){
//                    String code = txtCode.getText();
//                    deleteBook(code);
//                    clearControls(panelControls);
//                    loadBooks(tableModel);
//                }
//            }
//        });
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                AdminMenu adminMenu = new AdminMenu();
//                adminMenu.setVisible(true);
//                dispose();
//            }
//        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibrainMenu librainMenu = new LibrainMenu();
                librainMenu.setVisible(true);
                dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = txtCode.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a code to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                searchBookByCode(code);
            }
        });

    }

}