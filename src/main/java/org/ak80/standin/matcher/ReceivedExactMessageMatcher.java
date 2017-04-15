package org.ak80.standin.matcher;

/**
 * Defines an exact match for a received message
 */
public final class ReceivedExactMessageMatcher implements ReceivedMessageMatcher {

    private final Object exactMessage;

    public ReceivedExactMessageMatcher(Object receivedMessage) {
        this.exactMessage = receivedMessage;
    }

    @Override
    public boolean matches(Object receivedMessage) {
        return exactMessage.equals(receivedMessage);
    }

    @Override
    public String explain() {
        return "a message equal to >" + exactMessage + "<";
    }
}
