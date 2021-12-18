package com.akka.examples.racesimulation.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.akka.examples.racesimulation.command.RaceCommand;
import com.akka.examples.racesimulation.command.RaceFinishedCommand;
import com.akka.examples.racesimulation.command.RaceGetPositionsCommand;
import com.akka.examples.racesimulation.command.RacePositionCommand;
import com.akka.examples.racesimulation.command.RaceWorkerStartCommand;
import com.akka.examples.racesimulation.command.RaceStartCommand;
import com.akka.examples.racesimulation.command.RaceUpdateCommand;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class RaceManagerBehavior extends AbstractBehavior<RaceCommand> {

    private Object TIMER_KEY;
    private Map<ActorRef<RaceCommand>, Integer> currentPositions;
    private Map<ActorRef<RaceCommand>, Long> finishingTimes;
    private long start;
    private int raceLength = 100;

    private RaceManagerBehavior(ActorContext<RaceCommand> context) {
        super(context);
    }

    public static Behavior<RaceCommand> create() {
        return Behaviors.setup(RaceManagerBehavior::new);
    }

    @Override
    public Receive<RaceCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(RaceStartCommand.class, message -> {
                start = System.currentTimeMillis();
                currentPositions = new HashMap<>();
                finishingTimes = new HashMap<>();
                for (int i =  0; i < 10; i++) {
                    ActorRef<RaceCommand> racer = getContext().spawn(RaceWorkerBehavior.create(), "racer_" + i);
                    currentPositions.put(racer, 0);
                    racer.tell(new RaceWorkerStartCommand(raceLength));
                }
                return Behaviors.withTimers(timer -> {
                    timer.startTimerAtFixedRate(TIMER_KEY, new RaceGetPositionsCommand(), Duration.ofSeconds(1));
                    return Behaviors.same();
                });
            })
            .onMessage(RaceGetPositionsCommand.class, message -> {
                for(ActorRef<RaceCommand> racer : currentPositions.keySet()) {
                    racer.tell(new RacePositionCommand(getContext().getSelf()));
                    displayRace();
                }
                return Behaviors.same();
            })
            .onMessage(RaceUpdateCommand.class, message -> {
                currentPositions.put(message.getRacer(), message.getPosition());
                return Behaviors.same();
            })
            .onMessage(RaceFinishedCommand.class, message -> {
                finishingTimes.put(message.getRacer(), System.currentTimeMillis());
                if (finishingTimes.size() == 10) {
                    return raceCompleteMessageHandler();
                }
                return Behaviors.same();
            })
            .build();
    }

    public Receive<RaceCommand> raceCompleteMessageHandler() {
        return newReceiveBuilder()
            .onMessage(RaceGetPositionsCommand.class, message -> {
                displayResults();
                return Behaviors.withTimers(timers -> {
                    timers.cancelAll();
                    return Behaviors.stopped();
                });
            })
            .build();
    }

    private void displayRace() {
        int displayLength = 160;
        for (int i=0; i < 50; ++i)
            System.out.println();

        System.out.println("Race has been running for " + ((System.currentTimeMillis() - start)/1000) + " seconds.");
        System.out.println("    " + new String (new char[displayLength]).replace('\0', '='));
        int i = 0;
        for (ActorRef<RaceCommand> racer : currentPositions.keySet()) {
            System.out.println(i + " : "  + new String (new char[currentPositions.get(racer) * displayLength / 100]).replace('\0', '*'));
            i++;
        }
    }

    private void displayResults() {
        System.out.println();
        System.out.println("Results");
        finishingTimes.values().stream().sorted().forEach(it -> {
            for (ActorRef<RaceCommand> key : finishingTimes.keySet()) {
                if (finishingTimes.get(key) == it) {
                    String racerId = key.path().toString().substring(key.path().toString().length()-1);
                    System.out.println("Racer " + racerId + " finished in " + ( (double)it - start ) / 1000 + " seconds.");
                }
            }
        });
    }
}
