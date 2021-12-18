package com.akka.examples.bigprime.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.akka.examples.bigprime.command.BigPrimeInstructionCommand;
import com.akka.examples.bigprime.command.BigPrimeManagerCommand;
import com.akka.examples.bigprime.command.BigPrimeNoResponseReceivedCommand;
import com.akka.examples.bigprime.command.BigPrimeResultCommand;
import com.akka.examples.bigprime.command.BigPrimeWorkerCommand;

import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.TreeSet;

public class BigPrimeManagerBehavior extends AbstractBehavior<BigPrimeManagerCommand> {

    private SortedSet<BigInteger> primes = new TreeSet<>();
    private ActorRef<SortedSet<BigInteger>> sender;

    private BigPrimeManagerBehavior(ActorContext<BigPrimeManagerCommand> context) {
        super(context);
    }

    public static Behavior<BigPrimeManagerCommand> create() {
        return Behaviors.setup(BigPrimeManagerBehavior::new);
    }

    @Override
    public Receive<BigPrimeManagerCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(BigPrimeInstructionCommand.class, command -> {
                if (command.getMessage().equals("start")) {
                    this.sender = command.getSender();
                    for (int i = 0; i < 20; i++) {
                        ActorRef<BigPrimeWorkerCommand> worker = getContext().spawn(BigPrimeWorkerBehavior.create(), "worker" + i);
                        askWorkedForPrime(worker);
                    }
                }
                return Behaviors.same();
            })
            .onMessage(BigPrimeResultCommand.class, command -> {
                primes.add(command.getPrime());
                System.out.println("I have received " + primes.size() + " prime numbers");
                if (primes.size() == 20)
                    sender.tell(primes);

                return Behaviors.same();
            })
            .onMessage(BigPrimeNoResponseReceivedCommand.class, command -> {
                System.out.println("Retrying with worker " + command.getWorker().path());
                askWorkedForPrime(command.getWorker());
                return Behaviors.same();
            })
            .build();
    }

    private void askWorkedForPrime(ActorRef<BigPrimeWorkerCommand> worker) {
        getContext().ask(BigPrimeManagerCommand.class, worker, Duration.ofSeconds(5),
            (me) -> new BigPrimeWorkerCommand("start", me),
            (response, throwable) -> {
                if (response == null) {
                    System.out.println("Worker " + worker.path() + "failed to respond");
                    return new BigPrimeNoResponseReceivedCommand(worker);
                }
                return response;
            });
    }
}