package org.triBhaskar.coin.model;

import java.util.ArrayList;
import java.util.List;


public class CoinPriceHistoryData {
    private String change;
    private List<CoinPriceHistoryExchangeRate> history = new ArrayList<>();

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public List<CoinPriceHistoryExchangeRate> getHistory() {
        return history;
    }

    public void setHistory(List<CoinPriceHistoryExchangeRate> history) {
        this.history = history;
    }
}
