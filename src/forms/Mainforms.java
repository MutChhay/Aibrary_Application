package src.forms;

import javax.swing.*;
import java.awt.*;

public class Mainforms extends JFrame{
    private JPanel panel1;

    public Mainforms(){
        setContentPane(panel1);
        setTitle("Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,600));
        setLocationRelativeTo(null);

    }
}
