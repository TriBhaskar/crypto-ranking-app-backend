package org.triBhaskar.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.triBhaskar.exception.DataBaseConnectionException;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 2000;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @Primary
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        hikariConfig.setUsername(dataSourceProperties.getUsername());
        hikariConfig.setPassword(dataSourceProperties.getPassword());
        hikariConfig.setMaximumPoolSize(dataSourceProperties.getPoolSize());

        HikariDataSource hikariDataSource = null;
        int attempt = 0;

        while (attempt < MAX_RETRIES) {
            try {
                hikariDataSource = new HikariDataSource(hikariConfig);
                hikariDataSource.getConnection().isValid(2); // Test the connection
                log.info("Database connection established.");
                return hikariDataSource;
            } catch (Exception e) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new DataBaseConnectionException("Failed to connect to the database after " + MAX_RETRIES + " attempts "+e.getMessage());
                }
                try {
                    log.warn("Failed to connect to the database.... Retrying connection to the database");
                    Thread.sleep(30000); // Sleep for 30 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new DataBaseConnectionException("Thread interrupted during sleep"+ ie.getMessage());
                }
            }
        }
        throw new DataBaseConnectionException("Failed to connect to the database");
    }

    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource hikariDataSource) {
        return new JdbcTemplate(hikariDataSource);
    }
}