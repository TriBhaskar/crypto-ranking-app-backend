package com.tribhaskar.coin.exception;

public class RedisOperationException extends RuntimeException {
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
