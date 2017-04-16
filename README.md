# standin 

[![Travis master](https://img.shields.io/travis/ak80/standin/master.svg?maxAge=3600)](https://travis-ci.org/ak80/standin) [![Coverage Status](https://coveralls.io/repos/github/ak80/standin/badge.svg?maxAge=3600)](https://coveralls.io/github/ak80/standin?branch=master) [![Codacy grade](https://img.shields.io/codacy/grade/f8066f220fe74d27bacf984c61f5d5d4/master.svg?maxAge=3600)](https://www.codacy.com/app/josef-koch/standin/dashboard) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ak80.standin/standin-core/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/org.ak80.standin/standin-core/) [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

StandIn is a fluent mock framework for akka actors

## Create a StandIn

```java
// Create StandIn
ActorSystem actorSystem = ActorSystem.create();
ActorRef standIn = StandIn.standIn(actorSystem);

// ... with a name
ActorRef standInWithName = StandIn.standIn(actorSystem,"name");
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

Verify message received

```java
// Check message "hello" was received
StandIn.verify(actor).receivedEq("hello");

// Check message of specific type was received
StandIn.when(actor).receivedAny(String.class);

// Check message matches predicate
Predicate<Object> condition = msg -> msg.equals("hello");
StandIn.verify(actor).received(condition);
```

Verify message received from actor

```java
// Check message "hello" was received from specififc actor 
StandIn.verify(actor).from(sendingActor).receivedEq("hello");

// Check any message was received from specififc actor 
StandIn.verify(actor).from(sendingActor).receivedAny();

// .. and so on
    
```

Veriy messages have been received an exact number of times
```java
// Check message "hello" was never received
StandIn.verify(actor).receivedEq("hello", never());

// Check message "hello" was received exactly once (default)
StandIn.verify(actor).receivedEq("hello", once());

// Check message "hello" was received exactly 5 times
StandIn.verify(actor).receivedEq("hello", times(5));

// Check message "hello" was received exactly 5 times from specific actor
StandIn.verify(actor).from(sendingActor).receivedEq("hello", times(5));

// ... and so on
```
 
Further ideas:
 * stubbing with from(senderRef) like with verification
 * Verify with noMoreMessages, throwing UnverifiedMessagesError
 * Verify with noMessages(), throwing MessagesReceivedError
 * Functions to get received messages and unmatched messages for(StandIn).from()
 * thenEcho()
 * thenThrow()
 * thenForward(otherActorRef)
 * increase coverage
 * for(standIn).whenSender(optionalReceiver).getAllMessages(), message(), allMessages(exact/condition/class) dto. with messages()
 * in StandInStubbing for receive, detect multiple matches. so not the first one wins, but accidental use and defining to many matching matchers gives and exception
