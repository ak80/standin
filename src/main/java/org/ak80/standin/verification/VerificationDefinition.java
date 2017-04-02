package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.matcher.ReceivedMessageMatcher;

import java.util.Optional;

/**
 * Definition of verification combining receive and sender
 */
public class VerificationDefinition implements ReceivedMessageMatcher {

    private final ReceivedMessageMatcher receivedMessageMatcher;
    private final Optional<ActorRef> senderRef;
    private final long count;

    public VerificationDefinition(ReceivedMessageMatcher receivedMessageMatcher) {
        this.receivedMessageMatcher = receivedMessageMatcher;
        this.senderRef = Optional.empty();
        this.count = 1;
    }

    public VerificationDefinition(ReceivedMessageMatcher receivedMessageMatcher, ActorRef senderRef) {
        this.receivedMessageMatcher = receivedMessageMatcher;
        this.senderRef = Optional.of(senderRef);
        this.count = 1;
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
        return senderRef;
    }

    public long getCount() {
        return count;
    }

}
