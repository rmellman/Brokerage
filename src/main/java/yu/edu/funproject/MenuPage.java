package yu.edu.funproject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPage {
    private JPanel panel1;
    private JTextField symbol1;
    private JTextField quantity1;
    private JTextField price1;
    private JButton nextButton;
    private JButton addPurchaseButton;
    private JLabel purchaseMsg;
    private JLabel symbolMsg1;
    private JLabel quanMsg1;
    private JLabel priceMsg1;
    private JTextField symbol2;
    private JTextField quantity2;
    private JTextField price2;
    private JButton sellSomeSharesButton;
    private JTextField price3;
    private JButton sellAllSharesButton;
    private JLabel sellSomeMsg;
    private JLabel symbolMsg2;
    private JLabel quanMsg2;
    private JLabel priceMsg2;
    private JLabel sellAllMsg;
    private JLabel symbolMsg3;
    private JLabel priceMsg3;
    private JTextField symbol3;
    private JButton undo;
    private JButton back;
    private Brokerage brokerage;
    public MenuPage(Brokerage brokerage, JFrame frame){
        this.brokerage = brokerage;
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(720, 420);
        addPurchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    brokerage.purchase(symbol1.getText(),
                            Double.parseDouble(price1.getText()), Double.parseDouble(quantity1.getText()));
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You have bought " + quantity1.getText() + " shares of " + symbol1.getText() + " at $" + price1.getText() +".");
                    symbol1.setText("");
                    quantity1.setText("");
                    price1.setText("");
                }
                catch (Exception a){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter correctly formatted input.");
                }
            }
        });
        sellSomeSharesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    brokerage.sellSomeShares(symbol2.getText(),
                            Double.parseDouble(price2.getText()), Double.parseDouble(quantity2.getText()));
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You have sold " + quantity2.getText() + " shares of " + symbol2.getText() + " at $" + price2.getText() +".");
                    symbol2.setText("");
                    quantity2.setText("");
                    price2.setText("");
                }
                catch (Exception a){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter correctly formatted input.");
                }
            }
        });
        sellAllSharesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    brokerage.sellAllShares(symbol3.getText(), Double.parseDouble(price3.getText()));
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You have sold all shares of " + symbol3.getText() + " at $" + price3.getText() + ".");
                    symbol3.setText("");
                    price3.setText("");
                }
                catch (Exception s){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter correctly formatted input.");
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to sorting page
                //frame.removeAll();
                //panel1.removeAll();
                PrintPage printPage = new PrintPage(brokerage, frame);
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App app = new App();
            }
        });
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //undo
                try {
                    brokerage.undo();
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Action undone.");
                }
                catch (IllegalStateException a){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No actions to be undone. Nothing done.");
                }
            }
        });
    }
    public static void main(String[] args){
        JFrame frame = new JFrame();
        //frame.setContentPane(new MenuPage(new Brokerage(2000)).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
