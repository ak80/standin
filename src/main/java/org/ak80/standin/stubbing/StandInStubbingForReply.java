package org.ak80.standin.stubbing;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.reply.ReplyMessages;
import org.ak80.standin.reply.ReplyWith;

import java.util.function.Function;

/**
 * Fluent stubbing for a StandIn to define replies
 * <p>
 * Uses receive methods to define what how to match incoming messages and
 * thenReply methods to define what is sent back.
 * <p>
 * The order of defining the receives is important, first match wins
 * </p>
 */
public final class StandInStubbingForReply {

    private final ActorRef standIn;
    private final ReceivedMessageMatcher receivedMessageMatcher;

    public StandInStubbingForReply(ActorRef standIn, ReceivedMessageMatcher receivedMessageMatcher) {
        StandIn.verifyStandIn(standIn);
        this.standIn = standIn;
        this.receivedMessageMatcher = receivedMessageMatcher;
    }

    /**
     * Defines the reply message(s)
     * <p> If there are more than one, they a returned in the given order, and the last message is repeatedly
     * returned if necessary
     * </p>
     *
     * @param replyMessages the messages
     * @return this
     */
    public StandInStubbingForReceives thenReply(Object... replyMessages) {
        standIn.tell(new StubbingDefinition(receivedMessageMatcher, new ReplyMessages(replyMessages)), ActorRef.noSender());
        return new StandInStubbingForReceives(standIn);
    }

    /**
     * Defines that the reply messages are created with a function
     *
     * @param replyFunction
     * @return stubbing to define received
     */
    public <T, R> StandInStubbingForReceives thenReplyWith(Function<T, R> replyFunction) {
        standIn.tell(new StubbingDefinition(receivedMessageMatcher, new ReplyWith(replyFunction)), ActorRef.noSender());
        return new StandInStubbingForReceives(standIn);
    }

}
