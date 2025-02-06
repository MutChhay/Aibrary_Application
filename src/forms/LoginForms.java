package src.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForms extends JDialog{
    private JPanel panel1;
    private JButton adminButton;
    private JButton userButton;
    private JButton exitButton;

    public LoginForms(Admin parent){
        super(parent);
        setContentPane(panel1);
        setTitle("Login");
        setMinimumSize(new Dimension(600,400));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin admin = new Admin();
                admin.setVisible(true);
                dispose();
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserForms userForms = new UserForms();
                userForms.setVisible(true);
                dispose();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

//    public void paintComponent(Graphics g){
//        ImageIcon image = new ImageIcon("library.png");
//        g.drawImage(image.getImage(),300,300,44,24 , null);
   }
}

