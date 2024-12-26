package org.triBhaskar.coin.service.api;

import org.triBhaskar.coin.model.CoinPriceHistory;
import org.triBhaskar.coin.model.Coins;

public interface CoinApiService {
    Coins fetchCoins();
    CoinPriceHistory fetchCoinHistory(String coinUuid, String timePeriod);
}
