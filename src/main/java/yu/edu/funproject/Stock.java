package yu.edu.funproject;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Stock {
    private String symbol;
    private double purchasePrice;
    private double salePrice;
    private double quantity;
    private boolean hold;
    private Map<Double, Double> purchaseMap;
    private Map<Double, Double> saleMap;
    //add

    public Stock(String symbol, double purchasePrice, double quantity){
        this.hold = true;
        this.symbol = symbol;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        purchaseMap = new HashMap<>();
        purchaseMap.put(quantity, purchasePrice);
        saleMap = new HashMap<>();
    }
    public boolean isHold() {
        return hold;
    }
    public String statusToString(){
        if(isHold()){
            return "BOUGHT";
        }
        return "SOLD";
    }
    public String getSymbol(){
        return this.symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }
    public double getCostBasis(){
        double total = 0.0;
        for(Map.Entry<Double, Double> entry: purchaseMap.entrySet()){
            total = total + (entry.getKey() * entry.getValue());
        }
        return total/getQuantity();
    }

    public double getSalePrice() {
        double total = 0.0;
        for(Map.Entry<Double, Double> entry: saleMap.entrySet()){
            total = total + (entry.getKey() * entry.getValue());
        }
        return total/getQuantity();
    }

    public double getTotalInvestment(){
        return getQuantity() * getCostBasis();
    }
    public double getTotalCurrentValue(){
        if(!isHold()) return 0;
        return getQuantity() * getCurrentPrice();
    }
    public double getGrowthPercentage(){
        double numerator = 0;
        if(isHold()) numerator = getTotalCurrentValue();
        if(!isHold()) numerator = getSaleValue();
        return (numerator/getTotalInvestment() - 1);
    }
    public double getGrowthValue(){
        double total = 0;
        if(isHold()) total = getTotalCurrentValue();
        if(!isHold()) total = getSaleValue();
        return total - getTotalInvestment();
    }
    public double getSaleValue(){
        if(isHold()) return 0;
        return getSalePrice() * getQuantity();
    }
    public double getCurrentPrice(){
        if(!isHold()){
            return 0;
        }
        try {
            return stockPrice();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void additionalPurchase(double purchasePrice, double quantity, boolean isUndo){
        if(isUndo) {
            adjustSaleMap();
            saleMap.put(this.quantity, 0.0);
            hold = true;
        }
        this.quantity += quantity;
        purchaseMap.put(quantity, purchasePrice);
    }
    public void sellAllShares(double salePrice) {
        saleMap.put(quantity, salePrice);
        hold = false;
    }
    public void sellSomeShares(double quantity, double salePrice, boolean isUndo){
        //saleMap.put(quantity, salePrice);
        adjustPurchaseMap();
        if(isUndo) purchaseMap.put(quantity, -1 * salePrice);
        if(!isUndo) purchaseMap.put(quantity, -1 * getCostBasis());
        this.quantity -= quantity;
        //purchaseMap.put(this.quantity, purchasePrice);
        //hold = false;
    }
    private void adjustPurchaseMap(){
        double costBasis = getCostBasis();
        for(Map.Entry<Double, Double> entry: purchaseMap.entrySet()){
            purchaseMap.put(entry.getKey(), 0.0);
        }
        purchaseMap.put(getQuantity(), costBasis);
    }
    private void adjustSaleMap(){
        double costBasis = getSalePrice();
        for(Map.Entry<Double, Double> entry: saleMap.entrySet()){
            saleMap.put(entry.getKey(), 0.0);
        }
        saleMap.put(getQuantity(), costBasis);
    }

    public void combineStocks(Stock otherStock){
        if(!getSymbol().equals(otherStock.getSymbol()) || !statusToString().equals(otherStock.statusToString())){
            throw new IllegalArgumentException();
        }
        otherStock.adjustPurchaseMap();
        otherStock.adjustSaleMap();
        quantity += otherStock.getQuantity();
        purchaseMap.put(otherStock.getQuantity(), otherStock.getPurchasePrice());
        saleMap.put(otherStock.getQuantity(), otherStock.getSalePrice());
    }
    public void subtractFromQuantity(double quantity){
        if(quantity > getQuantity()){
            throw new IllegalArgumentException();
        }
        this.quantity -= quantity;
    }
    public void setQuantityToZero(){
        quantity = 0;
    }
    public void setHold(boolean hold){
        this.hold = hold;
    }
    public double stockPrice() throws IOException {
        String price = "";
        URL url = new URL("https://query1.finance.yahoo.com/v8/finance/chart/" + symbol + "?region=US&lang=en-US&includePrePost=false&interval=2m&useYfid=true&range=1d&corsDomain=finance.yahoo.com&.tsrc=finance");
        URLConnection urlConnection = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConnection.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inStream);
        String line = bufferedReader.readLine();
        while (line != null){
            if(line.contains("regularMarketPrice")){
                int target = line.indexOf("regularMarketPrice");
                int dec = line.indexOf(".", target);
                int start = dec;
                while(line.charAt(start) != ':'){
                    start--;
                }
                while(line.charAt(dec) != ','){
                    dec++;
                }
                price = line.substring(start + 1, dec);
                break;
            }
            line = bufferedReader.readLine();
        }
        return Double.parseDouble(price);
    }
    public String printForApp(){
        NumberFormat nf =
                NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat percent = NumberFormat.getPercentInstance(Locale.US);
        percent.setMinimumFractionDigits(2);
        return String.format("| %-10s | %-10s | %10.2f | %16s | %16s | %13s | %18s | %16s | %16s | %16s | %18s |",
                getSymbol(), statusToString(), getQuantity(), nf.format(getCurrentPrice()), nf.format(getCostBasis()), nf.format(getSalePrice()), nf.format(getTotalInvestment()), nf.format(getTotalCurrentValue()), nf.format(getSaleValue()), nf.format(getGrowthValue()), percent.format(getGrowthPercentage()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return hold == stock.hold && Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, hold);
    }
}
