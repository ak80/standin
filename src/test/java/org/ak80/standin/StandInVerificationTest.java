package org.ak80.standin;

import akka.actor.ActorRef;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.standin.verification.StandInAssertionError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    public void verify_exact_message_when_expected_message_was_not_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: expected message not received by StandIn; expected a message equal to \"goodbye\"");

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
    public void verify_message_of_type_when_expected_message_was_not_sent() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        standIn.tell("hello", ActorRef.noSender());

        // expect
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: expected message not received by StandIn; expected any message of type class java.lang.Integer");

        // When
        StandIn.verify(standIn).receivedAny(Integer.class);
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
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: message was not sent from the specified Actor " + senderMock.path());

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
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: message was not sent from the specified Actor " + senderMock.path());

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
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: expected message not received by StandIn; expected a message matching a custom condition");

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
        expectedException.expect(StandInAssertionError.class);
        expectedException.expectMessage("Verification error: message was not sent from the specified Actor " + senderMock.path());

        // When
        StandIn.verify(standIn).from(senderMock).received(message -> message.equals("hello"));
    }

    /*

    TODO

    // Check no more message not checked with verify have been received
    StandIn.verify(actor).noMoreMessages();

    // Check no messages at all have been received
    StandIn.verify(actor).noMessages();


     */

}