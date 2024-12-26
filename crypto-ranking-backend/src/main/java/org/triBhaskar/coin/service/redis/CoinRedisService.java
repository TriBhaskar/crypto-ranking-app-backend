package org.triBhaskar.coin.service.redis;

import org.triBhaskar.coin.model.CoinInfo;
import org.triBhaskar.coin.model.CoinPriceHistory;
import org.triBhaskar.coin.model.Coins;
import redis.clients.jedis.timeseries.TSElement;

import java.util.List;

public interface CoinRedisService {
    void storeCoins(Coins coins);
    void storeCoinHistory(String symbol, String timePeriod, CoinPriceHistory history);
    List<CoinInfo> getAllCoins();
    List<TSElement> getCoinHistory(String symbol, String timePeriod);
}
