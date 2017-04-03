package org.ak80.standin.matcher;

import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReceivedAnyMessageMatcherTest {

    @Test
    public void testMatch_whenAny_matches() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyMessageMatcher();

        // When
        boolean matches = messageMatcher.matches(a($String()));

        // Then
        assertThat(matches, is(true));
    }


    @Test
    public void testExplain() {
        // Given
        ReceivedMessageMatcher messageMatcher = new ReceivedAnyMessageMatcher();

        // When
        String explain = messageMatcher.explain();

        // Then
        assertThat(explain, is("any message"));
    }

}