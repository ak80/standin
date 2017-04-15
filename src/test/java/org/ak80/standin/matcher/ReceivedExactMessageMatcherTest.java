package org.ak80.standin.matcher;

import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReceivedExactMessageMatcherTest {

    @Test
    public void testMatch_whenSameClass_matches() {
        // Given
        String message = a($String());
        ReceivedMessageMatcher messageMatcher = new ReceivedExactMessageMatcher(message);

        // When
        boolean matches = messageMatcher.matches(message);

        // Then
        assertThat(matches, is(true));
    }

    @Test
    public void testMatch_whenNotSameClass_matchesNot() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedExactMessageMatcher(a($String()));

        // When
        boolean matches = messageMatcher.matches(a($String()));

        // Then
        assertThat(matches, is(false));
    }

    @Test
    public void testExplain() {
        // Given
        String expectedMessage = a($String());
        ReceivedMessageMatcher messageMatcher = new ReceivedExactMessageMatcher(expectedMessage);

        // When
        String explain = messageMatcher.explain();

        // Then
        assertThat(explain, is("a message equal to >" + expectedMessage + "<"));
    }

}
