package org.ak80.standin.verification.exception;

/**
 * Verification exception when a message has been received too often
 */
public class TooManyMessagesError extends VerificationError {

    public TooManyMessagesError(String errorMessage) {
        super(errorMessage);
    }

}
