package com.tribhaskar.coin.model;

import java.util.ArrayList;
import java.util.List;


public class CoinData {
    private CoinStats stats;
    private List<CoinInfo> coins = new ArrayList<>();

    public CoinStats getStats() {
        return stats;
    }

    public void setStats(CoinStats stats) {
        this.stats = stats;
    }

    public List<CoinInfo> getCoins() {
        return coins;
    }

    public void setCoins(List<CoinInfo> coins) {
        this.coins = coins;
    }
}
