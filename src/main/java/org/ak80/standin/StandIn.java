package org.ak80.standin;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * StandIn - static functions to create a StandIn and define stubbing
 */
public class StandIn extends AbstractActor {

    /**
     * Create a StandIn, a stub/mock Actor
     * @param actorSystem the actor system in which the actor is created
     * @return the StandIn
     */
    public static ActorRef standIn(ActorSystem actorSystem) {
        return actorSystem.actorOf(StandInActor.create());
    }

    /**
     * Start stubbing for a standIn
     * @param standIn
     */
    public static StandInStubbingForReceives when(ActorRef standIn) {
        return new StandInStubbingForReceives(standIn);
    }

    /**
     * Check that the actor is a stand in - or throw an exception
     * @param standIn the stand in to check
     */
    public static void verifyStandIn(ActorRef standIn) {
        CompletableFuture future = (CompletableFuture) PatternsCS.ask(standIn, new StandInActor.IdentifyMessage(), 1000L);
        if(!replyIsStandIn(future)) {
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
}
