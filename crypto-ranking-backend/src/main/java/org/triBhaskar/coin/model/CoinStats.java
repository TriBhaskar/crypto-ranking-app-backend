package org.triBhaskar.coin.model;

import java.util.ArrayList;
import java.util.List;


public class CoinStats {
    private float total;
    private float referenceCurrencyRate;
    private float totalCoins;
    private float totalMarkets;
    private float totalExchanges;
    private String totalMarketCap;
    private String total24hVolume;
    private float btcDominance;
    private List<CoinInfo> bestCoins = new ArrayList<>();
    private List<CoinInfo> newestCoins = new ArrayList<>();

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getReferenceCurrencyRate() {
        return referenceCurrencyRate;
    }

    public void setReferenceCurrencyRate(float referenceCurrencyRate) {
        this.referenceCurrencyRate = referenceCurrencyRate;
    }

    public float getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(float totalCoins) {
        this.totalCoins = totalCoins;
    }

    public float getTotalMarkets() {
        return totalMarkets;
    }

    public void setTotalMarkets(float totalMarkets) {
        this.totalMarkets = totalMarkets;
    }

    public float getTotalExchanges() {
        return totalExchanges;
    }

    public void setTotalExchanges(float totalExchanges) {
        this.totalExchanges = totalExchanges;
    }

    public String getTotalMarketCap() {
        return totalMarketCap;
    }

    public void setTotalMarketCap(String totalMarketCap) {
        this.totalMarketCap = totalMarketCap;
    }

    public String getTotal24hVolume() {
        return total24hVolume;
    }

    public void setTotal24hVolume(String total24hVolume) {
        this.total24hVolume = total24hVolume;
    }

    public float getBtcDominance() {
        return btcDominance;
    }

    public void setBtcDominance(float btcDominance) {
        this.btcDominance = btcDominance;
    }

    public List<CoinInfo> getBestCoins() {
        return bestCoins;
    }

    public void setBestCoins(List<CoinInfo> bestCoins) {
        this.bestCoins = bestCoins;
    }

    public List<CoinInfo> getNewestCoins() {
        return newestCoins;
    }

    public void setNewestCoins(List<CoinInfo> newestCoins) {
        this.newestCoins = newestCoins;
    }
}
