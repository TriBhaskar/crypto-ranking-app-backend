package com.tribhaskar.coin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tribhaskar.coin.entity.Coin;
import com.tribhaskar.coin.model.HistoryData;
import com.tribhaskar.coin.service.CoinsDataService;
import com.tribhaskar.utils.Utility;
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
    public ResponseEntity<List<Coin>> fetchAllCoins() {
        return ResponseEntity.ok().body(coinsDataService.getAllCoins());
    }
    @GetMapping("/{symbol}/{timePeriod}")
    public List<HistoryData> fetchCoinHistoryPerTimePeriod(@PathVariable String symbol, @PathVariable String timePeriod) {
        System.out.println("Fetching coin history for symbol: " + symbol + " and period: " + timePeriod);
        List<TSElement> coinsTSData = coinsDataService.getCoinHistory(symbol, timePeriod);
        List<HistoryData> coinHistory = new ArrayList<>();
        for (TSElement tsElement : coinsTSData) {
            HistoryData historyData = new HistoryData(Utility.convertUnixTimeToDate(tsElement.getTimestamp()), Utility.round(tsElement.getValue(), 2));
            coinHistory.add(historyData);
        }
        return coinHistory;
    }
}
