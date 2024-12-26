package org.triBhaskar.coin.service.redis;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.triBhaskar.coin.exception.RedisOperationException;
import org.triBhaskar.coin.model.CoinData;
import org.triBhaskar.coin.model.CoinInfo;
import org.triBhaskar.coin.model.CoinPriceHistory;
import org.triBhaskar.coin.model.Coins;
import org.triBhaskar.coin.service.api.CoinApiServiceImpl;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;
import redis.clients.jedis.timeseries.DuplicatePolicy;
import redis.clients.jedis.timeseries.TSCreateParams;
import redis.clients.jedis.timeseries.TSElement;
import redis.clients.jedis.timeseries.TSInfo;

import java.util.List;
import java.util.Map;

@Service
public class CoinRedisServiceImpl implements CoinRedisService {

    private static final Logger log = LoggerFactory.getLogger(CoinRedisServiceImpl.class);
    private static final String REDIS_KEY_COINS = "coins";

    private final JedisPooled jedisClient;
    private final Gson gson;

    public CoinRedisServiceImpl(JedisPooled jedisClient, Gson gson) {
        this.jedisClient = jedisClient;
        this.gson = gson;
    }
    @Override
    public void storeCoins(Coins coins) {
        log.info("Storing coins data to Redis");
        try {
            jedisClient.jsonSet(REDIS_KEY_COINS, gson.toJson(coins));
            log.info("Coins data stored successfully");
        } catch (Exception e) {
            log.error("Error storing coins data to Redis", e);
            throw new RedisOperationException("Failed to store coins data", e);
        }
    }

    @Override
    public void storeCoinHistory(String symbol, String timePeriod, CoinPriceHistory history) {
        log.info("Storing coin history for symbol: {} and period: {}", symbol, timePeriod);
        String key = buildHistoryKey(symbol, timePeriod);

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
    public List<CoinInfo> getAllCoins() {
        log.info("Fetching all coins from Redis");
        try {
            CoinData coinData = jedisClient.jsonGet(
                    REDIS_KEY_COINS,
                    CoinData.class,
                    new Path("data")
            );
            return coinData.getCoins();
        } catch (Exception e) {
            log.error("Error fetching coins from Redis", e);
            throw new RedisOperationException("Failed to fetch coins", e);
        }
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

    private String buildHistoryKey(String symbol, String timePeriod) {
        return String.format("%s:%s", symbol, timePeriod);
    }
}
