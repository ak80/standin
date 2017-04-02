package org.ak80.standin.verification;

/**
 * Error to throw when verification fails
 */
public class StandInAssertionError extends StandInVerificationException {

    public StandInAssertionError(String message) {
        super(message);
    }

}
