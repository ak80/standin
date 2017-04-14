package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.matcher.ReceivedMessageMatcher;

import java.util.Optional;

/**
 * Definition of verification combining receive and sender
 */
public class VerificationDefinition implements ReceivedMessageMatcher {

    private final ReceivedMessageMatcher receivedMessageMatcher;
    private final Optional<ActorRef> receivedFrom;
    private final VerificationMode verificationMode;

    VerificationDefinition(ReceivedMessageMatcher receivedMessageMatcher, Optional<ActorRef> receivedFrom, VerificationMode verificationMode) {
        this.receivedMessageMatcher = receivedMessageMatcher;
        this.receivedFrom = receivedFrom;
        this.verificationMode = verificationMode;
    }

    @Override
    public boolean matches(Object message) {
        return receivedMessageMatcher.matches(message);
    }

    @Override
    public String explain() {
        return receivedMessageMatcher.explain();
    }

    public Optional<ActorRef> getSenderRef() {
        return receivedFrom;
    }

    public boolean verifyMode(long numberOfMatchedMessages) {
        return verificationMode.verifyMode(numberOfMatchedMessages);
    }
}
