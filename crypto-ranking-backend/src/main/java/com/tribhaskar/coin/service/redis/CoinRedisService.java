package com.tribhaskar.coin.service.redis;

import com.tribhaskar.coin.model.CoinPriceHistory;
import redis.clients.jedis.timeseries.TSElement;

import java.util.List;
import java.util.Set;

public interface CoinRedisService {
    void storeCoinHistory(String symbol, String timePeriod, CoinPriceHistory history);
    List<TSElement> getCoinHistory(String symbol, String timePeriod);
    Long getLastUpdateTime(String symbol, String period);
    boolean hasCoinHistory();
    Set<String> getAllHistoryKeys();
}
