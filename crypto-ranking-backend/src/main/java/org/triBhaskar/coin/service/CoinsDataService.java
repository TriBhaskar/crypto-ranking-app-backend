package org.triBhaskar.coin.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.triBhaskar.coin.entity.Coin;
import org.triBhaskar.coin.model.*;
import org.triBhaskar.coin.repository.CoinRepository;
import org.triBhaskar.coin.service.api.CoinApiService;
import org.triBhaskar.coin.service.redis.CoinRedisService;
import redis.clients.jedis.timeseries.TSElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CoinsDataService {

    private static final Logger log = LoggerFactory.getLogger(CoinsDataService.class);
    // Group time periods by update frequency
    private static final List<String> HIGH_FREQUENCY_PERIODS = List.of("24h", "7d");
    private static final List<String> MEDIUM_FREQUENCY_PERIODS = List.of("30d", "3m");
    private static final List<String> LOW_FREQUENCY_PERIODS = List.of("1y", "3y");

    private final CoinApiService apiService;
    private final CoinRedisService redisService;

    private final CoinRepository coinRepository;

    public CoinsDataService(CoinApiService apiService, CoinRedisService redisService, CoinRepository coinRepository) {
        this.apiService = apiService;
        this.redisService = redisService;
        this.coinRepository = coinRepository;
    }

    @PostConstruct
    public void initialize() {
        try {
            log.info("Checking if initialization is needed...");
            List<Coin> allCoin = coinRepository.findAll();
            boolean coinHistoryExists = redisService.hasCoinHistory();

            if (allCoin.isEmpty()) {
                log.info("No coins found in Redis. Starting initial data load...");
                // Approach 1: Load from API
                Coins coins = apiService.fetchCoins();
                Coin coinEntity = new Coin();
                coins.getData().getCoins().forEach(coin -> {
                    coinEntity.setUuid(coin.getUuid());
                    coinEntity.setSymbol(coin.getSymbol());
                    coinEntity.setName(coin.getName());
                    coinEntity.setColor(coin.getColor());
                    coinEntity.setIconUrl(coin.getIconUrl());
                    coinEntity.setListedAt(coin.getListedAt());
                    coinEntity.setTier(coin.getTier());
                    coinEntity.setRank(coin.getRank());
                    coinEntity.setLowVolume(coin.getLowVolume());
                    coinEntity.setCoinrankingUrl(coin.getCoinrankingUrl());
                    coinEntity.setBtcPrice(coin.getBtcPrice());
                    coinRepository.save(coinEntity);
                });
                log.info("Initial data load completed to database for {} coins", coins.getData().getCoins().size());
            }else {
                log.info("Initialization not needed");
            }
            if (!coinHistoryExists) {
                log.info("No coin history found in Redis. Starting initial history load...");

                List<Coin> coins = getCoinInfos();
                log.info("Initial history load completed for {} coins", coins.size());
            } else {
                log.info("Initialization not needed");

            }
        } catch (Exception e) {
            log.error("Error during initialization", e);
        }
    }

    private List<Coin> getCoinInfos() {
        List<Coin> allCoins = coinRepository.findAll();
        allCoins.forEach(coin -> {
            HIGH_FREQUENCY_PERIODS.forEach(period ->
                storeCoinHistory(coin, period)
            );
            MEDIUM_FREQUENCY_PERIODS.forEach(period -> storeCoinHistory(coin, period));
            LOW_FREQUENCY_PERIODS.forEach(period -> storeCoinHistory(coin, period));
            try {
                Thread.sleep(200); // Rate limiting between coins
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return allCoins;
    }

//    @Scheduled(fixedRate = 300000) // 5 minutes
    public void syncRecentData() {
        try {
            log.info("Starting recent data sync");
            List<Coin> coins = coinRepository.findAll();

            coins.forEach(coin ->
                HIGH_FREQUENCY_PERIODS.forEach(period ->
                    syncCoinHistory(coin, period)));
        } catch (Exception e) {
            log.error("Error during recent data sync", e);
        }
    }

//    @Scheduled(cron = "0 0 */6 * * *") // Every 6 hours
    public void syncMediumTermData() {
        try {
            log.info("Starting medium-term data sync");
            List<Coin> coins = coinRepository.findAll();

            coins.forEach(coin ->
                MEDIUM_FREQUENCY_PERIODS.forEach(period ->
                    syncCoinHistory(coin, period)
                )
            );
        } catch (Exception e) {
            log.error("Error during medium-term data sync", e);
        }
    }

//    @Scheduled(cron = "0 0 0 * * *") // Once a day at midnight
    public void syncLongTermData() {
        try {
            log.info("Starting long-term data sync");
            List<Coin> coins = coinRepository.findAll();

            coins.forEach(coin ->
                LOW_FREQUENCY_PERIODS.forEach(period ->
                    syncCoinHistory(coin, period)
                )
            );
        } catch (Exception e) {
            log.error("Error during long-term data sync", e);
        }
    }

    private void syncCoinHistory(Coin coin, String period) {
        try {
            // Check if we need to update this data
            if (shouldUpdateHistory(coin.getSymbol(), period)) {
                CoinPriceHistory history = apiService.fetchCoinHistory(coin.getUuid(), period);
                redisService.storeCoinHistory(coin.getSymbol(), period, history);
                Thread.sleep(200); // Rate limiting
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error during rate limiting of syncCoinHistory", e);
        }
    }

    private void storeCoinHistory(Coin coin, String period){
        try {
            CoinPriceHistory history = apiService.fetchCoinHistory(coin.getUuid(), period);
            redisService.storeCoinHistory(coin.getSymbol(), period, history);
            Thread.sleep(200); // Rate limiting
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error during rate limiting of storeCoinHistory", e);
        }
    }

    private boolean shouldUpdateHistory(String symbol, String period) {
        // Check last update time from Redis
        Long lastUpdateTime = redisService.getLastUpdateTime(symbol, period);
        if (lastUpdateTime == null) return true;

        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastUpdateTime;

        // Different update strategies based on time period
        return switch (period) {
            case "24h" -> timeDiff > TimeUnit.MINUTES.toMillis(5);
            case "7d" -> timeDiff > TimeUnit.MINUTES.toMillis(30);
            case "30d", "3m" -> timeDiff > TimeUnit.HOURS.toMillis(6);
            case "1y", "3y" -> timeDiff > TimeUnit.DAYS.toMillis(1);
            default -> true;
        };
    }

    // Public methods remain the same
    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    public List<TSElement> getCoinHistory(String symbol, String timePeriod) {
        return redisService.getCoinHistory(symbol, timePeriod);
    }

}
