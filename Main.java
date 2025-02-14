import src.cls.AuthenticationState;
import src.db.DBConnection;
import src.forms.*;
import src.forms.ReturnBook;
import src.forms.IssueBook;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
            System.out.println("Hello world!");
            Connection connection = DBConnection.getConnection();
            if (connection != null) {
                System.out.println("Connection successfully.");
            } else {
                System.out.println("Cannot connect to Database.");
            }
              Admin admin = new Admin();
            LoginForms loginForms = new LoginForms(admin);
            loginForms.setVisible(true);
            if (AuthenticationState.isAuthenticated()) {
                admin.setVisible(true);
            }

//            AddBook addBook = new AddBook();
//            addBook.setVisible(true);

//        IssueBook issueBook = new IssueBook();
//        issueBook.setVisible(true);
//            AdminMenu adminMenu = new AdminMenu();
//            adminMenu.setVisible(true);

    }
}