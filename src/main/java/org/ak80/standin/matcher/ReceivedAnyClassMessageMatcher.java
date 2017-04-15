package org.ak80.standin.matcher;

/**
 * Matches any message with the given class
 */
public final class ReceivedAnyClassMessageMatcher implements ReceivedMessageMatcher {

    private final Class<?> expectedClass;

    public ReceivedAnyClassMessageMatcher(Class<?> receivedCall) {
        this.expectedClass = receivedCall;
    }

    @Override
    public boolean matches(Object message) {
        return expectedClass.isInstance(message);
    }

    @Override
    public String explain() {
        return "any message of type " + expectedClass;
    }
}
