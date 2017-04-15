package org.ak80.standin.stubbing;

import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.reply.ReplyMessageDefinition;
import org.ak80.standin.reply.ReplyMessages;
import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StubbingDefinitionTest {

    @Test
    public void testMatches_match_returnsTrue() {
        // Given
        String exactMessage = a($String());
        ReceivedMessageMatcher receivedMessageMatcher = getReceivedMessageMatcher(exactMessage);
        ReplyMessageDefinition replyMessageDefinition = getReplyMessages(a($String()));

        StubbingDefinition stubbingDefinition = new StubbingDefinition(receivedMessageMatcher, replyMessageDefinition);

        // Then
        assertThat(stubbingDefinition.matches(exactMessage), is(true));
    }

    @Test
    public void testMatches_noMatch_returnsFalse() {
        // Given
        String exactMessage = a($String());
        ReceivedMessageMatcher receivedMessageMatcher = getReceivedMessageMatcher(exactMessage);
        ReplyMessageDefinition replyMessageDefinition = getReplyMessages(a($String()));

        StubbingDefinition stubbingDefinition = new StubbingDefinition(receivedMessageMatcher, replyMessageDefinition);

        // Then
        assertThat(stubbingDefinition.matches(a($String())), is(false));
    }

    @Test
    public void testExplain() {
        // Given
        String exactMessage = a($String());
        ReceivedMessageMatcher receivedMessageMatcher = getReceivedMessageMatcher(exactMessage);
        ReplyMessageDefinition replyMessageDefinition = getReplyMessages(a($String()));

        StubbingDefinition stubbingDefinition = new StubbingDefinition(receivedMessageMatcher, replyMessageDefinition);

        // Then
        assertThat(stubbingDefinition.explain(), is(receivedMessageMatcher.explain()));
    }

    @Test
    public void testGetReplyMessage() {
        // Given
        String exactMessage = a($String());
        ReceivedMessageMatcher receivedMessageMatcher = getReceivedMessageMatcher(exactMessage);
        String replyMessage = a($String());
        ReplyMessageDefinition replyMessageDefinition = getReplyMessages(replyMessage);

        StubbingDefinition stubbingDefinition = new StubbingDefinition(receivedMessageMatcher, replyMessageDefinition);

        // Then
        assertThat(stubbingDefinition.getReplyMessage(exactMessage), is(replyMessage));
    }

    private ReceivedMessageMatcher getReceivedMessageMatcher(String message) {
        return new TestingReceivedExactMessageMatcher(message);
    }

    private ReplyMessages getReplyMessages(String replyMessage) {
        return new ReplyMessages(replyMessage);
    }

    private class TestingReceivedExactMessageMatcher implements ReceivedMessageMatcher {
        private final Object expectedMessage;

        public TestingReceivedExactMessageMatcher(String expectedMessage) {
            this.expectedMessage = expectedMessage;
        }

        @Override
        public boolean matches(Object message) {
            return expectedMessage.equals(message);
        }

        @Override
        public String explain() {
            return "i will match " + expectedMessage.toString();
        }
    }


}