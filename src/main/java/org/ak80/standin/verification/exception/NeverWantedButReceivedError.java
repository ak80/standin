package org.ak80.standin.verification.exception;

/**
 * Verification exception when a message has been received but should not have been received
 */
public class NeverWantedButReceivedError extends VerificationError {

    public NeverWantedButReceivedError(String errorMessage) {
        super(errorMessage);
    }

}
