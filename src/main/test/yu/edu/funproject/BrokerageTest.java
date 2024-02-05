package yu.edu.funproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrokerageTest {
    private Brokerage brokerage;
    @BeforeEach
    public void init(){
        brokerage = new Brokerage(2300.0);
    }
    @Test
    public void simpleOutput(){
        brokerage.purchase("NKE",  100.0, 7);
        brokerage.purchase("AAPL", 200.0, 5);
        brokerage.purchase("NVDA", 300.0, 2);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void doubleBuyOnOneStock(){
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.purchase("NKE", 100.0, 8);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void buySellThenAnotherBuySameStock(){
        //should show a sale of 7 shares and a buy of 8 shares
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.sellAllShares("NKE", 115.0);
        brokerage.purchase("NKE", 90.0, 8);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void buySellBuySellSameStock(){
        //should show one sold stock with sale price of 115, quantity 15
        brokerage.purchase("NKE",  100.0, 7);
        brokerage.sellAllShares("NKE", 115.0);
        brokerage.purchase("NKE", 90.0, 8);
        brokerage.sellAllShares("NKE", 115.0);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void sellAllSharesOneStock(){
        //should show one sold stock
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.sellAllShares("NKE", 115.0);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void sellSomeSharesOneStock(){
        //should show one bought one sold stock
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.sellSomeShares("NKE", 115.0, 2);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void undoOnePurchaseForNoOutputOfThatStock(){
        brokerage.purchase("GS",  300.0, 7);
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.undo();
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void undoOneSellForOutputOfOnlyOnePurchase(){
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.sellAllShares("NKE", 115.0);
        brokerage.undo();
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void buyOneStockBuySomeMoreThenUndo(){
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.purchase("NKE", 90, 2);
        brokerage.undo();
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void buyOneStockSellSomeThenUndo(){
        brokerage.purchase("NKE", 100.0, 7);
        brokerage.sellSomeShares("NKE", 110.0, 2);
        brokerage.undo();
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void sellSomeSharesOfMultipleStocks(){
        brokerage.purchase("NKE",  100.0, 7);
        brokerage.purchase("AAPL", 200.0, 5);
        brokerage.purchase("NVDA", 300.0, 2);
        brokerage.purchase("GS",  300.0, 7);
        brokerage.purchase("AMZN", 120.0, 5);
        brokerage.purchase("BA", 220.0, 2);
        brokerage.sellSomeShares("NKE", 110.0, 2);
        brokerage.sellSomeShares("GS", 250, 1);
        brokerage.sellSomeShares("BA", 220, 1);
        System.out.println(brokerage.initialOutputForApp());
    }
    @Test
    public void sortBySymbol(){
        brokerage.purchase("NKE",  100.0, 7);
        brokerage.purchase("AAPL", 200.0, 5);
        brokerage.purchase("NVDA", 300.0, 2);
        brokerage.purchase("GS",  300.0, 7);
        brokerage.purchase("AMZN", 120.0, 5);
        brokerage.purchase("BA", 220.0, 2);
        System.out.println(brokerage.initialOutputForApp());
        brokerage.sortBySymbol();
        System.out.println(brokerage.sortedOutput());
    }
}
