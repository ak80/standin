package org.ak80.standin.matcher;

import java.util.function.Predicate;

/**
 * Defines a received message match with a condition
 */
public final class ReceivedPredicateMessageMatcher implements ReceivedMessageMatcher {

    private final Predicate<Object> condition;

    public ReceivedPredicateMessageMatcher(Predicate<Object> condition) {
        this.condition = condition;
    }

    @Override
    public boolean matches(Object message) {
        return condition.test(message);
    }

    @Override
    public String explain() {
        return "a message matching a custom condition";
    }
}
