package org.ak80.standin;

import akka.actor.ActorRef;

/**
 * A received message with sender actor
 */
public class ReceivedMessage {

    private final Object message;
    private final ActorRef receivedFrom;

    public ReceivedMessage(Object message, ActorRef receivedFrom) {
        this.message = message;
        this.receivedFrom = receivedFrom;
    }

    public Object getMessage() {
        return message;
    }

    public ActorRef getReceivedFrom() {
        return receivedFrom;
    }

    // TODO Test this class
    @Override
    public String toString() {
        return String.format("message >%s< from %s", getMessage(), getReceivedFrom());
    }

    // TODO equals verifier in test
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceivedMessage that = (ReceivedMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return receivedFrom != null ? receivedFrom.equals(that.receivedFrom) : that.receivedFrom == null;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (receivedFrom != null ? receivedFrom.hashCode() : 0);
        return result;
    }

}
