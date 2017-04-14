package org.ak80.standin.reply;

/**
 * Defines reply messages to be replied sequentially with the last one used when none is left
 */
public class ReplyMessages implements ReplyMessageDefinition {

    private final Object[] replyMessages;
    private int index = -1;

    public ReplyMessages(Object... replyMessages) {
        this.replyMessages = replyMessages;
    }

    @Override
    public Object getReplyMessage(Object receivedMessage) {
        index = index + 1;
        if (index == replyMessages.length) {
            index = replyMessages.length - 1;
        }
        return replyMessages[index];
    }
}
