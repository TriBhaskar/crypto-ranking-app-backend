package org.triBhaskar.config;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.triBhaskar.scheduler.CoinApiScheduler;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${redis.url}")
    private String redisURL;

    @Bean
    public JedisPooled jedisPool() {
        int maxRetries = 5;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                JedisPooled jedisPooled = new JedisPooled(redisURL);
                String ping = jedisPooled.ping();// Test the connection
               log.info("successfully connected to redis {}",ping);
                return jedisPooled;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to connect to Redis after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(120000); // Wait for 2 minutes before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during sleep", ie);
                }
            }
        }
        throw new RuntimeException("Failed to connect to Redis");
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}