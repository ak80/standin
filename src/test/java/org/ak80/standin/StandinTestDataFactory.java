package org.ak80.standin;

import akka.actor.ActorRef;
import org.ak80.att.Builder;

import static org.ak80.att.BuilderDsl.a;
import static org.ak80.att.ValueTdf.$String;

public class StandinTestDataFactory {

    public static Builder<ReceivedMessage> $ReceivedMessage() {
        return () -> new ReceivedMessage(a($String()), ActorRef.noSender());
    }

}
