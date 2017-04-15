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


public class StandInStubbingTest extends AkkaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void define_reply_for_every_message() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesAny().thenReply("You send a message");

        // Then
        assertThat(askReply("any", standIn), is("You send a message"));
    }

    @Test
    public void define_reply_for_exact_message() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesEq("hello").thenReply("You said hello");

        // Then
        assertThat(askReply("hello", standIn), is("You said hello"));
    }

    @Test
    public void define_reply_for_message_of_a_class() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesAny(String.class).thenReply("You send me a String");

        // Then
        assertThat(askReply("any", standIn), is("You send me a String"));
    }

    @Test
    public void define_reply_for_message_that_matches_predicate() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        Predicate<Object> condition = msg -> msg.equals("hello");

        // When
        StandIn.when(standIn).receives(condition).thenReply("You said hello");

        // Then
        assertThat(askReply("hello", standIn), is("You said hello"));
    }

    @Test
    public void define_multiple_replies() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesEq("hello").thenReply("You said hello", "Hello again", "I refuse to say more hello");

        // Then
        assertThat(askReply("hello", standIn), is("You said hello"));
        assertThat(askReply("hello", standIn), is("Hello again"));
        assertThat(askReply("hello", standIn), is("I refuse to say more hello"));
        assertThat(askReply("hello", standIn), is("I refuse to say more hello"));
    }

    @Test
    public void define_reply_through_function() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        Function<Object, Object> replyFunction = msg -> msg.toString().toUpperCase();
        StandIn.when(standIn).receivesEq("hello").thenReplyWith(replyFunction);

        // Then
        assertThat(askReply("hello", standIn), is("HELLO"));
    }

    @Test
    public void define_complex() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn)
                .receivesEq("hello").thenReply("You said hello")
                .receivesEq("goodbye").thenReply("You said goodbye")
                .receivesAny(String.class).thenReply("You said something")
                .receivesEq("ahoi").thenReply("will never be replied - wrong order")
                .receivesAny().thenReply("Say what?");

        // Then
        assertThat(askReply("hello", standIn), is("You said hello"));
        assertThat(askReply("goodbye", standIn), is("You said goodbye"));
        assertThat(askReply("any", standIn), is("You said something"));
        assertThat(askReply("ahoi", standIn), is("You said something"));
        assertThat(askReply(Integer.valueOf(1), standIn), is("Say what?"));
    }


}