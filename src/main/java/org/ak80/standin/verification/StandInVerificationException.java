package org.ak80.standin.verification;

/**
 * Root class for all verification exceptions
 */
public abstract class StandInVerificationException extends RuntimeException {

    public StandInVerificationException(String message) {
        super(message);
    }

}
