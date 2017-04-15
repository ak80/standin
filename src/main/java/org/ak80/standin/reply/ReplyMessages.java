package org.ak80.standin.reply;

/**
 * Defines reply messages to be replied sequentially with the last one used when none is left
 */
public final class ReplyMessages implements ReplyMessageDefinition {

    private final Object[] definedMessages;
    private int index = -1;

    public ReplyMessages(Object... definedMessages) {
        this.definedMessages = definedMessages;
    }

    @Override
    public Object getReplyMessage(Object receivedMessage) {
        index = index + 1;
        if (index == definedMessages.length) {
            index = definedMessages.length - 1;
        }
        return definedMessages[index];
    }
}
