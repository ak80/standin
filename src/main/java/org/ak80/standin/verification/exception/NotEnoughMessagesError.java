package org.ak80.standin.verification.exception;

/**
 * Verification exception when a message has not been received often enough
 */
public class NotEnoughMessagesError extends VerificationError {

    public NotEnoughMessagesError(String errorMessage) {
        super(errorMessage);
    }

}
