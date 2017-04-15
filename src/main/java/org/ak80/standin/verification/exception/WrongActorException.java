package org.ak80.standin.verification.exception;

/**
 * Error to throw when verification fails due to wrong actor
 */
public class WrongActorException extends VerificationError {

    public WrongActorException(String errorMessage) {
        super(errorMessage);
    }

}
