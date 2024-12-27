package org.triBhaskar.coin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.triBhaskar.coin.entity.Coin;
import org.triBhaskar.coin.model.CoinInfo;
import org.triBhaskar.coin.model.HistoryData;
import org.triBhaskar.coin.service.CoinsDataService;
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
