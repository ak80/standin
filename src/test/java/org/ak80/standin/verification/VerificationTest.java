package org.ak80.standin.verification;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestActor;
import akka.testkit.TestActorRef;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.att.akkatesttools.EchoActor;
import org.ak80.standin.StandIn;
import org.ak80.standin.matcher.ReceivedExactMessageMatcher;
import org.ak80.standin.matcher.ReceivedMessageMatcher;
import org.ak80.standin.stubbing.StubbingException;
import org.ak80.standin.verification.exception.NoMessagesReceivedError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.ak80.standin.verification.Times.never;
import static org.ak80.standin.verification.Times.once;


public class VerificationTest extends AkkaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDoVerificationForReceive_whenNotStandIn_thenException() {
        // Given
        Verification verification = new Verification();

        ActorRef notStandIn = TestActorRef.create(actorSystem, Props.create(EchoActor.class));

        // Expect
        expectedException.expect(StubbingException.class);
        expectedException.expectMessage("The ActorRef is not for a StandIn");

        ReceivedMessageMatcher matcher = new ReceivedExactMessageMatcher("hello");
        Optional<ActorRef> sender = Optional.empty();
        VerificationMode mode = once();

        // When
        verification.doVerificationForReceive(notStandIn, new VerificationDefinition(matcher, sender, mode));
    }

    @Test
    public void testDoVerificationForReceive_whenNotMatched_thenException() {
        // Given
        Verification verification = new Verification();

        ActorRef standIn = StandIn.standIn(actorSystem);

        ReceivedMessageMatcher matcher = new ReceivedExactMessageMatcher("hello");
        Optional<ActorRef> sender = Optional.empty();
        VerificationMode mode = once();

        // Expect
        expectedException.expect(NoMessagesReceivedError.class);

        // When
        verification.doVerificationForReceive(standIn, new VerificationDefinition(matcher, sender, mode));
    }

    @Test
    public void testDoVerificationForReceive_whenMatched_noException() {
        // Given
        Verification verification = new Verification();

        ActorRef standIn = StandIn.standIn(actorSystem);

        ReceivedMessageMatcher matcher = new ReceivedExactMessageMatcher("hello");
        Optional<ActorRef> sender = Optional.empty();
        VerificationMode mode = never();

        // When
        verification.doVerificationForReceive(standIn, new VerificationDefinition(matcher, sender, mode));

        // Then
        assert true;
    }

}