package org.ak80.standin.verification.exception;

/**
 * Verification exception when a message has not received
 */
public class MessageNotReceivedError extends VerificationError {

    public MessageNotReceivedError(String errorMessage) {
        super(errorMessage);
    }

}
