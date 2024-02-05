package yu.edu.funproject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private Brokerage brokerage;
    private JPanel panelMain;
    private JButton entranceButton;
    private JTextField principle;
    private JLabel entranceMsg;

    public App() {
        JFrame frame = new JFrame();
        frame.setContentPane(this.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(720, 420);
        entranceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    brokerage = new Brokerage(Double.parseDouble(principle.getText()));
                    MenuPage menuPage = new MenuPage(brokerage, frame);
                }
                catch (IllegalArgumentException a){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter a double.");
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
