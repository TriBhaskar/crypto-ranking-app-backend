package org.triBhaskar.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {
    @Value("${redis.url}")
    private String redisURL;

    @Bean
    public JedisPooled jedisPool() {
        return new JedisPooled(redisURL);
    }
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
