package org.ak80.standin.matcher;

import org.junit.Test;

import java.util.function.Predicate;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReceivedPredicateMessageMatcherTest {

    private final String expectedMessage = a($String());
    private final Predicate<Object>matcher = message -> expectedMessage.equals(message);

    @Test
    public void testMatch_whenSameClass_matches() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedPredicateMessageMatcher(matcher);

        // When
        boolean matches = messageMatcher.matches(expectedMessage);

        // Then
        assertThat(matches,is(true));
    }

    @Test
    public void testMatch_whenNotSameClass_matchesNot() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedPredicateMessageMatcher(matcher);

        // When
        boolean matches = messageMatcher.matches(a($String()));

        // Then
        assertThat(matches,is(false));
    }

    @Test
    public void testExplain() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedPredicateMessageMatcher(matcher);

        // When
        String explain = messageMatcher.explain();

        // Then
        assertThat(explain,is("a message matching a custom condition"));
    }

}