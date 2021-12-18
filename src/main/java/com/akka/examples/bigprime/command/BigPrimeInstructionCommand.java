package com.akka.examples.bigprime.command;

import akka.actor.typed.ActorRef;

import java.math.BigInteger;
import java.util.SortedSet;

public class BigPrimeInstructionCommand implements BigPrimeManagerCommand {
    public final static long serialVersionUUID = 1L;
    private String message;
    private ActorRef<SortedSet<BigInteger>> sender;

    public BigPrimeInstructionCommand(String message, ActorRef<SortedSet<BigInteger>> sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public ActorRef<SortedSet<BigInteger>> getSender() {
        return sender;
    }
}