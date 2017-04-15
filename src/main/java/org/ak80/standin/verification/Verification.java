package org.ak80.standin.verification;

import akka.actor.ActorRef;
import org.ak80.standin.StandIn;
import org.ak80.standin.StandInActor;
import org.ak80.standin.verification.exception.StandInInternalException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.ak80.standin.StandIn.DEFAULT_WAIT;

/**
 * Send and receive VerificationDefinition to the StandIn
 * <p>
 * TODO test coverage
 */
class Verification {

    public void doVerificationForReceive(ActorRef standIn, VerificationDefinition verificationDefinition) {
        // TODO test verify
        StandIn.verifyStandIn(standIn);

        Object reply = getVerificationFromActor(standIn, verificationDefinition);

        if (reply instanceof StandInActor.VerificationOkMessage) {
            return;
        }

        if (reply instanceof StandInVerificationException) {
            throw (StandInVerificationException) reply;
        } else {
            throw new StandInInternalException("Internal error, unknown reply from StandIn: " + reply);
        }
    }

    private Object getVerificationFromActor(ActorRef standIn, VerificationDefinition verificationDefinition) {
        CompletableFuture future = (CompletableFuture) akka.pattern.PatternsCS.ask(
                standIn,
                verificationDefinition,
                DEFAULT_WAIT);
        return getReply(future);
    }

    private Object getReply(CompletableFuture future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new StandInInternalException(e);
        }
    }

}
