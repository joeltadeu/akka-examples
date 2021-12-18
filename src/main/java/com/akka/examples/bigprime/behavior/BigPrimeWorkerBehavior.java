package com.akka.examples.bigprime.behavior;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.akka.examples.bigprime.command.BigPrimeResultCommand;
import com.akka.examples.bigprime.command.BigPrimeWorkerCommand;

import java.math.BigInteger;
import java.util.Random;

public class BigPrimeWorkerBehavior extends AbstractBehavior<BigPrimeWorkerCommand> {

    private BigPrimeWorkerBehavior(ActorContext<BigPrimeWorkerCommand> context) {
        super(context);
    }

    public static Behavior<BigPrimeWorkerCommand> create() {
        return Behaviors.setup(BigPrimeWorkerBehavior::new);
    }

    @Override
    public Receive<BigPrimeWorkerCommand> createReceive() {
        return handleMessagesWhenWeDontYetHavePrimeNumber();
    }

    public Receive<BigPrimeWorkerCommand> handleMessagesWhenWeDontYetHavePrimeNumber() {
        return newReceiveBuilder().onAnyMessage(command -> {
            BigInteger bigInteger = new BigInteger(2000, new Random());
            BigInteger prime = bigInteger.nextProbablePrime();
            command.getSender().tell(new BigPrimeResultCommand(prime));
            return handleMessagesWhenWeAlreadyHavePrimeNumber(prime);
        }).build();
    }

    public Receive<BigPrimeWorkerCommand> handleMessagesWhenWeAlreadyHavePrimeNumber(BigInteger prime) {
        return newReceiveBuilder().onAnyMessage(command -> {
            command.getSender().tell(new BigPrimeResultCommand(prime));
            return Behaviors.same();
        }).build();
    }

}
