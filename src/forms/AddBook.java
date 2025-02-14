package src.forms;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jetbrains.annotations.NotNull;
import src.cls.BookCode;
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

public class AddBook extends JFrame {
    private JPanel MainPanel;
    private JTable tblStudent;
    private JTextField txtCode;
    private JTextField nameBook;
    private JTextField Genre;
    private JPanel Date;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JButton backButton;
    private JPanel panelData;
    private JPanel panelControls;
    private JComboBox cboCU;
    private JButton Search;
    private DatePicker dobPicker;
    DefaultTableModel tableModel = new DefaultTableModel();

    //ComboBook
    private KeyValue[] items ={
            new KeyValue(0,""),
            new KeyValue(1,"IT"),
            new KeyValue(2, "History"),
            new KeyValue(3, "Sicientist"),
            new KeyValue(4, "Math"),
            new KeyValue(5, "English")


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
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Couse");
        tableModel.addColumn("Date of Day");
        // Load data from the database
        loadBooks(tableModel);
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


    private void loadBooks(@NotNull DefaultTableModel tableModel){
        // Clear the existing data
        tableModel.setRowCount(0);
        try{
            Connection connection = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, book_name, genre, name_book,date_of_day");
            query.append(" FROM value INNER JOIN book");
            query.append(" ON value.book_id=book.id");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()){
                String code = resultSet.getString("code");
                String book_name = resultSet.getString("book_name");
                String genre = resultSet.getString("genre");
                String name_book = resultSet.getString("name_book");
                Date date_of_day = resultSet.getDate("date_of_day");
                tableModel.addRow(new Object[]{code, book_name, genre, name_book,date_of_day});
            }
            connection.close();
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Error loading data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadBookByCode(String code){
        try{
            Connection con = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, book_name, genre, book_id,date_of_day")
                    .append(" FROM ")
                    .append(" value")
                    .append(" WHERE code = '").append(code).append("'");

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()){
                txtCode.setText(resultSet.getString("code"));
                txtCode.setEditable(false);
                nameBook.setText(resultSet.getString("book_name"));
                Genre.setText(resultSet.getString("genre"));
                setSelectedItemByKey(cboCU,resultSet.getInt("book_id"));
                dobPicker.setDate(resultSet.getObject("date_of_day",LocalDate.class));
            }
            con.close();
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(this,"Error loading data from database "+ex.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private int insertBook(String code, String book_name, String genre, int book_id, LocalDate date_of_day) {
        StringBuilder insertSQL = new StringBuilder();
        insertSQL.append("INSERT INTO value( code,book_name,genre,book_id,date_of_day)")
                .append("values(").append("'")
                .append(code).append("','")
                .append(book_name).append("','")
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
    private void updateBook(String code, String book_name, String genre, int book_id, LocalDate date_of_day) {
        StringBuilder updateSQL = new StringBuilder();

        updateSQL.append("UPDATE value SET")
                .append(" book_name='").append(book_name).append("',")
                .append(" genre='").append(genre).append("',")
                .append(" book_id=").append(book_id).append(",")
                .append(" date_of_day='").append(date_of_day).append("'")
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
    private void deleteBook(String code) {
        StringBuilder deleteSQL = new StringBuilder();
        deleteSQL.append("DELETE FROM value")
                .append(" WHERE code='").append(code).append("'");
        Connection con = DBConnection.getConnection();
        try{
            Statement statement = con.createStatement();
            int rowsAffected = statement.executeUpdate(deleteSQL.toString());
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete the record.");
            }
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
    }
    private void searchBooks(String code, String name) {
        // Clear the existing data
        tableModel.setRowCount(0);
        try {
            Connection connection = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, book_name, genre, name_book, date_of_day")
                    .append(" FROM value INNER JOIN book")
                    .append(" ON value.book_id=book.id")
                    .append(" WHERE 1=1");
            if (!code.isEmpty()) {
                query.append(" AND code = '").append(code).append("'");
            }
            if (!name.isEmpty()) {
                query.append(" AND book_name LIKE '%").append(name).append("%'");
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String codeResult = resultSet.getString("code");
                String book_name = resultSet.getString("book_name");
                String genre = resultSet.getString("genre");
                String name_book = resultSet.getString("name_book");
                Date date_of_day = resultSet.getDate("date_of_day");
                tableModel.addRow(new Object[]{codeResult, book_name, genre, name_book, date_of_day});
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public AddBook() {
        initializedComboBox();
        initializeDatePicker();
        initializedTable();
        setTitle("Add Book");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 600));
//        setLocationRelativeTo(null);
        setContentPane(MainPanel);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearControls(panelControls);
                loadBooks(tableModel);
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
//                    String code = BookCode.generateBookCode();
                    String book_name = nameBook.getText();
                    String genre = Genre.getText();
                    int book_id = getKeyFromSelectedItem(cboCU);
                    LocalDate date_of_day = dobPicker.getDate();
                    int recordID = insertBook(code, book_name, genre, book_id, date_of_day);
                    clearControls(panelControls);
                    loadBooks(tableModel);
                }
                //loadStudentByID(recordID);
        });
        tblStudent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    int selectedRow = tblStudent.getSelectedRow();
                    if(selectedRow != -1){
                        Object selectedCode = tblStudent.getValueAt(selectedRow,0);
                        loadBookByCode(selectedCode.toString());
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtCode.getText().equals("")){
                    String code = txtCode.getText();
                    String book_name = nameBook.getText();
                    String genre = Genre.getText();
                    int book_id = getKeyFromSelectedItem(cboCU);
                    LocalDate date_of_day = dobPicker.getDate();
                    updateBook(code, book_name, genre, book_id, date_of_day);
                    clearControls(panelControls);
                    loadBooks(tableModel);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtCode.getText().equals("")){
                    String code = txtCode.getText();
                    deleteBook(code);
                    clearControls(panelControls);
                    loadBooks(tableModel);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibrainMenu librainMenu = new LibrainMenu();
                librainMenu.setVisible(true);
                dispose();
            }
        });
        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = txtCode.getText().trim();
                String name = nameBook.getText().trim();
                searchBooks(code, name);
            }
        });
    }

}