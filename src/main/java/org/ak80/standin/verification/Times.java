package org.ak80.standin.verification;

/**
 * Define optionally how often a message is expected to be received
 */
public class Times implements VerificationMode {

    private final long wantedNumberOfMessages;

    /**
     * Define how many times a message is expected to be received
     *
     * @param wantedNumberOfMessages number of expected messages
     */
    Times(long wantedNumberOfMessages) {
        this.wantedNumberOfMessages = wantedNumberOfMessages;
    }

    // TODO test
    @Override
    public boolean verifyMode(long numberOfMatchedMessages) {
        return numberOfMatchedMessages == wantedNumberOfMessages;
    }

    // TODO test factory methods and implement more
    public static Times never() {
        return new Times(0L);
    }

    // TODO test factory methods and implement more
    public static Times once() {
        return new Times(1L);
    }

}
