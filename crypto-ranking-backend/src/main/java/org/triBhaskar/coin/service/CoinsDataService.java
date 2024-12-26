package org.triBhaskar.coin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.triBhaskar.coin.model.*;
import org.triBhaskar.coin.service.api.CoinApiService;
import org.triBhaskar.coin.service.redis.CoinRedisService;
import redis.clients.jedis.timeseries.TSElement;

import java.util.List;

@Service
public class CoinsDataService {

    private static final Logger log = LoggerFactory.getLogger(CoinsDataService.class);
    public static final List<String> TIME_PERIODS = List.of("24h", "7d", "30d", "3m", "1y", "3y");

    private final CoinApiService apiService;
    private final CoinRedisService redisService;

    public CoinsDataService(CoinApiService apiService, CoinRedisService redisService) {
        this.apiService = apiService;
        this.redisService = redisService;
    }

    //@Scheduled(fixedRate = 300000) // 5 minutes
    public void syncCoinsData() {
        try {
            log.info("Starting coins sync");
            Coins coins = apiService.fetchCoins();
            redisService.storeCoins(coins);
            log.info("Coins sync completed");
        } catch (Exception e) {
            log.error("Error during coins sync", e);
        }
    }

    //@Scheduled(fixedRate = 3600000) // 1 hour
    public void syncCoinHistory() {
        log.info("Starting coin history sync");
        List<CoinInfo> coins = redisService.getAllCoins();

        coins.forEach(coin -> {
            TIME_PERIODS.forEach(period -> {
                try {
                    CoinPriceHistory history = apiService.fetchCoinHistory(coin.getUuid(), period);
                    redisService.storeCoinHistory(coin.getSymbol(), period, history);
                    Thread.sleep(200); // Rate limiting
                } catch (Exception e) {
                    log.error("Error syncing history for coin: {} period: {}", coin.getSymbol(), period, e);
                }
            });
        });
        log.info("Coin history sync completed");
    }

    // Public methods for fetching data
    public List<CoinInfo> getAllCoins() {
        return redisService.getAllCoins();
    }

    public List<TSElement> getCoinHistory(String symbol, String timePeriod) {
        return redisService.getCoinHistory(symbol, timePeriod);
    }

}
