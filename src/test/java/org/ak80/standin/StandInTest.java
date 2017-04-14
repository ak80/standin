package org.ak80.standin;

import akka.actor.ActorRef;
import org.ak80.att.akkatesttools.AkkaTest;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StandInTest extends AkkaTest {

    @Test
    public void define_stanIn() {
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

}