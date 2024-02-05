package yu.edu.funproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PrintPage {
    public PrintPage(Brokerage brokerage, JFrame frame){
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1372, 420);
        printedStocks.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        printedStocks.append(brokerage.initialOutputForApp());
        sorting.setSelectedIndex(0);
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printedStocks.setText("");
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Symbol")){
                    brokerage.sortBySymbol();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Status")){
                    brokerage.sortByStatus();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Quantity")){
                    brokerage.sortByQuantity();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Current Price")){
                    brokerage.sortByCurrentPrice();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Sale Price")){
                    brokerage.sortBySalePrice();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Purchase Price")){
                    brokerage.sortByPurchasePrice();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Initial Investment")){
                    brokerage.sortByTotalInvestment();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Current Value")){
                    brokerage.sortByCurrentValue();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Sale Value")){
                    brokerage.sortBySaleValue();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Growth $")){
                    brokerage.sortByGrowthValue();
                }
                if(sorting.getItemAt(sorting.getSelectedIndex()).equals("Growth %")){
                    brokerage.sortByGrowthPercentage();
                }
                printedStocks.append(brokerage.sortedOutput());
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Sorting by " + sorting.getItemAt(sorting.getSelectedIndex()) + ".");
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuPage menuPage = new MenuPage(brokerage, frame);
            }
        });
        reverseOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printedStocks.setText("");
                brokerage.reverseOutput();
                printedStocks.append(brokerage.sortedOutput());
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Reversing the order of the listed stocks");
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printedStocks.setText("");
                printedStocks.append(brokerage.initialOutputForApp());
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Refreshing the listed stocks");
            }
        });
    }
    private JPanel panel1;
    private JTextArea printedStocks;
    private JComboBox sorting;
    private JButton sortButton;
    private JButton back;
    private JLabel sortBy;
    private JScrollPane scroll;
    private JButton reverseOrder;
    private JButton refreshButton;
}
