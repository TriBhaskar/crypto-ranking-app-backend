package com.tribhaskar.auth.exception;

public class TooManyOtpRequestsException extends RuntimeException {
  public TooManyOtpRequestsException(String message) {
    super(message);
  }
}
