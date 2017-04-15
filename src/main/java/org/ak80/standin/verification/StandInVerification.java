package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.ReceivedAnyClassMessageMatcher;
import org.ak80.standin.matcher.ReceivedExactMessageMatcher;
import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.matcher.ReceivedPredicateMessageMatcher;

import java.util.Optional;
import java.util.function.Predicate;

import static org.ak80.standin.verification.Times.once;

/**
 * Fluent definition of verification
 */
public final class StandInVerification implements StandInVerificationForReceive {

    private final Verification verification = new Verification();

    private final ActorRef standIn;

    private Optional<ActorRef> receivedFrom = Optional.empty();

    public StandInVerification(ActorRef standIn) {
        StandIn.verifyStandIn(standIn);
        this.standIn = standIn;
    }

    /**
     * Verify the matched message was sent from the actor
     *
     * @param receivedFrom the expected sender
     * @return a verification for a StandIn receive
     */
    public StandInVerificationForReceive from(ActorRef receivedFrom) {
        this.receivedFrom = Optional.of(receivedFrom);
        return this;
    }


    @Override
    public void receivedEq(Object expectedMessage) {
        receivedEq(expectedMessage, once());
    }

    public void verify(ReceivedMessageMatcher messageMatcher, VerificationMode verificationMode) {
        VerificationDefinition verificationDefinition = new VerificationDefinition(messageMatcher, receivedFrom, verificationMode);
        verification.doVerificationForReceive(standIn, verificationDefinition);
    }

    @Override
    public void receivedEq(Object expectedMessage, VerificationMode verificationMode) {
        ReceivedMessageMatcher messageMatcher = new ReceivedExactMessageMatcher(expectedMessage);
        verify(messageMatcher, verificationMode);
    }

    @Override
    public void receivedAny(Class<?> clazz) {
        receivedAny(clazz, once());
    }

    @Override
    public void receivedAny(Class<?> clazz, VerificationMode verificationMode) {
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyClassMessageMatcher(clazz);
        verify(messageMatcher, verificationMode);
    }

    @Override
    public void received(Predicate<Object> condition) {
        received(condition, once());
    }

    @Override
    public void received(Predicate<Object> condition, VerificationMode verificationMode) {
        ReceivedMessageMatcher messageMatcher = new ReceivedPredicateMessageMatcher(condition);
        verify(messageMatcher, verificationMode);
    }

}
