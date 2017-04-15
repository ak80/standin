package org.ak80.standin.verification.exception;

/**
 * Exception, when no messages received but tried to match
 */
public class NoMessagesReceivedError extends VerificationError {

    public NoMessagesReceivedError(String errorMessage) {
        super(errorMessage);
    }

}
