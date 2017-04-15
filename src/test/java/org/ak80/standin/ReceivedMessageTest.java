package org.ak80.standin;

import akka.actor.ActorRef;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.ak80.att.akkatesttools.AkkaTest;
import org.junit.Test;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ReceivedMessageTest extends AkkaTest {

    @Test
    public void testProperties() {
        // Given
        Object message = a($String());
        ActorRef sender = StandIn.standIn(actorSystem);

        // When
        ReceivedMessage receivedMessage = new ReceivedMessage(message, sender);

        // Then
        assertThat(receivedMessage.getMessage(), is(message));
        assertThat(receivedMessage.getSender(), is(sender));
    }

    @Test
    public void testToString() {
        // Given
        Object message = a($String());
        ActorRef sender = StandIn.standIn(actorSystem);

        // When
        ReceivedMessage receivedMessage = new ReceivedMessage(message, sender);

        // Then
        assertThat(receivedMessage.toString(), is("message >"+message+"< from "+sender));
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ReceivedMessage.class)
                .withPrefabValues(ActorRef.class,
                        StandIn.standIn(actorSystem, "red"),
                        StandIn.standIn(actorSystem, "black"))
                .verify();
    }

}