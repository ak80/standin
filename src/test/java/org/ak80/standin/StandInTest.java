package org.ak80.standin;

import akka.actor.ActorRef;
import akka.testkit.JavaTestKit;
import org.ak80.att.akkatesttools.AkkaTest;
import org.ak80.standin.stubbing.StubbingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.ak80.att.akkatesttools.FutureTools.askReply;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StandInTest extends AkkaTest {

    @Test
    public void define_stanIn() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // Then
        assertThat(standIn.isTerminated(),is(false));
    }

    @Test
    public void define_standIn_with_name() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem,"name");

        // Then
        assertThat(standIn.path().name(),is("name"));
    }

}