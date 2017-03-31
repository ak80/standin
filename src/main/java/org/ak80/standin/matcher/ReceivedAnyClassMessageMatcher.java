package org.ak80.standin.matcher;

/**
 * Matches any message with the given class
 */
public class ReceivedAnyClassMessageMatcher implements ReceivedMessageMatcher {

    private final Class<?> clazz;

    public ReceivedAnyClassMessageMatcher(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean matches(Object message) {
        return clazz.isInstance(message);
    }
}
