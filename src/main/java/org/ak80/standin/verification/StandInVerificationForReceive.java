package org.ak80.standin.verification;

import java.util.function.Predicate;

/**
 * Fluent verification for received message when from is already defined
 */
public interface StandInVerificationForReceive {

    /**
     * Verify the exact message was received
     *
     * @param expectedMessage the message that must be received
     */
    void receivedEq(Object expectedMessage);

    /**
     * Verify the exact message was received
     *
     * @param expectedMessage  the message that must be received
     * @param verificationMode how many times message that must be received
     */
    void receivedEq(Object expectedMessage, VerificationMode verificationMode);

    /**
     * Match any message of a type
     *
     * @param clazz the type of message to match
     */
    void receivedAny(Class<?> clazz);

    /**
     * Match any message of a type
     *
     * @param clazz            the type of message to match
     * @param verificationMode how many times message that must be received
     */
    void receivedAny(Class<?> clazz, VerificationMode verificationMode);

    /**
     * Match message with condition
     *
     * @param condition the condition for matching
     * @return a verification for a StandIn from
     */
    void received(Predicate<Object> condition);

    /**
     * Match message with condition
     *
     * @param condition        the condition for matching
     * @param verificationMode how many times message that must be received
     */
    void received(Predicate<Object> condition, VerificationMode verificationMode);

}
