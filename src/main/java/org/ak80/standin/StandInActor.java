package org.ak80.standin;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import org.ak80.standin.stubbing.StubbingDefinition;
import org.ak80.standin.verification.StandInAssertionError;
import org.ak80.standin.verification.VerificationDefinition;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * StandIn Actor for stubbing and verification
 */
public class StandInActor extends AbstractActor {

    private final List<StubbingDefinition> stubbingDefinitionList = new ArrayList<>();

    // TODO create ReceivedMessage class to hold this information so we can use streams also in actor verification
    private final List<Object> receivedMessages = new ArrayList<>();
    private final List<ActorRef> senderOfMessages = new ArrayList<>();

    // TODO move to verification class ?
    private StandInAssertionError assertionError;

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
        receivedMessages.add(receivedMessage);
        senderOfMessages.add(sender());
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
        verifyMessage(verificationDefinition);
        if (matchedOk()) {
            verifySender(verificationDefinition);
        }
        if (matchedOk()) {
            sender().tell(new VerificationOkMessage(), self());
        } else {
            sender().tell(assertionError, self());
        }
    }

    private boolean matchedOk() {
        return assertionError == null;
    }

    private void verifyMessage(VerificationDefinition verificationDefinition) {
        long numberOfMatchedMessages = getNumberOfMatchedMessages(verificationDefinition);

        if (numberOfMatchedMessages != verificationDefinition.getCount()) {
            // TODO print received messages with actor in order
            failVerification(new StandInAssertionError("Verification error: expected message not received by StandIn; expected " + verificationDefinition.explain()));
        }
    }

    private void failVerification(StandInAssertionError standInAssertionError) {
        assertionError = standInAssertionError;
    }

    private long getNumberOfMatchedMessages(VerificationDefinition verificationDefinition) {
        return receivedMessages.stream()
                .filter(verificationDefinition::matches)
                .count();
    }

    private void verifySender(VerificationDefinition verificationDefinition) {
        if (verificationDefinition.getSenderRef().isPresent()) {
            ActorRef expectedSender = verificationDefinition.getSenderRef().get();

            for (int i = 0; i < receivedMessages.size(); i++) {
                if (verificationDefinition.matches(receivedMessages.get(i)) && senderOfMessages.get(i).equals(expectedSender)) {
                    return;
                }
            }
            // TODO print actor names, and other messages from this actor
            failVerification(new StandInAssertionError("Verification error: message was not sent from the specified Actor " + expectedSender.path()));
        }
    }


    public static class IdentifyMessage {

    }

    public static class VerificationOkMessage {

    }

}
