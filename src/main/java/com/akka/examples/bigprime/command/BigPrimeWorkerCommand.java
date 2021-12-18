package com.akka.examples.bigprime.command;

import akka.actor.typed.ActorRef;

import java.io.Serializable;

public class BigPrimeWorkerCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private ActorRef<BigPrimeManagerCommand> sender;

    public BigPrimeWorkerCommand(String message, ActorRef<BigPrimeManagerCommand> sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public ActorRef<BigPrimeManagerCommand> getSender() {
        return sender;
    }
}