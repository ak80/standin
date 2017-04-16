package org.ak80.standin;

import akka.actor.ActorRef;

import java.util.Objects;

/**
 * A received message with sender actor
 */
public final class ReceivedMessage {

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
    public boolean equals(Object object) {
        if (object == null || !this.getClass().isInstance(object)) {
            return false;
        }
        ReceivedMessage other = (ReceivedMessage) object;
        return Objects.equals(message, other.message)
                && Objects.equals(sender, other.sender);

    }

    @Override
    public int hashCode() {
        return Objects.hash(message, sender);
    }

}
