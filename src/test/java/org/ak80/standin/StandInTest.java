package org.ak80.standin;

import akka.actor.ActorRef;
import akka.testkit.JavaTestKit;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.standin.stubbing.StandInStubbingForReceives;
import org.ak80.standin.stubbing.StandInStubbingForReply;
import org.ak80.standin.stubbing.StubbingException;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StandInTest extends AkkaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void define_standIn() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // Then
        assertThat(standIn.isTerminated(), is(false));
    }

    @Test
    public void define_standIn_with_name() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem, "name");

        // Then
        assertThat(standIn.path().name(), is("name"));
    }

    @Test
    public void testVerifyStandIn_whenStandIn_noException() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.verify(standIn);
    }

    @Test
    public void testVerifyStandIn_whenNotStandIn_exception() {
        // Given
        ActorRef standIn = new JavaTestKit(actorSystem).getRef();

        // Expect
        expectedException.expect(StubbingException.class);
        expectedException.expectMessage("The ActorRef is not for a StandIn");

        // When
        StandIn.verify(standIn);
    }

    @Test
    public void testWhen_createsStandInStubbingForReceives() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        assertThat(StandIn.when(standIn), instanceOf(StandInStubbingForReceives.class));
    }

    @Test
    public void testWhen_createsStubbingWithActor() {
        // Given
        String name = a($String());
        ActorRef standIn = StandIn.standIn(actorSystem, name);

        // When
       assertThat(standIn.path().name(), is(name));
    }

}
