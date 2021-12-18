package com.akka.examples.simplebehavior;

import akka.actor.typed.ActorSystem;

public class FirstSimpleMain {
    public static void main(String[] args) {
        ActorSystem<String> actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem");
        actorSystem.tell("say hello");
        actorSystem.tell("who are you");
        actorSystem.tell("create a child");
        actorSystem.tell("this is the third message.");
    }
}
