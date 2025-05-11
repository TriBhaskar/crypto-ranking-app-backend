package com.tribhaskar.auth.exception;

public class FailedToSendEmailException extends RuntimeException {
  public FailedToSendEmailException(String message) {
    super(message);
  }
}
