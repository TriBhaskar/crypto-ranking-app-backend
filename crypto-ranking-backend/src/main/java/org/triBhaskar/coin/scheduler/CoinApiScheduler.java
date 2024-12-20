package org.triBhaskar.coin.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.triBhaskar.coin.service.CoinsDataService;

@Component
public class CoinApiScheduler {
    @Autowired
    private CoinsDataService coinsDataService;

//    @Scheduled(cron = "0 0/30 * 1/1 * ?")
    //once a month cron

    @Scheduled(cron = "0 0 0 1 * ?")
    public void getCoinDataToRedis(){
        try {
            coinsDataService.fetchCoins();
            coinsDataService.fetchCoinHistory();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
