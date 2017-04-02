package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.ReceivedMessageMatcher;

/**
 * Fluent definition of verification for sender of message
 */
public class StandInVerificationForFrom {

    private final ActorRef standIn;
    private final ReceivedMessageMatcher receivedMessageMatcher;
    private final Verification verification = Verification.verification;

    public StandInVerificationForFrom(ActorRef standIn, ReceivedMessageMatcher receivedMessageMatcher) {
        // TODO test verify
        StandIn.verifyStandIn(standIn);
        this.standIn = standIn;
        this.receivedMessageMatcher = receivedMessageMatcher;
    }

    /**
     * Verify the matched message was sent from the actor
     *
     * @param senderRef the expected sender
     */
    public void from(ActorRef senderRef) {
        verify(receivedMessageMatcher, senderRef);
    }

    private void verify(ReceivedMessageMatcher receivedMessageMatcher, ActorRef senderRef) {
        VerificationDefinition verificationDefinition = new VerificationDefinition(receivedMessageMatcher, senderRef);
        verification.doVerificationForReceive(standIn, verificationDefinition);
    }

}
