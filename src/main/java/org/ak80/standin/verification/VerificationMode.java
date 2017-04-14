package org.ak80.standin.verification;

/**
 * Additional constraints to the verification, currently only how many times the message was expected
 */
public interface VerificationMode {


    boolean verifyMode(long numberOfMatchedMessages);
}
