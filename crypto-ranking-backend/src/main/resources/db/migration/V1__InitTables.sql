CREATE TABLE coin_user (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_login TIMESTAMP,
    role VARCHAR(50) NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    verification_expiry TIMESTAMP
);

CREATE TABLE coins (
    uuid VARCHAR(255) PRIMARY KEY,
    symbol VARCHAR(255),
    name VARCHAR(255),
    color VARCHAR(255),
    icon_url VARCHAR(255),
    listed_at INTEGER,
    tier INTEGER,
    rank INTEGER,
    low_volume BOOLEAN,
    coinranking_url VARCHAR(255),
    btc_price VARCHAR(255)
);