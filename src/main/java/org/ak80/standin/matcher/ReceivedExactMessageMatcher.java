package org.ak80.standin.matcher;

/**
 * Defines an exact match for a received message
 */
public class ReceivedExactMessageMatcher implements ReceivedMessageMatcher {

    private final Object exactMessage;

    public ReceivedExactMessageMatcher(Object exactMessage) {
        this.exactMessage = exactMessage;
    }

    @Override
    public boolean matches(Object message) {
        return exactMessage.equals(message);
    }

    @Override
    public String explain() {
        return "a message equal to \"" + exactMessage + "\"";
    }
}
