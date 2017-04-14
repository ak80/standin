package org.ak80.standin.verification.exception;

import org.ak80.standin.verification.StandInVerificationException;

/**
 * Verification exception when a message has been received but should not have been received
 */
public abstract class NeverWantedButReceivedError extends StandInVerificationException {

    public NeverWantedButReceivedError(String message) {
        super(message);
    }

}
