package org.ak80.standin.stubbing;

import akka.actor.ActorRef;
import akka.testkit.JavaTestKit;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.standin.StandIn;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.ak80.att.akkatesttools.FutureTools.askReply;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StandInStubbingForReceivesTest extends AkkaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testVerifyStandIn_whenNotStandIn_exception() {
        // Given
        ActorRef standIn = new JavaTestKit(actorSystem).getRef();

        // Expect
        expectedException.expect(StubbingException.class);
        expectedException.expectMessage("The ActorRef is not for a StandIn");

        // When
        new StandInStubbingForReceives(standIn);
    }

    @Test
    public void testVerifyStandIn_whenDefinedReply_tellActor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesAny();
        replyStubbing.thenReply(message);

        // Then
        assertThat(askReply(a($String()), standIn), is(message));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReplyWith_tellActor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesAny();
        replyStubbing.thenReplyWith((String msg) -> msg.toLowerCase());

        // Then
        assertThat(askReply(message.toUpperCase(), standIn), is(message.toLowerCase()));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReply_returnReceivesStubbing() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesAny();
        replyStubbing.thenReply(message);

        // Then
        assertThat(replyStubbing.thenReply(message), instanceOf(StandInStubbingForReceives.class));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReplyWith_returnReceivesStubbing() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesAny();
        replyStubbing.thenReplyWith((String msg) -> msg.toUpperCase());

        // Then
        assertThat(replyStubbing.thenReply(message), instanceOf(StandInStubbingForReceives.class));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReplyEq_tellActor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String input = a($String());
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesEq(input);
        replyStubbing.thenReply(message);

        // Then
        assertThat(askReply(input, standIn), is(message));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReplCondition_tellActor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String input = a($String());
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receives(msg -> msg.equals(input));
        replyStubbing.thenReply(message);

        // Then
        assertThat(askReply(input, standIn), is(message));
    }

    @Test
    public void testVerifyStandIn_whenDefinedReplyAny_tellActor() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        StandInStubbingForReceives standInStubbingForReceives = new StandInStubbingForReceives(standIn);
        String message = a($String());

        // When
        StandInStubbingForReply replyStubbing = standInStubbingForReceives.receivesAny(String.class);
        replyStubbing.thenReply(message);

        // Then
        assertThat(askReply(a($String()), standIn), is(message));
    }

}