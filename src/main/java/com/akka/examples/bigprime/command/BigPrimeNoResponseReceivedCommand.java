package com.akka.examples.bigprime.command;

import akka.actor.typed.ActorRef;

public class BigPrimeNoResponseReceivedCommand implements BigPrimeManagerCommand {
    public final static long serialVersionUUID = 1L;

    private ActorRef<BigPrimeWorkerCommand> worker;

    public BigPrimeNoResponseReceivedCommand(
        ActorRef<BigPrimeWorkerCommand> worker) {
        this.worker = worker;
    }

    public ActorRef<BigPrimeWorkerCommand> getWorker() {
        return worker;
    }
}