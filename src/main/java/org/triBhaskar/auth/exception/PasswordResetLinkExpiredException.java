package org.triBhaskar.auth.exception;

public class PasswordResetLinkExpiredException extends RuntimeException {
    public PasswordResetLinkExpiredException(String message) {
        super(message);
    }
}
