package org.ak80.standin.matcher;

/**
 * Matches any message
 */
public class ReceivedAnyMessageMatcher implements ReceivedMessageMatcher {

    @Override
    public boolean matches(Object message) {
        return true;
    }

}
