package com.tribhaskar.coin.model;


//@AllArgsConstructor
//@NoArgsConstructor
public class HistoryData {
    private String timestamp;
    private double value;

    public HistoryData(String s, double round) {
        timestamp = s;
        value = round;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
