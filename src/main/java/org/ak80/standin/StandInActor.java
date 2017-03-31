package org.ak80.standin;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * StandIn Actor for stubbing and verification
 */
public class StandInActor extends AbstractActor {

    private final List<StubbingDefinition> stubbingDefinitionList = new ArrayList<>();

    public static Props create() {
        return Props.create(StandInActor.class);
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(IdentifyMessage.class,this::replyIsStandIn)
                .match(StubbingDefinition.class,this::defineStubbing)
                .matchAny(this::handleMessage)
                .build();
    }

    private void replyIsStandIn(Object o) {
        sender().tell(StandInActor.class,self());
    }

    private void defineStubbing(StubbingDefinition stubbingDefinition) {
        stubbingDefinitionList.add(stubbingDefinition);
    }

    private void handleMessage(Object receivedMessage) {
        stubbingDefinitionList.stream()
                .filter(stubbingDefinition -> stubbingDefinition.matches(receivedMessage))
                .findFirst()
                .ifPresent(stubbingDefinition -> replyMessage(stubbingDefinition,receivedMessage));
    }

    private void replyMessage(StubbingDefinition stubbingDefinition, Object receivedMessage) {
        sender().tell(stubbingDefinition.getReplyMessage(receivedMessage), ActorRef.noSender());
    }

    public static class IdentifyMessage {

    }

}
