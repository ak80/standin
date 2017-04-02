package org.ak80.standin.matcher;

/**
 * Defines a received message match
 */
public interface ReceivedMessageMatcher {

    /**
     * Returns true if the message matches
     *
     * @param message to match
     * @return true if the message matches
     */
    boolean matches(Object message);

    /**
     * Explain the match
     *
     * @return a string explaining how the match is determined
     */
    String explain();

}
