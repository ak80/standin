package org.ak80.standin.matcher;

import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$Integer;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ReceivedAnyClassMessageMatcherTest {

    @Test
    public void testMatch_whenSameClass_matches() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyClassMessageMatcher(String.class);

        // When
        boolean matches = messageMatcher.matches(a($String()));

        // Then
        assertThat(matches,is(true));
    }

    @Test
    public void testMatch_whenNotSameClass_matchesNot() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyClassMessageMatcher(String.class);

        // When
        boolean matches = messageMatcher.matches(a($Integer()));

        // Then
        assertThat(matches,is(false));
    }

    @Test
    public void testExplain() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyClassMessageMatcher(String.class);

        // When
        String explain = messageMatcher.explain();

        // Then
        assertThat(explain,is("any message of type class java.lang.String"));
    }

}