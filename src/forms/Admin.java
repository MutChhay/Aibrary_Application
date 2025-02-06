package src.forms;

import src.cls.AuthenticationState;
import src.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;

//import static jdk.internal.net.http.common.Utils.close;

public class Admin extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton cencelButton;
    private JButton backButton;

    public Admin (){
        setContentPane(panel1);
        setTitle("Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);

        cencelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
           }
        });
       loginButton.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               src.cls.User user=new src.cls.User();
               try {
                   String username=textField1.getText();
                   String password=String.valueOf(passwordField1.getPassword());
                   Connection connection= DBConnection.getConnection();
                   Statement statement=connection.createStatement();
                   String query="SELECT* FROM User WHERE Username=? AND Password=?";
                   PreparedStatement preparedStatement=connection.prepareStatement(query);
                   preparedStatement.setString(1,username);
                   preparedStatement.setString(2,password);
                   ResultSet resultSet=preparedStatement.executeQuery();
                   if (resultSet.next()){
                       user.setUsername(resultSet.getString("username"));
                       user.setRoleID(resultSet.getInt("role_id"));
                       AuthenticationState.setAuthenticated(true);
//                       LibrainMenu adminmenu =new LibrainMenu();
//                       adminmenu.setVisible(true);
                       AdminMenu adminMenu = new AdminMenu();
                       adminMenu.setVisible(true);
                       dispose();
                   }
                   else {
                       AuthenticationState.setAuthenticated(false);
//                       lblMsg.setText("Login Failed");
//                       lblMsg.setForeground(Color.red);
                   }
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
           }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForms loginForms = new LoginForms(null);
                loginForms.setVisible(true);
                dispose();
            }
        });
    }
}
