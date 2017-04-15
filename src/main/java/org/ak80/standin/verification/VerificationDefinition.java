package org.ak80.standin.verification;
import akka.actor.ActorRef;
import org.ak80.standin.ReceivedMessage;
import org.ak80.standin.matcher.ReceivedMessageMatcher;

import java.util.List;
import java.util.Optional;

/**
 * Definition of verification combining receive and sender
 */
public class VerificationDefinition implements ReceivedMessageMatcher<ReceivedMessage> {

    private final ReceivedMessageMatcher receivedMessageMatcher;
    private final Optional<ActorRef> receivedFrom;
    private final VerificationMode verificationMode;

    VerificationDefinition(ReceivedMessageMatcher receivedMessageMatcher, Optional<ActorRef> receivedFrom, VerificationMode verificationMode) {
        this.receivedMessageMatcher = receivedMessageMatcher;
        this.receivedFrom = receivedFrom;
        this.verificationMode = verificationMode;
    }

    @Override
    public boolean matches(ReceivedMessage message) {
        boolean messageMatches = receivedMessageMatcher.matches(message.getMessage());
        boolean senderMatches = getSenderRef().isPresent() ? getSenderRef().get().equals(message.getSender()) : true;
        return messageMatches && senderMatches;
    }

    @Override
    public String explain() {
        String expectedSender = getSenderRef().isPresent() ? " from " + getSenderRef().get().path().toString() : " from any Actor";
        return receivedMessageMatcher.explain() + expectedSender;
    }

    public Optional<ActorRef> getSenderRef() {
        return receivedFrom;
    }

    public void verifyMode(List<ReceivedMessage> matchedMessages, List<ReceivedMessage> receivedMessages) {
        verificationMode.verifyMode(matchedMessages, receivedMessages, explain());
    }

}
