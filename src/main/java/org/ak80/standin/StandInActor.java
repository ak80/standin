package org.ak80.standin;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import org.ak80.standin.stubbing.StubbingDefinition;
import org.ak80.standin.verification.VerificationDefinition;
import org.ak80.standin.verification.exception.VerificationError;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StandIn Actor for stubbing and verification
 */
public class StandInActor extends AbstractActor {

    private final List<StubbingDefinition> stubbingDefinitionList = new ArrayList<>();

    private final List<ReceivedMessage> receivedMessages = new ArrayList<>();

    public static Props create() {
        return Props.create(StandInActor.class);
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(IdentifyMessage.class, this::replyIsStandIn)
                .match(StubbingDefinition.class, this::defineStubbing)
                .match(VerificationDefinition.class, this::verifyReceive)
                .matchAny(this::handleMessage)
                .build();
    }

    private void replyIsStandIn(Object o) {
        sender().tell(StandInActor.class, self());
    }

    private void defineStubbing(StubbingDefinition stubbingDefinition) {
        stubbingDefinitionList.add(stubbingDefinition);
    }

    private void handleMessage(Object receivedMessage) {
        recordMessage(receivedMessage);
        replyWithMessageIfStubbed(receivedMessage);
    }

    private void recordMessage(Object receivedMessage) {
        receivedMessages.add(new ReceivedMessage(receivedMessage, sender()));
    }

    private void replyWithMessageIfStubbed(Object receivedMessage) {
        stubbingDefinitionList.stream()
                .filter(stubbingDefinition -> stubbingDefinition.matches(receivedMessage))
                .findFirst()
                .ifPresent(stubbingDefinition -> replyMessage(stubbingDefinition, receivedMessage));
    }

    private void replyMessage(StubbingDefinition stubbingDefinition, Object receivedMessage) {
        sender().tell(stubbingDefinition.getReplyMessage(receivedMessage), ActorRef.noSender());
    }

    private void verifyReceive(VerificationDefinition verificationDefinition) {
        try {
            verificationDefinition.verifyMode(getMatchedMessages(verificationDefinition), receivedMessages);
        } catch (VerificationError verificationError) {
            sender().tell(verificationError, self());
        }
        sender().tell(new VerificationOkMessage(), self());
    }

    private List<ReceivedMessage> getMatchedMessages(VerificationDefinition verificationDefinition) {
        return receivedMessages.stream()
                .filter(verificationDefinition::matches)
                .collect(Collectors.toList());
    }


    public static class IdentifyMessage {

    }

    public static class VerificationOkMessage {

    }

}
