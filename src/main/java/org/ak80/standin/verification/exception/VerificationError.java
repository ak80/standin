package org.ak80.standin.verification.exception;

import org.ak80.standin.verification.StandInVerificationException;

/**
 * Error to throw when verification fails
 */
public class VerificationError extends StandInVerificationException {

    public VerificationError(String errorMessage) {
        super("Verification error:\n    " + errorMessage);
    }

}
