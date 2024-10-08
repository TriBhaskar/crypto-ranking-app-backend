package org.triBhaskar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.triBhaskar.model.CoinInfo;
import org.triBhaskar.model.HistoryData;
import org.triBhaskar.service.CoinsDataService;
import org.triBhaskar.utils.Utility;
import redis.clients.jedis.timeseries.TSElement;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/coins")
@Slf4j
public class CoinsRankingController {

    @Autowired
    private CoinsDataService coinsDataService;

    @GetMapping
    public ResponseEntity<List<CoinInfo>> fetchAllCoins() {
        return ResponseEntity.ok().body(coinsDataService.fetchAllCoinsFromRedisJSON());
    }
    @GetMapping("/{symbol}/{timePeriod}")
    public List<HistoryData> fetchCoinHistoryPerTimePeriod(@PathVariable String symbol, @PathVariable String timePeriod) {
        List<TSElement> coinsTSData = coinsDataService.fetchCoinHistoryPerTimePeriodFromRedisTS(symbol, timePeriod);
        List<HistoryData> coinHistory = new ArrayList<>();
        for (TSElement tsElement : coinsTSData) {
            HistoryData historyData = new HistoryData(Utility.convertUnixTimeToDate(tsElement.getTimestamp()), Utility.round(tsElement.getValue(), 2));
            coinHistory.add(historyData);
        }
        return coinHistory;
    }
}
