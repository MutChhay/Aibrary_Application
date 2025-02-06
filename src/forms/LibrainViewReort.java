package src.forms;

import org.jetbrains.annotations.NotNull;
import src.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LibrainViewReort extends JFrame{

    private JTable tblAddbook;
    private JPanel panelData;
    private JTable tblborrowbook;
    private JTable returnbook;
    private JButton backButton;
    private JPanel MainPanel;
    private JPanel panelData1;
    private JPanel panelData3;
    private JTable tblStudent;
    private JPanel panelData4;
    DefaultTableModel tableModelstudnet = new DefaultTableModel();
    DefaultTableModel tableModelAddbook = new DefaultTableModel();
    DefaultTableModel tableModelIssuebook = new DefaultTableModel();
    DefaultTableModel tableReturnBook = new DefaultTableModel();

    // add book

    private void initializedTable(){
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
        panelData1.add(panelBook);
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



    // add student
    private void initializedTableAddstudnets(){
        // Add columns to the table model
//        tableModel.addColumn("ID");
        tableModelstudnet.addColumn("Student ID");
        tableModelstudnet.addColumn("Student Name");
        tableModelstudnet.addColumn("Department");
        tableModelstudnet.addColumn("Year");
        tableModelstudnet.addColumn("Date of birth");
        // Load data from the database
        loadAddUser(tableModelstudnet);
        tblStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //set data to JTable
        tblStudent.setModel(tableModelstudnet);
        tblStudent.setRowHeight(30);
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(tblStudent);
        scrollPane.setPreferredSize(new Dimension(tblStudent.getPreferredScrollableViewportSize().width,300));
        JPanel panelAddUser = new JPanel(new BorderLayout());
        panelAddUser.add(scrollPane,BorderLayout.CENTER);
        panelData.add(panelAddUser);
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


//      Issue Book
private void initializedTableÍssuebook(){
    // Add columns to the table model
    tableModelIssuebook.addColumn("Code");
    tableModelIssuebook.addColumn("Name title");
    tableModelIssuebook.addColumn("Author ");
    tableModelIssuebook.addColumn("Genre");
    tableModelIssuebook.addColumn("Date of Borrow");
    // Load data from the database
    loadIssueBooks(tableModelIssuebook);
    tblborrowbook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //set data to JTable
    tblborrowbook.setModel(tableModelIssuebook);
    tblborrowbook.setRowHeight(30);
    // Add the JTable to a JScrollPane
    JScrollPane scrollPane = new JScrollPane(tblborrowbook);
    scrollPane.setPreferredSize(new Dimension(tblborrowbook.getPreferredScrollableViewportSize().width,300));
    JPanel panelBook = new JPanel(new BorderLayout());
    panelBook.add(scrollPane,BorderLayout.CENTER);
    panelData3.add(panelBook);
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


    // return book
    private void initializeTablereturnbook() {
        tableReturnBook.addColumn("Code");
        tableReturnBook.addColumn("Name title");
        tableReturnBook.addColumn("Author ");
        tableReturnBook.addColumn("Genre");
        tableReturnBook.addColumn("Date of Due");
        loadReturnBooks(tableReturnBook);
        returnbook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        returnbook.setModel(tableReturnBook);
        returnbook.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(returnbook);
        scrollPane.setPreferredSize(new Dimension(returnbook.getPreferredScrollableViewportSize().width, 300));
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
    public LibrainViewReort(){
        super("View Report");
        initializedTableÍssuebook();
        initializedTableAddstudnets();
        initializeTablereturnbook();
        initializedTable();
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibrainMenu librainMenu = new LibrainMenu();
                librainMenu.setVisible(true);
            }
        });
    }

}

