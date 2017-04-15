package org.ak80.standin.verification;

import org.ak80.standin.ReceivedMessage;
import org.ak80.standin.verification.exception.*;

import java.util.List;

/**
 * Define optionally how often a message is expected to be received
 */
public class Times implements VerificationMode {

    public static final String MATCHED = " [*matched*]";
    public static final String MATCHED_TOO_OFTEN = " [*matched too often*]";
    //TODO test coverage
    private final long wantedNumberOfMessages;
    private long matchedMessagesCount;

    /**
     * Define how many times a message is expected to be received
     *
     * @param wantedNumberOfMessages number of expected messages
     */
    Times(long wantedNumberOfMessages) {
        // TODO throw exception when negative
        this.wantedNumberOfMessages = wantedNumberOfMessages;
    }

    public static Times never() {
        return new Times(0L);
    }

    public static Times once() {
        return new Times(1L);
    }

    public static Times times(int wantedNumberOfMessages) {
        return new Times(wantedNumberOfMessages);
    }

    @Override
    public boolean verifyMode(List<ReceivedMessage> matchedMessages, List<ReceivedMessage> receivedMessages, String explanation) {
        checkIfAnyReceived(receivedMessages.size(), explanation);
        checkIfMatchWanted(matchedMessages, receivedMessages, explanation);
        return true;
    }

    private void checkIfAnyReceived(long numberOfReceivedMessages, String explanation) {
        if (numberOfReceivedMessages == 0) {
            if (getNumberOfWantedMessages() > 0) {
                throw new NoMessagesReceivedError("no messages received while looking for " + explanation);
            }
        }
    }

    private void checkIfMatchWanted(List<ReceivedMessage> matchedMessages, List<ReceivedMessage> receivedMessages, String explanation) {
        if (matchedButNotWanted(matchedMessages.size())) {
            throw new NeverWantedButReceivedError(
                    String.format("never wanted messages but received %s while looking for %s",
                            matchedMessages.size(), explanation) + printMatchedMessages(matchedMessages));
        }
        if (receivedOneButNotTheOneWanted(receivedMessages.size(), matchedMessages.size())) {
            throw new MessageNotReceivedError("expected message not received, expected "
                    + explanation + printReceivedMessages(receivedMessages, matchedMessages));
        }
        if (matchedTooManyMessages(matchedMessages.size())) {
            throw new TooManyMessagesError(
                    String.format("more than %s messages received, received %s while looking for %s",
                            getNumberOfWantedMessages(), matchedMessages.size(), explanation)
                            + printMatchedMessages(matchedMessages));

        }
        if (matchedTooFewMessages(matchedMessages.size()) && getNumberOfWantedMessages() == 1) {
            throw new MessageNotReceivedError(
                    String.format("expected message not received, expected %s",
                            explanation) + printReceivedMessages(receivedMessages, matchedMessages));
        }
        if (matchedTooFewMessages(matchedMessages.size())) {
            throw new NotEnoughMessagesError(
                    String.format("fewer than %s messages received, received %s while looking for %s",
                            getNumberOfWantedMessages(), matchedMessages.size(), explanation) +
                            printReceivedMessages(receivedMessages, matchedMessages));
        }
    }

    private boolean matchedButNotWanted(long numberOfMatchedMessages) {
        boolean matchedMessages = numberOfMatchedMessages > 0;
        boolean notWantedMessages = getNumberOfWantedMessages() == 0;
        return matchedMessages && notWantedMessages;
    }

    private boolean receivedOneButNotTheOneWanted(long numberOfReceivedMessage, long numberOfMatchedMessages) {
        boolean receivedOne = numberOfReceivedMessage == 1;
        boolean matchedNone = numberOfMatchedMessages == 0;
        boolean wantedMessages = getNumberOfWantedMessages() == 1;
        return receivedOne && matchedNone && wantedMessages;
    }

    private boolean matchedTooManyMessages(long numberOfMatchedMessages) {
        return numberOfMatchedMessages > getNumberOfWantedMessages();
    }

    private boolean matchedTooFewMessages(long numberOfMatchedMessages) {
        return numberOfMatchedMessages < getNumberOfWantedMessages();
    }

    // TODO later print from actor
    String printReceivedMessages(List<ReceivedMessage> receivedMessages, List<ReceivedMessage> matchedMessages) {
        if (receivedMessages.isEmpty()) {
            return "";
        }
        StringBuilder prettyPrinted = new StringBuilder("\nReceived messages:\n");
        matchedMessagesCount = 0;
        receivedMessages.forEach(receivedMessage -> prettyPrinted.append(printReceivedMessage(receivedMessage, matchedMessages)));
        return prettyPrinted.toString();
    }

    private String printReceivedMessage(ReceivedMessage receivedMessage, List<ReceivedMessage> matchedMessages) {
        return "    " + receivedMessage + printMatched(receivedMessage, matchedMessages) + "\n";
    }

    private String printMatched(ReceivedMessage receivedMessage, List<ReceivedMessage> matchedMessages) {
        if (matchedMessages.contains(receivedMessage)) {
            matchedMessagesCount = matchedMessagesCount + 1;
            if (matchedMessagesCount <= getNumberOfWantedMessages()) {
                return MATCHED;
            } else {
                return MATCHED_TOO_OFTEN;
            }
        } else {
            return "";
        }
    }

    String printMatchedMessages(List<ReceivedMessage> matchedMessages) {
        if (matchedMessages.isEmpty()) {
            return "";
        }
        long expectedMessages = getNumberOfWantedMessages();
        StringBuilder prettyPrinted = new StringBuilder("\nMatched messages:\n");
        for (int i = 0; i < matchedMessages.size(); i++) {
            prettyPrinted.append("    " + matchedMessages.get(i));
            if (i < expectedMessages) {
                prettyPrinted.append(MATCHED);
            }
            prettyPrinted.append("\n");
        }
        return prettyPrinted.toString();
    }

    public long getNumberOfWantedMessages() {
        return wantedNumberOfMessages;
    }

    public boolean is(Times times) {
        return wantedNumberOfMessages == times.getNumberOfWantedMessages();
    }
}
