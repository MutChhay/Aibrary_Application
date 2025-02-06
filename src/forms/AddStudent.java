package src.forms;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jetbrains.annotations.NotNull;
import src.cls.AddUserCode;
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

public class AddStudent extends JFrame {
    private JTextField ID;
    private JTextField Username;
    private JTextField Couse;
    private JButton addButton;
    private JButton updateButton;
    private JButton cencelButton;
    private JPanel MainPanel;
    private JPanel DOB;
    private JTable tbluser;
    private JButton deleteButton;
    private JButton backButton;
    private DatePicker dobPicker;
    private JComboBox COG;
    private JPanel panelData;
    private JPanel panelControls;
    DefaultTableModel tableModel = new DefaultTableModel();
    private KeyValue[] items ={
            new KeyValue(0,""),
            new KeyValue(1,"1"),
            new KeyValue(2,"2"),
            new KeyValue(3,"3"),
            new KeyValue(4,"4"),
            new KeyValue(5,"Null"),
    };
    private void initializedComboBox(){
        for(KeyValue kw:items) COG.addItem(kw);
    }
    private void initializeDatePicker() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        // Add the date picker to your JPanel
        dobPicker = new DatePicker(dateSettings);
        DOB.add(dobPicker);
        dobPicker.setDate(LocalDate.now().minusYears(18));
    }

    private void initializedTable(){
        // Add columns to the table model
//        tableModel.addColumn("ID");
        tableModel.addColumn("Student ID");
        tableModel.addColumn("Student Name");
        tableModel.addColumn("Department");
        tableModel.addColumn("Year");
        tableModel.addColumn("Date of birth");
        // Load data from the database
        loadAddUser(tableModel);
        tbluser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //set data to JTable
        tbluser.setModel(tableModel);
        tbluser.setRowHeight(30);
        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(tbluser);
        scrollPane.setPreferredSize(new Dimension(tbluser.getPreferredScrollableViewportSize().width,300));
        JPanel panelAddUser = new JPanel(new BorderLayout());
        panelAddUser.add(scrollPane,BorderLayout.CENTER);
        panelData.add(panelAddUser);
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
    // get key from select item
    private int getKeyFromSelectedItem(@NotNull JComboBox<KeyValue> comboBox){
        KeyValue selectedItem = (KeyValue) comboBox.getSelectedItem();
        int selectedKey = 0;
        if (selectedItem != null) {
            selectedKey = selectedItem.getKey();
        }
        return selectedKey;
    }
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
    private void loadAddUserByCode(String code){
        try{
            Connection con = DBConnection.getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT code, user_name, couse, gender_id,date_of_birth")
                    .append(" FROM ")
                    .append(" adduser")
                    .append(" WHERE code = '").append(code).append("'");

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()){
                ID.setText(resultSet.getString("code"));
                ID.setEditable(false);
                Username.setText(resultSet.getString("user_name"));
                Couse.setText(resultSet.getString("couse"));
                setSelectedItemByKey(COG,resultSet.getInt("gender_id"));
                dobPicker.setDate(resultSet.getObject("date_of_birth",LocalDate.class));
            }
            con.close();
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(this,"Error loading data from database "+ex.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private int insertAddUser(String code, String user_name, String couse, int gender_id, LocalDate date_of_birth) {
        StringBuilder insertSQL = new StringBuilder();
        insertSQL.append("INSERT INTO adduser( code, user_name, couse, gender_id,date_of_birth)")
                .append("values(").append("'")
                .append(code).append("','")
                .append(user_name).append("','")
                .append(couse).append("',")
                .append(gender_id).append(",'")
                .append(date_of_birth).append("')");
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
    private void updateAddUser(String code, String book_name, String genre, int book_id, LocalDate date_of_day) {
        StringBuilder updateSQL = new StringBuilder();
        updateSQL.append("UPDATE adduser SET")
                .append(" user_name='").append(book_name).append("',")
                .append(" couse='").append(genre).append("',")
                .append(" gender_id=").append(book_id).append(",")
                .append(" date_of_birth='").append(date_of_day).append("'")
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
    private void deleteAddUser(String code) {
        StringBuilder deleteSQL = new StringBuilder();
        deleteSQL.append("DELETE FROM adduser")
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

    public AddStudent() {
        initializedTable();
        initializedComboBox();
        initializeDatePicker();
        setTitle("View user ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(MainPanel);
        ID.setEditable(false);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        cencelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearControls(panelControls);
                loadAddUser(tableModel);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ID.getText().equals("")) {
                    String code = AddUserCode.generateAddUserCode();
                    String user_name = Username.getText();
                    String couse = Couse.getText();
                    int gender_id = getKeyFromSelectedItem(COG);
                    LocalDate date_of_birth = dobPicker.getDate();
                    int recordID = insertAddUser(code, user_name, couse, gender_id, date_of_birth);
                    clearControls(panelControls);
                    loadAddUser(tableModel);
                }
                //loadStudentByID(recordID);
            }
        });
        tbluser.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    int selectedRow = tbluser.getSelectedRow();
                    if(selectedRow != -1){
                        Object selectedCode = tbluser.getValueAt(selectedRow,0);
                        loadAddUserByCode(selectedCode.toString());
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ID.getText().equals("")){
                    String code = ID.getText();
                    String user_name = Username.getText();
                    String couse = Couse.getText();
                    int gender_id = getKeyFromSelectedItem(COG);
                    LocalDate date_of_birth = dobPicker.getDate();
                    updateAddUser(code, user_name, couse, gender_id, date_of_birth);
                    clearControls(panelControls);
                    loadAddUser(tableModel);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ID.getText().equals("")){
                    String code = ID.getText();
                    deleteAddUser(code);
                    clearControls(panelControls);
                    loadAddUser(tableModel);
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}