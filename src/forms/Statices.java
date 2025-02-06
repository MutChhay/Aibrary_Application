package src.forms;

import javax.swing.*;
import java.awt.*;

public class Statices extends JFrame  {
    private JPanel MainPanel;

    public Statices() {
        setTitle("Delete Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(MainPanel);
        setMinimumSize(new Dimension(600, 400 ));
    }
}
