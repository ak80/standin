package org.ak80.standin.stubbing;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.*;

import java.util.function.Predicate;

/**
 * Fluent stubbing for a StandIn to define receives
 * <p> Uses "receive" methods to define what how to match incoming messages and
 * "thenReply" methods to define what is sent back.
 * <p> The order of defining the receives is important, first match wins
 * </p>
 */
public final class StandInStubbingForReceives {

    private final ActorRef standIn;

    private ReceivedMessageMatcher messageMatcher;

    public StandInStubbingForReceives(ActorRef standIn) {
        StandIn.verifyStandIn(standIn);
        this.standIn = standIn;
    }

    /**
     * Return the reply regardless of message
     *
     * @return stubbing for reply
     */
    public StandInStubbingForReply receivesAny() {
        messageMatcher = new ReceivedAnyMessageMatcher();
        return new StandInStubbingForReply(standIn, messageMatcher);
    }

    /**
     * Return the reply when message is instance of the class
     *
     * @param clazz the class to match instances of
     * @return this
     */
    public StandInStubbingForReply receivesAny(Class<?> clazz) {
        messageMatcher = new ReceivedAnyClassMessageMatcher(clazz);
        return new StandInStubbingForReply(standIn, messageMatcher);
    }

    /**
     * Return the reply when message is equal
     *
     * @param exactMessage the exact message to match with equals()
     * @return stubbing for reply
     */

    public StandInStubbingForReply receivesEq(Object exactMessage) {
        messageMatcher = new ReceivedExactMessageMatcher(exactMessage);
        return new StandInStubbingForReply(standIn, messageMatcher);
    }

    /**
     * Return the reply if the condition is true
     *
     * @param condition test if reply is returned
     * @return stubbing for reply
     */
    public StandInStubbingForReply receives(Predicate<Object> condition) {
        messageMatcher = new ReceivedPredicateMessageMatcher(condition);
        return new StandInStubbingForReply(standIn, messageMatcher);
    }

}
