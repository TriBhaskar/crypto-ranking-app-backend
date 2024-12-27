package org.triBhaskar.coin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "coins")
public class Coin {

    @Id
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "listed_at")
    private Integer listedAt;

    @Column(name = "tier")
    private Integer tier;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "low_volume")
    private Boolean lowVolume;

    @Column(name = "coinranking_url")
    private String coinrankingUrl;

    @Column(name = "btc_price")
    private String btcPrice;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getListedAt() {
        return listedAt;
    }

    public void setListedAt(Integer listedAt) {
        this.listedAt = listedAt;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getLowVolume() {
        return lowVolume;
    }

    public void setLowVolume(Boolean lowVolume) {
        this.lowVolume = lowVolume;
    }

    public String getCoinrankingUrl() {
        return coinrankingUrl;
    }

    public void setCoinrankingUrl(String coinrankingUrl) {
        this.coinrankingUrl = coinrankingUrl;
    }

    public String getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(String btcPrice) {
        this.btcPrice = btcPrice;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "uuid='" + uuid + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", listedAt=" + listedAt +
                ", tier=" + tier +
                ", rank=" + rank +
                ", lowVolume=" + lowVolume +
                ", coinrankingUrl='" + coinrankingUrl + '\'' +
                ", btcPrice=" + btcPrice +
                '}';
    }
}
