package org.ak80.standin;

import akka.actor.ActorRef;

import java.util.Objects;

/**
 * A received message with sender actor
 */
public class ReceivedMessage {

    private final Object message;
    private final ActorRef sender;

    public ReceivedMessage(Object message, ActorRef receivedFrom) {
        this.message = message;
        this.sender = receivedFrom;
    }

    public Object getMessage() {
        return message;
    }

    public ActorRef getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return String.format("message >%s< from %s", getMessage(), getSender());
    }

    @Override
    public final boolean equals(Object object) {
        if(object == null || !getClass().isInstance(object)) {
            return false;
        }
        ReceivedMessage other = (ReceivedMessage) object;
        return Objects.equals(message, other.message)
                && Objects.equals(sender, other.sender);

    }

    @Override
    public final int hashCode() {
        return Objects.hash(message, sender);
    }

}
