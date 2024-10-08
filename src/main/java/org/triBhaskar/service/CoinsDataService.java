package org.triBhaskar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.triBhaskar.model.*;
import org.triBhaskar.utils.HttpUtils;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;
import redis.clients.jedis.timeseries.DuplicatePolicy;
import redis.clients.jedis.timeseries.TSCreateParams;
import redis.clients.jedis.timeseries.TSElement;
import redis.clients.jedis.timeseries.TSInfo;
import org.triBhaskar.model.CoinInfo;

import java.util.List;
import java.util.Map;

@Service
public class CoinsDataService {

    public static final String GET_COINS_API = "https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
    public static final String GET_COIN_HISTORY_API = "https://coinranking1.p.rapidapi.com/coin/";
    public static final String COIN_HISTORY_TIME_PERIOD_PARAM = "/history?timePeriod=";
    public static final List<String> timePeriods = List.of("24h", "7d", "30d","3m","1y", "3y");
    public static final String REDIS_KEY_COINS = "coins";
    private static final Logger log = LoggerFactory.getLogger(CoinsDataService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JedisPooled client;
    @Autowired
    private Gson gson;

    public void fetchCoins() throws JsonProcessingException {
        log.info("Fetching coins data");
        ResponseEntity<Coins> response = restTemplate.exchange(GET_COINS_API, HttpMethod.GET, HttpUtils.getHttpEntity(), Coins.class);
        Coins coins = response.getBody();
        log.info("Coins data fetched: {}", coins);
        storeCoinsToRedis(coins);
    }
    public void fetchCoinHistory(){
        log.info("Fetching coin history data");
        List<CoinInfo> allCoins = getAllCoinsFromRedisJSON();
        allCoins.forEach(coinInfo -> {
            timePeriods.forEach(s -> {
                fetchCoinHistoryForTimePeriod(coinInfo, s);
                try {
                    Thread.sleep(200); // To Avoid Rate Limit of rapid API of 5 Request/Sec
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private void fetchCoinHistoryForTimePeriod(CoinInfo coinInfo, String timePeriod) {
        log.info("Fetching coin history data for coin: {} and time period: {}", coinInfo.getName(), timePeriod);
        String url = GET_COIN_HISTORY_API + coinInfo.getUuid() + COIN_HISTORY_TIME_PERIOD_PARAM + timePeriod;
        ResponseEntity<CoinPriceHistory> coinPriceHistoryResponseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpUtils.getHttpEntity(), CoinPriceHistory.class);
        log.info("Coin history data fetched: {}", coinPriceHistoryResponseEntity.getBody());
        storeCoinHistoryToRedisTS(coinPriceHistoryResponseEntity.getBody(), coinInfo.getSymbol(), timePeriod);
    }

    private void storeCoinHistoryToRedisTS(CoinPriceHistory coinPriceHistory, String symbol, String timePeriod) {
        log.info("Storing coin history data to Redis");
        List<CoinPriceHistoryExchangeRate> coinExchangeRates = coinPriceHistory.getData().getHistory();
        coinExchangeRates.forEach(ch->{
                                    long l = client.tsAdd( symbol + ":" + timePeriod, ch.getTimestamp(), ch.getPrice(), new TSCreateParams().uncompressed().duplicatePolicy(DuplicatePolicy.LAST));
                                });
        log.info("Coin history data stored in Redis");
//        client.tsAdd("coinHistory:"+symbol+":"+timePeriod, coinPriceHistory.getTimestamps(), coinPriceHistory.getPrices());
    }

    private List<CoinInfo> getAllCoinsFromRedisJSON() {
        log.info("Fetching all coins from Redis");
        CoinData coinData= client.jsonGet(REDIS_KEY_COINS,CoinData.class,new Path("data"));
        System.out.println(coinData);
        return coinData.getCoins();
    }

    private void storeCoinsToRedis(Coins coins) throws JsonProcessingException {
        log.info("Storing coins data to Redis");
        client.jsonSet(REDIS_KEY_COINS, gson.toJson(coins));

    }

    public List<CoinInfo> fetchAllCoinsFromRedisJSON() {
        return getAllCoinsFromRedisJSON();
    }

    public List<TSElement> fetchCoinHistoryPerTimePeriodFromRedisTS(String symbol, String timePeriod) {
        Map<String,Object> tsInfo = fetchTSInfoForSymbol(symbol, timePeriod);
        Long firsTimestamp = Long.valueOf(tsInfo.get("firstTimestamp").toString());
        Long lastTimestamp = Long.valueOf(tsInfo.get("lastTimestamp").toString());
        System.out.println("First Timestamp: "+firsTimestamp + " Last Timestamp: "+lastTimestamp);
        List<TSElement> coinsTSData = fetchTSDataForCoin(symbol, timePeriod, firsTimestamp, lastTimestamp);
        return coinsTSData;
    }

    private List<TSElement> fetchTSDataForCoin(String symbol, String timePeriod, Long firsTimestamp, Long lastTimestamp) {
        String key = symbol + ":" + timePeriod;
        return client.tsRange(key, firsTimestamp, lastTimestamp);
    }

    private Map<String, Object> fetchTSInfoForSymbol(String symbol, String timePeriod) {
        TSInfo tsInfo = client.tsInfo(symbol + ":" + timePeriod);
        return tsInfo.getProperties();
    }
}
