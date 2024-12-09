ALTER TABLE coin_user ADD email_verified BOOLEAN DEFAULT FALSE NOT NULL;
ALTER TABLE coin_user ADD COLUMN verification_expiry TIMESTAMP;

CREATE TABLE coininfo (
    uuid VARCHAR(255) PRIMARY KEY,
    symbol VARCHAR(255),
    name VARCHAR(255),
    color VARCHAR(255),
    icon_url VARCHAR(255),
    market_cap VARCHAR(255),
    price VARCHAR(255),
    listed_at INTEGER,
    tier INTEGER,
    change VARCHAR(255),
    rank INTEGER,
    sparkline TEXT,
    low_volume BOOLEAN,
    coinranking_url VARCHAR(255),
    _24h_volume VARCHAR(255),
    btc_price VARCHAR(255)
);

