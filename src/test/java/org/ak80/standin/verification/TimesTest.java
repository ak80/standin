package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.ReceivedMessage;
import org.ak80.standin.verification.exception.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.ak80.att.BuilderDsl.*;
import static org.ak80.att.CollectionDsl.$listOf;
import static org.ak80.att.CollectionDsl.listOf;
import static org.ak80.standin.StandinTestDataFactory.$ReceivedMessage;
import static org.ak80.standin.verification.Times.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TimesTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String EXPLANATION = "a message that matched " + a($ReceivedMessage());

    @Test
    public void testNever() {
        assertThat(never().getNumberOfWantedMessages(), is(0L));
        assertThat(never().is(times(0)), is(true));
        assertThat(never().is(times(1)), is(false));
    }

    @Test
    public void testOnce() {
        assertThat(once().getNumberOfWantedMessages(), is(1L));
        assertThat(once().is(times(1)), is(true));
        assertThat(once().is(times(0)), is(false));
        assertThat(once().is(times(2)), is(false));
    }

    @Test
    public void testTime() {
        assertThat(times(5).getNumberOfWantedMessages(), is(5L));
        assertThat(times(5).is(times(5)), is(true));
        assertThat(times(5).is(times(0)), is(false));
        assertThat(times(5).is(times(1)), is(false));
        assertThat(times(5).is(times(4)), is(false));
        assertThat(times(5).is(times(6)), is(false));
    }

    @Test
    public void testNoMessages_when_times_never_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf();
        List<ReceivedMessage> matchedMessages = listOf();
        VerificationMode verificationMode = never();

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testNoMatchWithOneMessage_when_times_never_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(1, () -> new ReceivedMessage($ReceivedMessage(), ActorRef.noSender()));
        List<ReceivedMessage> matchedMessages = listOf();
        VerificationMode verificationMode = never();

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testNoMatchWithManyMessages_when_times_never_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf();
        VerificationMode verificationMode = never();

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testOneMatchWithOneMessage_when_times_never_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(1, $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf(receivedMessages.get(0));
        Times verificationMode = never();

        // Expect
        expectedException.expect(NeverWantedButReceivedError.class);
        expectedException.expectMessage("Verification error:\n    never wanted messages but received 1 while looking for "
                + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testOneMatchWithManyMessages_when_times_never_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf(receivedMessages.get(0));
        Times verificationMode = never();

        // Expect
        expectedException.expect(NeverWantedButReceivedError.class);
        expectedException.expectMessage("Verification error:\n    never wanted messages but received 1 while looking for "
                + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testManyMatchesWithManyMessages_when_times_never_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = $listOf(few(), () -> receivedMessages.get(0));
        Times verificationMode = never();

        // Expect
        expectedException.expect(NeverWantedButReceivedError.class);
        expectedException.expectMessage("Verification error:\n    never wanted messages but received " + few() + " while looking for " + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testNoMessages_when_times_once_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf();
        List<ReceivedMessage> matchedMessages = listOf();
        VerificationMode verificationMode = once();

        // Expect
        expectedException.expect(NoMessagesReceivedError.class);
        expectedException.expectMessage("Verification error:\n    no messages received while looking for " + EXPLANATION);

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testNoMatchWithOneMessage_when_times_once_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = once();

        // Expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("expected message not received, expected " + EXPLANATION
                + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testNoMatchWithManyMessages_when_times_once_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = once();

        // Expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("expected message not received, expected " + EXPLANATION
                + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testOneMatchWithOneMessage_when_times_once_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = listOf(receivedMessages.get(0));
        VerificationMode verificationMode = once();

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testOneMatchWithManyMessages_when_times_once_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = $listOf(few(), () -> receivedMessages.get(0));
        Times verificationMode = once();

        // Expect
        expectedException.expect(TooManyMessagesError.class);
        expectedException.expectMessage("Verification error:\n    more than 1 messages received, received "
                + few() + " while looking for " + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testManyMatchesWithManyMessages_when_times_once_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf(receivedMessages.get(0));
        VerificationMode verificationMode = once();

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testNoMessages_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf();
        List<ReceivedMessage> matchedMessages = listOf();
        VerificationMode verificationMode = times(few());

        // Expect
        expectedException.expect(NoMessagesReceivedError.class);
        expectedException.expectMessage("Verification error:\n    no messages received while looking for " + EXPLANATION);

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testNoMatchWithOneMessage_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(NotEnoughMessagesError.class);
        expectedException.expectMessage("Verification error:\n    fewer than 3 messages received, received 0 while looking for "
                + EXPLANATION + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testNoMatchWithManyMessage_when_times_moreThanOnce() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(NotEnoughMessagesError.class);
        expectedException.expectMessage("Verification error:\n    fewer than 3 messages received, received 0 while looking for "
                + EXPLANATION + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testTooFewMatchesWithAllMatched_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = $listOf(1, () -> receivedMessages.get(0));
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(NotEnoughMessagesError.class);
        expectedException.expectMessage("Verification error:\n    fewer than 3 messages received, received 1 while looking for "
                + EXPLANATION + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testTooFewMatchesWithUnmatched_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = $listOf(1, () -> receivedMessages.get(0));
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(NotEnoughMessagesError.class);
        expectedException.expectMessage("Verification error:\n    fewer than 3 messages received, received 1 while looking for "
                + EXPLANATION + verificationMode.printReceivedMessages(receivedMessages, matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testTooManyMatchesWithAllMatched_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = $listOf(several(), () -> receivedMessages.get(0));
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(TooManyMessagesError.class);
        expectedException.expectMessage("Verification error:\n    more than 3 messages received, received 10 while looking for "
                + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testTooManyMatchesWithUnmatched_when_times_moreThanOnce_FAIL() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = $listOf(several(), () -> receivedMessages.get(0));
        Times verificationMode = times(few());

        // Expect
        expectedException.expect(TooManyMessagesError.class);
        expectedException.expectMessage("Verification error:\n    more than 3 messages received, received 10 while looking for "
                + EXPLANATION + verificationMode.printMatchedMessages(matchedMessages));

        // When
        verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);
    }

    @Test
    public void testExpectedNumberOfMatchedWithAllMatched_when_times_moreThanOnce_OK() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf(a($ReceivedMessage()));
        List<ReceivedMessage> matchedMessages = $listOf(few(), () -> receivedMessages.get(0));
        VerificationMode verificationMode = times(few());

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testExpectedNumberOfMatchedWithUnmatched_when_times_moreThanOnce() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = $listOf(few(), () -> receivedMessages.get(0));
        VerificationMode verificationMode = times(few());

        // When
        boolean verified = verificationMode.verifyMode(matchedMessages, receivedMessages, EXPLANATION);

        // Then
        assertThat(verified, is(true));
    }

    @Test
    public void testPrintReceivedMessages_whenNoneReceived() {
        // Given
        List<ReceivedMessage> receivedMessages = listOf();
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printReceivedMessages(receivedMessages, matchedMessages);

        // Then
        assertThat(prettyPrint, is(""));
    }

    @Test
    public void testPrintReceivedMessages_whenReceivedButNoneMatched() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printReceivedMessages(receivedMessages, matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nReceived messages:\n");
        receivedMessages.forEach(message -> expectedString.append("    " + message + "\n"));
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testPrintReceivedMessages_whenReceivedAllMatched() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = receivedMessages;
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printReceivedMessages(receivedMessages, matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nReceived messages:\n");
        receivedMessages.forEach(message -> expectedString.append("    " + message + " [*matched*]\n"));
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testPrintReceivedMessages_whenReceivedSomeMatched() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        List<ReceivedMessage> matchedMessages = listOf(receivedMessages.get(1));
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printReceivedMessages(receivedMessages, matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nReceived messages:\n");
        expectedString.append("    " + receivedMessages.get(0) + "\n");
        expectedString.append("    " + receivedMessages.get(1) + " [*matched*]\n");
        expectedString.append("    " + receivedMessages.get(2) + "\n");
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testPrintReceivedMessages_whenReceivedTooManyMatched() {
        // Given
        List<ReceivedMessage> receivedMessages = $listOf(few(), $ReceivedMessage());
        ReceivedMessage receivedTwice = receivedMessages.get(1);
        receivedMessages.add(receivedTwice);
        List<ReceivedMessage> matchedMessages = listOf(receivedTwice, receivedTwice);
        Times verificationMode = once();

        // When
        String prettyPrint = verificationMode.printReceivedMessages(receivedMessages, matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nReceived messages:\n");
        expectedString.append("    " + receivedMessages.get(0) + "\n");
        expectedString.append("    " + receivedMessages.get(1) + " [*matched*]\n");
        expectedString.append("    " + receivedMessages.get(2) + "\n");
        expectedString.append("    " + receivedMessages.get(1) + " [*matched too often*]\n");
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testPrintMatchedMessages_whenNoneMatched() {
        // Given
        List<ReceivedMessage> matchedMessages = listOf();
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printMatchedMessages(matchedMessages);

        // Then
        assertThat(prettyPrint, is(""));
    }

    @Test
    public void testPrintMatchedMessages_whenAllMatched() {
        // Given
        List<ReceivedMessage> matchedMessages = $listOf(few(), $ReceivedMessage());
        Times verificationMode = times(few());

        // When
        String prettyPrint = verificationMode.printMatchedMessages(matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nMatched messages:\n");
        matchedMessages.forEach(message -> expectedString.append("    " + message + " [*matched*]\n"));
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testPrintMatchedMessages_whenOneMatched() {
        // Given
        List<ReceivedMessage> matchedMessages = $listOf(few(), $ReceivedMessage());
        Times verificationMode = once();

        // When
        String prettyPrint = verificationMode.printMatchedMessages(matchedMessages);

        // Then
        StringBuilder expectedString = new StringBuilder("\nMatched messages:\n");
        expectedString.append("    " + matchedMessages.get(0) + " [*matched*]\n");
        expectedString.append("    " + matchedMessages.get(1) + "\n");
        expectedString.append("    " + matchedMessages.get(2) + "\n");
        assertThat(prettyPrint, is(expectedString.toString()));
    }

    @Test
    public void testNegative() {
        // Expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the wanted number of messages must not be negative, the given value was -1");

        // When
        times(-1);
    }

    @Test
    public void testNegativeBigger() {
        // Expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the wanted number of messages must not be negative, the given value was -50");

        // When
        times(-50);
    }

}
