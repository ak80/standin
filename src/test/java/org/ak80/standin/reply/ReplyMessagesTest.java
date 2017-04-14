package org.ak80.standin.reply;

import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReplyMessagesTest {

    @Test
    public void testGetReplyMessage_whenOneDefined_returnTheSame() {
        // Given
        String message = a($String());
        ReplyMessageDefinition replyMessages = new ReplyMessages(message);

        // When Then
        assertThat(replyMessages.getReplyMessage(null), is(message));
        assertThat(replyMessages.getReplyMessage(null), is(message));
        assertThat(replyMessages.getReplyMessage(null), is(message));
    }

    @Test
    public void testGetReplyMessage_whenMultipleDefined_returnThemAndTheLastRepeteatedly() {
        // Given
        String message0 = a($String());
        String message1 = a($String());
        String message2 = a($String());
        ReplyMessageDefinition replyMessages = new ReplyMessages(message0, message1, message2);

        // When Then
        assertThat(replyMessages.getReplyMessage(null), is(message0));
        assertThat(replyMessages.getReplyMessage(null), is(message1));
        assertThat(replyMessages.getReplyMessage(null), is(message2));
        assertThat(replyMessages.getReplyMessage(null), is(message2));
        assertThat(replyMessages.getReplyMessage(null), is(message2));
        assertThat(replyMessages.getReplyMessage(null), is(message2));
    }

}