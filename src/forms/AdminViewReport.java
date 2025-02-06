package src.forms;

import org.jetbrains.annotations.NotNull;
import src.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminViewReport extends JFrame{
    private JTable tbluser;
    private JTable tblAddbook;
    private JTable table3;
    private JTable tblstudent;
    private JPanel panelData1;
    private JPanel panelData2;
    private JPanel panelData3;
    private JPanel panelData4;
    private JPanel MainPanel;
    private JButton backButton;
    DefaultTableModel tableModel = new DefaultTableModel();
    DefaultTableModel tableModelAddbook = new DefaultTableModel();
    DefaultTableModel tableModelAddUser = new DefaultTableModel();
    DefaultTableModel tableReturnBook = new DefaultTableModel();

    //Librain

    private void initializedTable() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Password");
        tableModel.addColumn("Date of Birth");

        loadAddLibrain(tableModel);
        tbluser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbluser.setModel(tableModel);
        tbluser.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tbluser);
        scrollPane.setPreferredSize(new Dimension(tbluser.getPreferredScrollableViewportSize().width, 300));

        JPanel panelAddUser = new JPanel(new BorderLayout());
        panelAddUser.add(scrollPane, BorderLayout.CENTER);
        panelData1.add(panelAddUser);
    }
    private void loadAddLibrain(@NotNull DefaultTableModel tableModel) {
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

    // Addbook
    private void initializedTableAddbook(){
        // Add columns to the table model
        tableModelAddbook.addColumn("Code");
        tableModelAddbook.addColumn("Book Name");
        tableModelAddbook.addColumn("Genre");
        tableModelAddbook.addColumn("Couse");
        tableModelAddbook.addColumn("Date of Day");
        // Load data from the database
        loadBooks(tableModelAddbook);
        tblAddbook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //set data to JTable
        tblAddbook.setModel(tableModelAddbook);
        tblAddbook.setRowHeight(30);
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(tblAddbook);
        scrollPane.setPreferredSize(new Dimension(tblAddbook.getPreferredScrollableViewportSize().width,300));
        JPanel panelBook = new JPanel(new BorderLayout());
        panelBook.add(scrollPane,BorderLayout.CENTER);
        panelData2.add(panelBook);
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

    // addStudent
    private void initializedTableAddStudent(){
        // Add columns to the table model
//        tableModel.addColumn("ID");
        tableModelAddUser.addColumn("Student ID");
        tableModelAddUser.addColumn("Student Name");
        tableModelAddUser.addColumn("Department");
        tableModelAddUser.addColumn("Year");
        tableModelAddUser.addColumn("Date of birth");
        // Load data from the database
        loadAddUser(tableModelAddUser);
        tblstudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //set data to JTable
        tblstudent.setModel(tableModelAddUser);
        tblstudent.setRowHeight(30);
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(tblstudent);
        scrollPane.setPreferredSize(new Dimension(tblstudent.getPreferredScrollableViewportSize().width,300));
        JPanel panelAddUser = new JPanel(new BorderLayout());
        panelAddUser.add(scrollPane,BorderLayout.CENTER);
        panelData3.add(panelAddUser);
    }
    private void loadAddUser(@NotNull DefaultTableModel tableModel){
        // Clear the existing data
        tableModel.setRowCount(0);
        try{
            Connection connection = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, user_name, couse, gender_name,date_of_birth");
            query.append(" FROM adduser INNER JOIN gender");
            query.append(" ON adduser.gender_id=gender.id");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()){
                String code = resultSet.getString("code");
                String user_name = resultSet.getString("user_name");
                String couse = resultSet.getString("couse");
                String gender_name = resultSet.getString("gender_name");
                Date date_of_birth = resultSet.getDate("date_of_birth");
                tableModel.addRow(new Object[]{code, user_name, couse, gender_name,date_of_birth});
            }
            connection.close();
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Error loading data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void initializeTablereturnbook() {
        tableReturnBook.addColumn("Code");
        tableReturnBook.addColumn("Name title");
        tableReturnBook.addColumn("Author ");
        tableReturnBook.addColumn("Genre");
        tableReturnBook.addColumn("Date of Due");
        loadReturnBooks(tableReturnBook);
        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table3.setModel(tableReturnBook);
        table3.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table3);
        scrollPane.setPreferredSize(new Dimension(table3.getPreferredScrollableViewportSize().width, 300));
        panelData4.setLayout(new BorderLayout());
        panelData4.add(scrollPane, BorderLayout.CENTER);
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
    public AdminViewReport(){
        super("View Report");
        initializeTablereturnbook();
        initializedTableAddStudent();
        initializedTableAddbook();
        initializedTable();
            setContentPane(MainPanel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);
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
