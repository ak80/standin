package org.ak80.standin.reply;

/**
 * Defines reply message(s)
 */
public interface ReplyMessageDefinition {

    /**
     * Get reply message, optionally based on received message
     *
     * @param receivedMessage the received message
     * @return the reply message
     */
    <T extends Object> Object getReplyMessage(T receivedMessage);
}
