package yu.edu.funproject;

import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;

public class Brokerage {
    private double principle;
    private double totalCurrentHoldingsInvestment;
    private double totalCurrentValue;
    private double totalGrowthValue;
    private double totalSaleValue;
    public Map<String, Stock> boughtStocks;
    public Map<String, Stock> soldStocks;
    public Map<Stock, String> mapToPrintStocks;
    public Stack<Command> commandStack;
    private List<Stock> stocks;
    public Brokerage(double principle){
        this.principle = principle;
        this.stocks = new ArrayList<>();
        boughtStocks = new HashMap<>();
        soldStocks = new HashMap<>();
        commandStack = new Stack<>();
        mapToPrintStocks = new HashMap<>();
    }
    public void purchase(String symbol, double purchasePrice, double quantity){
        Stock stock = boughtStocks.get(symbol);
        if(stock != null){
            //this stock has been bought already. Buy more.
            stock.additionalPurchase(purchasePrice, quantity);
            boughtStocks.put(symbol, stock);
        }
        if(stock == null) {
            //this stock has not yet been bought. Buy anew.
            stock = new Stock(symbol, purchasePrice, quantity);
            stocks.add(stock);
            boughtStocks.put(symbol, stock);
        }
    }
    public void sellAllShares(String symbol, double salePrice){
        Stock stock = boughtStocks.get(symbol);
        if(stock != null){
            //stock is already bought. Sell all shares.
            //create undo function
            Function<String, Boolean> undo = (String s) -> {
                //needs work
                Stock undoStock = stock;
                undoStock.setHold(true);
                boughtStocks.put(symbol, stock);
                return true;
            };
            //push command onto stack
            commandStack.push(new Command(symbol, undo));
            stock.sellAllShares(salePrice);
            Stock toSell = soldStocks.get(symbol);
            if(toSell != null){
                //this stock has previously been sold. We need to combine the two sold stocks for proper output.
                stock.combineStocks(toSell);
                //remove old sold stock from list of stocks
                stocks.remove(toSell);
            }
            soldStocks.put(symbol, stock);
            //set old val in bought BTree to null
            boughtStocks.put(symbol, null);
        }
        if(stock == null) {
            //stock is not bought; can't be sold
            throw new IllegalArgumentException("Currently not holding this stock.");
        }
    }
    public void sellSomeShares(String symbol, double salePrice, double quantity){
        Stock stock = boughtStocks.get(symbol);
        if(stock != null){
            //stock was bought. Now sell some shares.
            if(quantity == stock.getQuantity()){
                //if quantity is equal to all bought shares, sell all shares.
                sellAllShares(symbol, salePrice);
                return;
            }
            Stock toSell = soldStocks.get(symbol);
            if(toSell == null){
                //this stock has not been sold yet. Make a new stock to add to soldStocks.
                toSell = stock;
                //set quantity of sold stocks to zero
                toSell.setQuantityToZero();
            }
            toSell.sellSomeShares(quantity, salePrice);
            //edit the bought stock
            stock.subtractFromQuantity(quantity);
            soldStocks.put(symbol, toSell);
            stocks.add(toSell);
        }
        if(stock == null) {
            //stock is not bought; can't be sold
            System.out.println("Currently not holding this stock.");
        }
    }
    public void undo(){
        if(commandStack.size() == 0){
            throw new IllegalStateException("No commands to undo.");
        }
        commandStack.pop().undo();
    }
    private void collectTotals(){
        for(Stock stock : stocks){
            if(stock.isHold()){
                totalCurrentHoldingsInvestment += stock.getTotalInvestment();
            }
            totalCurrentValue += stock.getTotalCurrentValue();
            totalGrowthValue += stock.getGrowthValue();
            totalSaleValue += stock.getSaleValue();
        }
    }
    public void sortBySymbol(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return o1.getSymbol().compareTo(o2.getSymbol());
            }
        });
    }
    public void sortByStatus(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return o1.statusToString().compareTo(o2.statusToString());
            }
        });
    }
    public void sortByQuantity(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getQuantity() -o2.getQuantity());
            }
        });
    }
    public void sortByCurrentPrice(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getCurrentPrice() - o2.getCurrentPrice());
            }
        });
    }
    public void sortByPurchasePrice(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getPurchasePrice() - o2.getPurchasePrice());
            }
        });
    }
    public void sortBySalePrice(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getSalePrice() - o2.getSalePrice());
            }
        });
    }
    public void sortByTotalInvestment(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getTotalInvestment() - o2.getTotalInvestment());
            }
        });
    }
    public void sortByCurrentValue(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getTotalCurrentValue() - o2.getTotalCurrentValue());
            }
        });
    }
    public void sortBySaleValue(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getSaleValue() - o2.getSaleValue());
            }
        });
    }
    public void sortByGrowthValue(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getGrowthValue() - o2.getGrowthValue());
            }
        });
    }
    public void sortByGrowthPercentage(){
        Collections.sort(stocks, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return (int) (o1.getGrowthPercentage() - o2.getGrowthPercentage());
            }
        });
    }
    public void reverseOutput(){
        Collections.reverse(stocks);
    }
    public String sortedOutput(){
        String rtn = "";
        rtn += firstLineOutput();
        for(Stock s: stocks){
            rtn += mapToPrintStocks.get(s);
            rtn += "\n";
        }
        rtn += bottomLineOutput();
        return rtn;
    }
    public String firstLineOutput(){
        String rtn = "";
        rtn += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
        rtn += String.format("| %-10s | %-10s | %10s | %16s | %16s | %13s | %18s | %16s | %16s | %16s | %18s |",
                "Symbol", "Status", "Quantity", "Current Price", "Purchase Price", "Sale Price", "Total Investment", "Current Value", "Sale Value", "Growth Value", "Growth Percentage");
        rtn += "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
        return rtn;
    }
    public String bottomLineOutput(){
        NumberFormat nf =
                NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat percent = NumberFormat.getPercentInstance(Locale.US);
        percent.setMinimumFractionDigits(2);
        String rtn = "";
        rtn += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
        //print bottom line
        rtn += String.format("| %-18s %-10s %35s %123s\n| %18s %-25s %22s %10s %-25s %25s %54s\n| %18s %-25s %22s %10s %-25s %24s %54s\n| %18s %-25s %20s %9s %-25s %20s %53s",
                "TOTALS...", "Principle : ", nf.format(principle), "|",
                "", "Account Total Value : ",  nf.format(getTotalAccountValue()), "", "Current Holdings Total Value : ", nf.format(getCurrentHoldingsValue()), "|",
                "", "Account Growth Value : ", nf.format(getTotalAccountValue() - principle), "", "Current Holdings Growth Value : ", nf.format(getCurrentHoldingsValue() - getTotalCurrentHoldingsInvestment()), "|",
                "", "Account Growth Percentage : ", percent.format(getPrincipleGrowthPercentage()), "", "Current Holdings Growth Percentage : ", percent.format(getHoldingsGrowthPercentage()), "|");
        rtn += "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
        return rtn;
    }
    public String initialOutputForApp(){
        String rtn = "";
        collectTotals();
        rtn += firstLineOutput();
        for(Stock s: stocks){
            mapToPrintStocks.put(s, s.printForApp());
            rtn += s.printForApp();
            rtn += "\n";
        }
        rtn += bottomLineOutput();
        return rtn;
    }
    private double getTotalCurrentHoldingsInvestment(){
        return totalCurrentHoldingsInvestment;
    }
    private double getCurrentHoldingsValue(){
        return totalCurrentValue;
    }
    private double getTotalGrowthValue(){
        return totalGrowthValue;
    }
    private double getTotalSaleValue(){
        return totalSaleValue;
    }
    private double getTotalAccountValue(){
        return (getCurrentHoldingsValue() + getTotalSaleValue());
    }
    private double getHoldingsGrowthPercentage(){
        return (getCurrentHoldingsValue()/getTotalCurrentHoldingsInvestment() - 1);
    }
    private double getPrincipleGrowthPercentage(){
        return (getTotalAccountValue()/principle - 1);
    }
}
