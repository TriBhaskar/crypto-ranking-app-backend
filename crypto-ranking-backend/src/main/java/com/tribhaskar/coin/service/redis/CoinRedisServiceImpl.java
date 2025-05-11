package com.tribhaskar.coin.service.redis;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.tribhaskar.coin.model.CoinPriceHistory;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.timeseries.DuplicatePolicy;
import redis.clients.jedis.timeseries.TSCreateParams;
import redis.clients.jedis.timeseries.TSElement;
import redis.clients.jedis.timeseries.TSInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CoinRedisServiceImpl implements CoinRedisService {

    private static final Logger log = LoggerFactory.getLogger(CoinRedisServiceImpl.class);
    private static final String REDIS_KEY_COIN_HISTORY = "coin:history";

    private final JedisPooled jedisClient;
    private final Gson gson;

    public CoinRedisServiceImpl(JedisPooled jedisClient, Gson gson) {
        this.jedisClient = jedisClient;
        this.gson = gson;
    }
    @Override
    public void storeCoinHistory(String symbol, String timePeriod, CoinPriceHistory history) {
        log.info("Storing coin history for symbol: {} and period: {}", symbol, timePeriod);
        String key = buildHistoryKey(symbol, timePeriod);

        // Add to parent key set to track all history keys
        try {
            jedisClient.sadd(REDIS_KEY_COIN_HISTORY, key);
        } catch (Exception e) {
            log.error("Error adding to parent key set", e);
        }

        history.getData().getHistory().forEach(rate -> {
            try {
                jedisClient.tsAdd(
                        key,
                        rate.getTimestamp(),
                        rate.getPrice(),
                        new TSCreateParams()
                                .uncompressed()
                                .duplicatePolicy(DuplicatePolicy.LAST)
                );
            } catch (Exception e) {
                log.error("Error storing history data point", e);
            }
        });
    }

    @Override
    public List<TSElement> getCoinHistory(String symbol, String timePeriod) {
        String key = buildHistoryKey(symbol, timePeriod);
        TSInfo tsInfo = jedisClient.tsInfo(key);
        Map<String, Object> properties = tsInfo.getProperties();

        long firstTimestamp = Long.parseLong(properties.get("firstTimestamp").toString());
        long lastTimestamp = Long.parseLong(properties.get("lastTimestamp").toString());

        return jedisClient.tsRange(key, firstTimestamp, lastTimestamp);
    }

    @Override
    public Long getLastUpdateTime(String symbol, String period) {
        String key = buildHistoryKey(symbol, period);
        TSInfo tsInfo = jedisClient.tsInfo(key);
        Map<String, Object> properties = tsInfo.getProperties();
        return Long.parseLong(properties.get("lastTimestamp").toString());
    }

    private String buildHistoryKey(String symbol, String timePeriod) {
        return String.format("%s:%s:%s",REDIS_KEY_COIN_HISTORY, symbol, timePeriod);
    }

    @Override
    public boolean hasCoinHistory() {
        try {
            return !jedisClient.smembers(REDIS_KEY_COIN_HISTORY).isEmpty();
        } catch (Exception e) {
            log.error("Error checking coin history existence", e);
            return false;
        }
    }

    // Helper method to get all history keys
    @Override
    public Set<String> getAllHistoryKeys() {
        try {
            return jedisClient.smembers(REDIS_KEY_COIN_HISTORY);
        } catch (Exception e) {
            log.error("Error getting history keys", e);
            return Collections.emptySet();
        }
    }
}
