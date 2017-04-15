package org.ak80.standin;

import akka.actor.ActorRef;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.standin.verification.exception.MessageNotReceivedError;
import org.ak80.standin.verification.exception.NoMessagesReceivedError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.ak80.standin.verification.Times.never;

public class StandInVerificationTest extends AkkaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void verify_exact_message_when_expected_message_was_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // When Then
        StandIn.verify(standIn).receivedEq("hello");
    }

    @Test
    public void verify_exact_message_when_no_message_was_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // expect
        expectedException.expect(NoMessagesReceivedError.class);
        expectedException.expectMessage("Verification error:\n    no messages received while looking for a message equal to >goodbye<");

        // When
        StandIn.verify(standIn).receivedEq("goodbye");
    }

    @Test
    public void verify_exact_message_when_expected_message_was_not_sent_but_others_where() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n    expected message not received, expected a message equal to >goodbye< from any Actor" + "\nReceived messages:\n    message >hello< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).receivedEq("goodbye");
    }

    @Test
    public void verify_message_of_type_when_expected_message_was_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // When Then
        StandIn.verify(standIn).receivedAny(String.class);
    }

    @Test
    public void verify_exact_message_with_actor_when_expected_message_was_sent_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", senderMock);


        // When Then
        StandIn.verify(standIn).from(senderMock).receivedEq("hello");
    }

    @Test
    public void verify_exact_message_with_actor_when_expected_message_was_sent_but_not_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n" +
                "    expected message not received, expected a message equal to >hello< from " + senderMock.path() + "\n"
                + "Received messages:\n" +
                "    message >hello< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).from(senderMock).receivedEq("hello");
    }

    @Test
    public void verify_message_of_type_with_actor_when_expected_message_was_sent_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", senderMock);

        // When Then
        StandIn.verify(standIn).from(senderMock).receivedAny(String.class);
    }

    @Test
    public void verify_message_of_type_with_actor_when_expected_message_was_sent_not_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n" +
                "    expected message not received, expected any message of type class java.lang.String from " + senderMock.path() + "\n"
                + "Received messages:\n" +
                "    message >hello< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).from(senderMock).receivedAny(String.class);
    }

    @Test
    public void verify_message_with_predicate_when_expected_message_was_sent_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", senderMock);

        // When
        StandIn.verify(standIn).from(senderMock).received(message -> message.equals("hello"));
    }

    @Test
    public void verify_message_with_predicate_when_expected_message_was_not_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("goodbye", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n" +
                "    expected message not received, expected a message matching a custom condition from any Actor\n" +
                "Received messages:\n" +
                "    message >goodbye< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).received(message -> message.equals("hello"));
    }

    @Test
    public void verify_message_with_predicate_when_expected_message_was_not_sent_from_actor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        ActorRef senderMock = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n" +
                "    expected message not received, expected a message matching a custom condition from " + senderMock.path() + "\n" +
                "Received messages:\n" +
                "    message >hello< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).from(senderMock).received(message -> message.equals("hello"));
    }

    @Test
    public void verify_once_whenNever_thenFail() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(MessageNotReceivedError.class);
        expectedException.expectMessage("Verification error:\n" +
                "    expected message not received, expected a message equal to >goodbye< from any Actor\n" +
                "Received messages:\n" +
                "    message >hello< from Actor[akka://default/deadLetters]");

        // When
        StandIn.verify(standIn).receivedEq("goodbye");
    }

    @Test
    public void verify_never_whenNever_thenOk() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // When Then
        StandIn.verify(standIn).receivedEq("goodbye", never());
    }

}