package org.ak80.standin;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import org.ak80.standin.stubbing.StandInStubbingForReceives;
import org.ak80.standin.stubbing.StubbingException;
import org.ak80.standin.verification.StandInVerificationForReceive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * StandIn - static functions to create a StandIn and define stubbing and verification
 */
public class StandIn extends AbstractActor {

    /**
     * Create a StandIn, a stub/mock Actor
     *
     * @param actorSystem the actor system in which the actor is created
     * @return the StandIn
     */
    public static ActorRef standIn(ActorSystem actorSystem) {
        return actorSystem.actorOf(StandInActor.create());
    }

    /**
     * Create a StandIn, a stub/mock Actor with a name
     *
     * @param actorSystem the actor system in which the actor is created
     * @param name the actors name
     * @return the StandIn
     */
    public static ActorRef standIn(ActorSystem actorSystem, String name) {
        return actorSystem.actorOf(StandInActor.create(),name);
    }

    /**
     * Start stubbing for a standIn
     *
     * @param standIn
     */
    public static StandInStubbingForReceives when(ActorRef standIn) {
        return new StandInStubbingForReceives(standIn);
    }

    /**
     * Check that the actor is a stand in - or throw an exception
     *
     * @param standIn the stand in to check
     */
    public static void verifyStandIn(ActorRef standIn) {
        CompletableFuture future = (CompletableFuture) PatternsCS.ask(standIn, new StandInActor.IdentifyMessage(), 1000L);
        if (!replyIsStandIn(future)) {
            throw new StubbingException("The ActorRef is not for a StandIn");
        }
    }

    private static boolean replyIsStandIn(CompletableFuture future) {
        try {
            return future.get().equals(StandInActor.class);
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public static StandInVerificationForReceive verify(ActorRef standIn) {
        return new StandInVerificationForReceive(standIn);
    }

}
