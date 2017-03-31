package org.ak80.standin.reply;

import java.util.function.Function;

/**
 * Defines a function to generate replies
 */
public class ReplyWith implements ReplyMessageDefinition {

    private final Function<Object, Object> replyFunction;

    public ReplyWith(Function<Object, Object> replyFunction) {
        this.replyFunction = replyFunction;
    }

    @Override
    public Object getReplyMessage(Object receivedMessage) {
        return replyFunction.apply(receivedMessage);
    }
}
