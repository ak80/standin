package org.ak80.standin;

import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.reply.ReplyMessageDefinition;

/**
 * Definition of stubbing combining receive and reply
 */
public class StubbingDefinition implements ReceivedMessageMatcher {

    private final ReceivedMessageMatcher receivedMessageMatcher;
    private final ReplyMessageDefinition replyMessagesDefinition;

    public StubbingDefinition(ReceivedMessageMatcher receivedMessageMatcher, ReplyMessageDefinition replyMessagesDefinition) {
        this.receivedMessageMatcher = receivedMessageMatcher;
        this.replyMessagesDefinition = replyMessagesDefinition;
    }

    @Override
    public boolean matches(Object message) {
        return receivedMessageMatcher.matches(message);
    }

    public Object getReplyMessage(Object receivedMessage) {
        return replyMessagesDefinition.getReplyMessage(receivedMessage);
    }
}
