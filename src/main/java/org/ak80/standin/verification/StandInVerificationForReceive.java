package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.ReceivedAnyClassMessageMatcher;
import org.ak80.standin.matcher.ReceivedExactMessageMatcher;
import org.ak80.standin.matcher.ReceivedMessageMatcher;

/**
 * Fluent verification for received message
 */
public class StandInVerificationForReceive {

    private final ActorRef standIn;
    private final Verification verification = Verification.verification;

    public StandInVerificationForReceive(ActorRef standIn) {
        // TODO test verify
        StandIn.verifyStandIn(standIn);
        this.standIn = standIn;
    }

    /**
     * Verify the exact message was received
     *
     * @param expectedMessage the message that must be received
     * @return a verification for a StandIn from
     */
    public StandInVerificationForFrom receivedEq(Object expectedMessage) {
        ReceivedMessageMatcher receivedMessageMatcher = new ReceivedExactMessageMatcher(expectedMessage);
        verify(receivedMessageMatcher);
        return new StandInVerificationForFrom(standIn, receivedMessageMatcher);
    }

    private void verify(ReceivedMessageMatcher receivedMessageMatcher) {
        VerificationDefinition verificationDefinition = new VerificationDefinition(receivedMessageMatcher);
        verification.doVerificationForReceive(standIn, verificationDefinition);
    }

    /**
     * Match any message of a type
     *
     * @param clazz the type of message to match
     * @return a verification for a StandIn from
     */
    public StandInVerificationForFrom receivedAny(Class<?> clazz) {
        ReceivedMessageMatcher receivedMessageMatcher = new ReceivedAnyClassMessageMatcher(clazz);
        verify(receivedMessageMatcher);
        return new StandInVerificationForFrom(standIn, receivedMessageMatcher);
    }

}
