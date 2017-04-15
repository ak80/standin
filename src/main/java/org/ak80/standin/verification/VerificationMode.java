package org.ak80.standin.verification;

import org.ak80.standin.ReceivedMessage;

import java.util.List;

/**
 * Additional constraints to the verification, currently only how many times the message was expected
 */
public interface VerificationMode {

    boolean verifyMode(List<ReceivedMessage> matchedMessages, List<ReceivedMessage> receivedMessages, String explanation);
}
