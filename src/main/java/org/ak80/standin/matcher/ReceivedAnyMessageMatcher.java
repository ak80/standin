package org.ak80.standin.matcher;

/**
 * Matches any message
 */
public final class ReceivedAnyMessageMatcher implements ReceivedMessageMatcher {

    @Override
    public boolean matches(Object message) {
        return true;
    }

    @Override
    public String explain() {
        return "any message";
    }

}
