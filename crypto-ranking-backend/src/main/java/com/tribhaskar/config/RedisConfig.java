package com.tribhaskar.config;

import com.google.gson.Gson;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.Connection;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableScheduling
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    private volatile JedisPooled cachejedisPooled;
    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.username}")
    private String username;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.max-active}")
    private int maxTotal;
    @Value("${redis.max-idle}")
    private int maxIdle;
    @Value("${redis.min-idle}")
    private int minIdle;
    @Value("${redis.use-ssl}")
    private boolean useSsl;

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setUsername(username);
        config.setPassword(password);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JedisPooled jedisPool() {
        int maxRetries = 5;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
                poolConfig.setMinIdle(minIdle);
                poolConfig.setMaxIdle(maxIdle);
                poolConfig.setMaxTotal(maxTotal);
                cachejedisPooled = new JedisPooled(poolConfig, host, port, timeout, password, useSsl);
                String ping = cachejedisPooled.ping();
                log.info("Successfully connected to Redis: {}", ping);
                return cachejedisPooled;
            } catch (Exception e) {
                retryCount++;
                log.error("Attempt{} of {}failed to connect to Redis:{}", retryCount, maxRetries, e.getMessage());

                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to connect to Redis after " + maxRetries + " attempts", e);
                }

                try {
                    log.warn("Waiting 30 seconds before retry attempt {}", retryCount + 1);
                    Thread.sleep(30000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during sleep", ie);
                }
            }
        }
        throw new RuntimeException("Failed to connect to Redis");
    }

    public JedisPooled getValidRedisConnection() {
        if (cachejedisPooled == null || !isConnectionValid(cachejedisPooled)) {
            if (isReconnecting.compareAndSet(false, true)) {
                try {
                    log.warn("Connection invalid or null, attempting to create new connection");
                    cachejedisPooled = jedisPool();
                } catch (Exception e) {
                    log.error("Failed to create new Redis connection: {}", e.getMessage());
                    throw e;
                } finally {
                    isReconnecting.set(false);
                }
            } else {
                log.info("Another thread is currently reconnecting, waiting...");
                // Wait for the other thread to finish reconnecting
                int waitCount = 0;
                while (isReconnecting.get() && waitCount < 30) { // Maximum 30 seconds wait
                    try {
                        Thread.sleep(1000);
                        waitCount++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted while waiting for reconnection");
                    }
                }
                if (waitCount >= 30) {
                    log.error("Timeout waiting for Redis reconnection");
                    throw new RuntimeException("Timeout waiting for Redis reconnection");
                }
            }
        }
        return cachejedisPooled;
    }

    private boolean isConnectionValid(JedisPooled jedisPooled) {
        try {
            String pingResult = jedisPooled.ping();
            if("PONG".equalsIgnoreCase(pingResult)){
                log.debug("Redis connection check successful");
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Redis connection check failed: {}", e.getMessage());
            return false;
        }
    }

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void checkConnection() {
        try {
            log.debug("Performing scheduled Redis connection check");
            if (cachejedisPooled != null) {
                if (!isConnectionValid(cachejedisPooled)) {
                    log.warn("Scheduled check: Redis connection invalid, initiating reconnection");
                    getValidRedisConnection(); // This will handle reconnection
                }else{
                    log.debug("Scheduled check: Redis connection is valid");
                }
            }
        } catch (Exception e) {
            log.error("Error during scheduled Redis connection check: {}", e.getMessage());
            cachejedisPooled = null; // Force reconnection on next use
        }
    }

    public JedisPooled getCurrentConnection() {
        return getValidRedisConnection();
    }
}