package org.triBhaskar.model;

import lombok.Data;


public class CoinPriceHistory {
    private String status;
    private CoinPriceHistoryData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CoinPriceHistoryData getData() {
        return data;
    }

    public void setData(CoinPriceHistoryData data) {
        this.data = data;
    }
}
