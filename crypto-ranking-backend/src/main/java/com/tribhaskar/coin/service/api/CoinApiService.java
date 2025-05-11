package com.tribhaskar.coin.service.api;

import com.tribhaskar.coin.model.CoinPriceHistory;
import com.tribhaskar.coin.model.Coins;

public interface CoinApiService {
    Coins fetchCoins();
    CoinPriceHistory fetchCoinHistory(String coinUuid, String timePeriod);
}
