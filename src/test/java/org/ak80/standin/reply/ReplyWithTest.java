package org.ak80.standin.reply;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReplyWithTest {

    @Test
    public void testGetReplyMessage_whenOneDefined_returnTheSame() {
        // Given
        String message = a($String()).toUpperCase();
        ReplyMessageDefinition replyMessages = new ReplyWith((String string) -> string.toLowerCase());

        // When Then
        assertThat(replyMessages.getReplyMessage(message),is(message.toLowerCase()));
    }

}