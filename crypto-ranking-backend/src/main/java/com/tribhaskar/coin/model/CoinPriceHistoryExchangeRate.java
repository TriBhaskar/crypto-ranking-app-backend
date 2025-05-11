package com.tribhaskar.coin.model;


public class CoinPriceHistoryExchangeRate {
    private double price;
    private long timestamp;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
