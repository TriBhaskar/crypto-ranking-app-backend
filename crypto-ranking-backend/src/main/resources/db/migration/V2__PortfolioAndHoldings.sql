CREATE TABLE portfolio (
    portfolio_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES coin_user(user_id),
    virtual_balance DECIMAL(20,8) DEFAULT 10000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE portfolio_holdings (
    holding_id SERIAL PRIMARY KEY,
    portfolio_id BIGINT REFERENCES portfolio(portfolio_id),
    coin_uuid VARCHAR(255) REFERENCES coins(uuid),
    quantity DECIMAL(20,8) NOT NULL,
    average_buy_price DECIMAL(20,8) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(portfolio_id, coin_uuid)
);

CREATE TABLE transaction_history (
    transaction_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES coin_user(user_id),
    coin_uuid VARCHAR(255) REFERENCES coins(uuid),
    transaction_type VARCHAR(4) CHECK (transaction_type IN ('BUY', 'SELL')),
    quantity DECIMAL(20,8) NOT NULL,
    price_at_transaction DECIMAL(20,8) NOT NULL,
    transaction_fee DECIMAL(20,8) DEFAULT 0,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE watchlist (
    watchlist_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES coin_user(user_id),
    coin_uuid VARCHAR(255) REFERENCES coins(uuid),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    alerts_enabled BOOLEAN DEFAULT false,
    UNIQUE(user_id, coin_uuid)
);

-- Indexes for better query performance
CREATE INDEX idx_portfolio_user_id ON portfolio(user_id);
CREATE INDEX idx_portfolio_holdings_portfolio_id ON portfolio_holdings(portfolio_id);
CREATE INDEX idx_portfolio_holdings_coin_uuid ON portfolio_holdings(coin_uuid);
CREATE INDEX idx_transaction_history_user_id ON transaction_history(user_id);
CREATE INDEX idx_transaction_time ON transaction_history(transaction_time);
CREATE INDEX idx_watchlist_user_id ON watchlist(user_id);