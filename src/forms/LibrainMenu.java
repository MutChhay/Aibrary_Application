package src.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LibrainMenu extends JFrame {
    private JButton borrowBooksButton;
    private JButton AddUserButton;
    private JButton returnBookButton;
    private JButton Exit;
    private JButton addBookButton;
    private JButton reportsButton;
    private JPanel MainPanel;

    public LibrainMenu() {
        super("Admin Menu");
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBook addBook = new AddBook();
                addBook.setVisible(true);
            }
        });
        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnBook returnBook = new ReturnBook();
                returnBook.setVisible(true);
            }
        });

        AddUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStudent addStudent = new AddStudent();
                addStudent.setVisible(true);
             }
        });
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        borrowBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IssueBook issueBook = new IssueBook();
                issueBook.setVisible(true);
            }
        });
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibrainViewReort librainViewReort = new LibrainViewReort();
                librainViewReort.setVisible(true);
            }
        });
    }
}
