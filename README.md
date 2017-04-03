# standin 

[![Travis master](https://img.shields.io/travis/ak80/standin/master.svg?maxAge=3600)](https://travis-ci.org/ak80/standin) [![Coverage Status](https://coveralls.io/repos/github/ak80/standin/badge.svg?maxAge=3600)](https://coveralls.io/github/ak80/standin?branch=master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/f8066f220fe74d27bacf984c61f5d5d4)](https://www.codacy.com/app/josef-koch/standin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ak80/standin&amp;utm_campaign=Badge_Grade) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ak80.standin/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/org.ak80.standin/standin/) [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

StandIn is a fluent mock framework for akka actors

## Ideas

```java
    // Create StandIn
    ActorSystem actorSystem = ActorSystem.create();
    ActorRef actor = StandIn.standIn(actorSystem);
```

### Stubbing

```java
    // Define reply for every message that is received
    StandIn.when(actor).receivesAny().thenReply("You said hello");

    // Define reply when exact message is received
    StandIn.when(actor).receivesEq("hello").thenReply("You said hello");

    // Define reply when message of a class is received
    StandIn.when(actor).receivesAny(String.class).thenReply("You send me a String");

    // Define reply when received message matches a predicate
    Predicate<Object> condition = msg -> msg.equals("hello");
    StandIn.when(standIn).receives(condition).thenReply("You said hello");

    // Define multiple replies, combine with any of the receive* definitions
    StandIn.when(actor).receivesEq("hello").thenReply("You said hello","Hello Again", "I refuse to say more hello");
    
    // Define reply through function
    Function<Object,Object> replyFunction = msg -> msg.toString();
    StandIn.when(actor).receivesEq("hello").thenReplyWith(replyFunction);
    
    // combine the stubbings, first stubbing wins
    StandIn.when(actor)
        .receivesEq("hello").thenReply("You said hello")
        .receivesEq("goodbye").thenReply("You said goodbye")
        .receivesAny(String.class).thenReply("You said something")
        .receiveAny().thenReply("Say what?");
```

### Verification

```java
    // Check message "hello" was sent
    StandIn.verify(actor).receivedEq("hello");
    
    // Check message of specific type was sent
    StandIn.when(actor).receivedAny(String.class);

    // Check message "hello" was sent from specififc actor 
    StandIn.verify(actor).receivedEq("hello").from(sendingActor);

    // Check any message was sent from specififc actor 
    StandIn.verify(actor).receivedAny().from(sendingActor);

    // Check message matches predicate
    Predicate<Object> condition = msg -> msg.equals("hello");
    StandIn.verify(actor).received(condition);
   
    // Check no more message not checked with verify have been received
    StandIn.verify(actor).noMoreMessages();
    
    // Check no messages at all have been received
    StandIn.verify(actor).noMessages();
```


Further ideas:
 * stubbing with from(senderRef)
 * Verify with times, throwing MessageReceivedTooOftenError 
 * Verify with noMoreMessages / noMessages / notReceived() (NeverWantedButReceivedError etc)
 * Functions to get received messages and unmatched messages
 * thenEcho();
 * thenThrow();
 * thenForward(otherActorRef);
 * reportUnmatched() with exception or getUnmatchedMessages()

