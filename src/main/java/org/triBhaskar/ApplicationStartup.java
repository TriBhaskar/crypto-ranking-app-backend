package org.triBhaskar;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.triBhaskar.service.CoinsDataService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private CoinsDataService coinsDataService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        try {
//            coinsDataService.fetchCoins();
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        coinsDataService.fetchCoinHistory();

    }
}
