package com.curlylogic.com.stockwall.database;

/**
 * Created by jon on 12/31/17.
 */
public class StockQuote {
    private String symbol;
    private float price;
    private double volume;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String timestamp;

    public String toString(){
        return  getTimestamp() + "," +
                getSymbol() + "," +
                getPrice() + "," +
                getVolume();
    }


}
