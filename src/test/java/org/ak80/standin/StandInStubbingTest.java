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

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;
import static org.ak80.att.akkatesttools.FutureTools.askReply;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StandInStubbingTest extends AkkaTest {

    public static final String YOU_SEND_A_MESSAGE = "You send a message";
    public static final String HELLO = "hello";
    public static final String YOU_SAID_HELLO = "You said hello";
    public static final String YOU_SEND_ME_A_STRING = "You send me a String";
    public static final String GOODBYE = "goodbye";
    public static final String YOU_SAID_GOODBYE = "You said goodbye";
    public static final String HELLO_AGAIN = "Hello again";
    public static final String I_REFUSE_TO_SAY_MORE_HELLO = "I refuse to say more hello";
    public static final String YOU_SAID_SOMETHING = "You said something";
    public static final String AHOI = "ahoi";
    public static final String WILL_NEVER_BE_REPLIED_WRONG_ORDER = "will never be replied - wrong order";
    public static final String SAY_WHAT = "Say what?";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void define_reply_for_every_message() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesAny().thenReply(YOU_SEND_A_MESSAGE);

        // Then
        assertThat(askReply(a($String()), standIn), is(YOU_SEND_A_MESSAGE));
    }

    @Test
    public void define_reply_for_exact_message() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesEq(HELLO).thenReply(YOU_SAID_HELLO);

        // Then
        assertThat(askReply(HELLO, standIn), is(YOU_SAID_HELLO));
    }

    @Test
    public void define_reply_for_message_of_a_class() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesAny(String.class).thenReply(YOU_SEND_ME_A_STRING);

        // Then
        assertThat(askReply(a($String()), standIn), is(YOU_SEND_ME_A_STRING));
    }

    @Test
    public void define_reply_for_message_that_matches_predicate() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);
        Predicate<Object> condition = msg -> msg.equals(HELLO);

        // When
        StandIn.when(standIn).receives(condition).thenReply(YOU_SAID_HELLO);

        // Then
        assertThat(askReply(HELLO, standIn), is(YOU_SAID_HELLO));
    }

    @Test
    public void define_multiple_replies() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn).receivesEq(HELLO).thenReply(YOU_SAID_HELLO, HELLO_AGAIN, I_REFUSE_TO_SAY_MORE_HELLO);

        // Then
        assertThat(askReply(HELLO, standIn), is(YOU_SAID_HELLO));
        assertThat(askReply(HELLO, standIn), is(HELLO_AGAIN));
        assertThat(askReply(HELLO, standIn), is(I_REFUSE_TO_SAY_MORE_HELLO));
        assertThat(askReply(HELLO, standIn), is(I_REFUSE_TO_SAY_MORE_HELLO));
    }

    @Test
    public void define_reply_through_function() {
        // Given
        assert !HELLO.equals(HELLO.toUpperCase());
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        Function<Object, Object> replyFunction = msg -> msg.toString().toUpperCase();
        StandIn.when(standIn).receivesEq(HELLO).thenReplyWith(replyFunction);

        // Then
        assertThat(askReply(HELLO, standIn), is(HELLO.toUpperCase()));
    }

    @Test
    public void define_complex() {
        // Given
        ActorRef standIn = StandIn.standIn(actorSystem);

        // When
        StandIn.when(standIn)
                .receivesEq(HELLO).thenReply(YOU_SAID_HELLO)
                .receivesEq(GOODBYE).thenReply(YOU_SAID_GOODBYE)
                .receivesAny(String.class).thenReply(YOU_SAID_SOMETHING)
                .receivesEq(AHOI).thenReply(WILL_NEVER_BE_REPLIED_WRONG_ORDER)
                .receivesAny().thenReply(SAY_WHAT);

        // Then
        assertThat(askReply(HELLO, standIn), is(YOU_SAID_HELLO));
        assertThat(askReply(GOODBYE, standIn), is(YOU_SAID_GOODBYE));
        assertThat(askReply(a($String()), standIn), is(YOU_SAID_SOMETHING));
        assertThat(askReply(AHOI, standIn), is(YOU_SAID_SOMETHING));
        assertThat(askReply(Integer.valueOf(1), standIn), is(SAY_WHAT));
    }

}
