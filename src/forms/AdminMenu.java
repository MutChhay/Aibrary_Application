package src.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame{
    private JButton addLibrarianButton;
    private JButton viewReportButton;
    private JButton exitButton;
    private JPanel Mainpanel;

    public AdminMenu() {
        super("Admin Menu");
        setContentPane(Mainpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950,600));
        setLocationRelativeTo(null);
        addLibrarianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            AddLibrain addLibrain = new AddLibrain();
            addLibrain.setVisible(true);
            dispose();

            }
        });
        viewReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminViewReport adminViewReport = new AdminViewReport();
                adminViewReport.setVisible(true);
                dispose();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
