package org.ak80.standin.verification;

/**
 * Verification exception when a message has been received but should not have been received
 */
public abstract class NeverWantedButReceivedError extends StandInVerificationException {

    public NeverWantedButReceivedError(String message) {
        super(message);
    }

}
