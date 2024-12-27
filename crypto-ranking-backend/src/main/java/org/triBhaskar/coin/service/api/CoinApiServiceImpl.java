package org.triBhaskar.coin.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.triBhaskar.coin.model.CoinPriceHistory;
import org.triBhaskar.coin.model.Coins;
import org.triBhaskar.utils.HttpUtils;

@Service
public class CoinApiServiceImpl implements CoinApiService {

    private static final Logger log = LoggerFactory.getLogger(CoinApiServiceImpl.class);
    private static final String GET_COINS_API = "https://coinranking1.p.rapidapi.com/coins";
    private static final String GET_COIN_HISTORY_API = "https://coinranking1.p.rapidapi.com/coin/";

    @Value("${coin.api.limit:50}")
    private int coinLimit;
    @Value("${coin.api.host}")
    private String apiHost;
    @Value("${coin.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public CoinApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Coins fetchCoins() {
        log.info("Fetching coins data");
        String url = buildCoinsUrl();
        ResponseEntity<Coins> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpUtils.getHttpEntity(apiHost, apiKey),
                Coins.class
        );
        log.info("Coins data fetched successfully");
        return response.getBody();
    }

    @Override
    public CoinPriceHistory fetchCoinHistory(String coinUuid, String timePeriod) {
        log.info("Fetching coin history for coin: {} and period: {}", coinUuid, timePeriod);
        String url = buildHistoryUrl(coinUuid, timePeriod);
        ResponseEntity<CoinPriceHistory> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpUtils.getHttpEntity(apiHost, apiKey),
                CoinPriceHistory.class
        );
        log.info("Coin history fetched successfully");
        return response.getBody();
    }

    private String buildCoinsUrl() {
        return UriComponentsBuilder.fromHttpUrl(GET_COINS_API)
                .queryParam("referenceCurrencyUuid", "yhjMzLPhuIDl")
                .queryParam("timePeriod", "24h")
                .queryParam("tiers", "1")
                .queryParam("orderBy", "marketCap")
                .queryParam("orderDirection", "desc")
                .queryParam("limit", coinLimit)
                .queryParam("offset", "0")
                .build()
                .toUriString();
    }

    private String buildHistoryUrl(String coinUuid, String timePeriod) {
        return GET_COIN_HISTORY_API + coinUuid + "/history?timePeriod=" + timePeriod;
    }
}
