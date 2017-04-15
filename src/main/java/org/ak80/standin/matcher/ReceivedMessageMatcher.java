package org.ak80.standin.matcher;

/**
 * Defines a received message match
 */
public interface ReceivedMessageMatcher<T> {

    /**
     * Returns true if the message matches
     *
     * @param message to match
     * @return true if the message matches
     */
    boolean matches(T message);

    /**
     * Explain the match
     *
     * @return a string explaining how the match is determined
     */
    String explain();

}
