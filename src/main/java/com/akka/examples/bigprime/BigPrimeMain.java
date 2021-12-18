package com.akka.examples.bigprime;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import com.akka.examples.bigprime.behavior.BigPrimeManagerBehavior;
import com.akka.examples.bigprime.command.BigPrimeInstructionCommand;
import com.akka.examples.bigprime.command.BigPrimeManagerCommand;

import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.concurrent.CompletionStage;

public class BigPrimeMain {
    public static void main(String[] args) {
        ActorSystem<BigPrimeManagerCommand> bigPrimes = ActorSystem.create(BigPrimeManagerBehavior.create(), "BigPrimes");
        CompletionStage<SortedSet<BigInteger>> result = AskPattern.ask(bigPrimes,
            (me) -> new BigPrimeInstructionCommand("start", me),
            Duration.ofSeconds(20),
            bigPrimes.scheduler());

        result.whenComplete(
            (reply, failure) -> {
                if (reply != null) {
                    reply.forEach(System.out::println);
                } else {
                    System.out.println("The system didnt respond in time");
                }
                bigPrimes.terminate();
            }
        );
    }
}
