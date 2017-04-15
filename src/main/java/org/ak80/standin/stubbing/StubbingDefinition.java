package org.ak80.standin.stubbing;

import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.reply.ReplyMessageDefinition;

/**
 * Definition of stubbing combining receive and reply
 */
public final class StubbingDefinition implements ReceivedMessageMatcher {

    private final ReceivedMessageMatcher messageMatcher;
    private final ReplyMessageDefinition replyDefinition;

    public StubbingDefinition(ReceivedMessageMatcher messageMatcher, ReplyMessageDefinition replyDefinition) {
        this.messageMatcher = messageMatcher;
        this.replyDefinition = replyDefinition;
    }

    @Override
    public boolean matches(Object message) {
        return messageMatcher.matches(message);
    }

    @Override
    public String explain() {
        return messageMatcher.explain();
    }

    public Object getReplyMessage(Object receivedMessage) {
        return replyDefinition.getReplyMessage(receivedMessage);
    }
}
