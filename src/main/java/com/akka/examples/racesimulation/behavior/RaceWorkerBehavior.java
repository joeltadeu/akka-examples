package com.akka.examples.racesimulation.behavior;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.akka.examples.racesimulation.command.RaceCommand;
import com.akka.examples.racesimulation.command.RaceFinishedCommand;
import com.akka.examples.racesimulation.command.RacePositionCommand;
import com.akka.examples.racesimulation.command.RaceWorkerStartCommand;
import com.akka.examples.racesimulation.command.RaceUpdateCommand;

import java.util.Random;

public class RaceWorkerBehavior extends AbstractBehavior<RaceCommand> {

    private final double defaultAverageSpeed = 48.2;
    private int averageSpeedAdjustmentFactor;
    private Random random;

    private double currentSpeed = 0;



    private RaceWorkerBehavior(ActorContext<RaceCommand> context) {
        super(context);
    }

    public static Behavior<RaceCommand> create() {
        return Behaviors.setup(RaceWorkerBehavior::new);
    }

    public Receive<RaceCommand> notYetStarted() {
        return newReceiveBuilder()
            .onMessage(RaceWorkerStartCommand.class, message -> {
                this.random = new Random();
                this.averageSpeedAdjustmentFactor = random.nextInt(30) - 10;
                return running(message.getRaceLength(), 0);
            })
            .onMessage(RacePositionCommand.class, message -> {
                message.getController().tell(new RaceUpdateCommand(getContext().getSelf(), 0));
                return Behaviors.same();
            })
            .build();
    }

    public Receive<RaceCommand> running(int raceLength, int currentPosition) {
        return newReceiveBuilder()
            .onMessage(RacePositionCommand.class, message -> {
                determineNextSpeed(currentPosition, raceLength  );
                int newPosition = currentPosition;
                newPosition += getDistanceMovedPerSecond();
                if (newPosition > raceLength )
                    newPosition  = raceLength;
                message.getController().tell(new RaceUpdateCommand(getContext().getSelf(), (int) newPosition));
                if (newPosition == raceLength) {
                    return completed(raceLength);
                }
                return running(raceLength, newPosition);
            })
            .build();
    }

    public Receive<RaceCommand> completed(int raceLength) {
        return newReceiveBuilder()
            .onMessage(RacePositionCommand.class, message -> {
                message.getController().tell(new RaceUpdateCommand(getContext().getSelf(), raceLength));
                message.getController().tell(new RaceFinishedCommand(getContext().getSelf()));
                return waitingToStop();
            })
            .build();
    }

    public Receive<RaceCommand> waitingToStop() {
        return newReceiveBuilder()
            .onAnyMessage(message -> Behaviors.same())
            .onSignal(PostStop.class, signal -> {
                getContext().getLog().info("I'm about to terminate!");
                return Behaviors.same();
            })
            .build();
    }

    @Override
    public Receive<RaceCommand> createReceive() {
        return notYetStarted();
    }

    private double getMaxSpeed() {
        return defaultAverageSpeed * (1+((double)averageSpeedAdjustmentFactor / 100));
    }

    private double getDistanceMovedPerSecond() {
        return currentSpeed * 1000 / 3600;
    }

    private void determineNextSpeed(int currentPosition, int raceLength) {
        if (currentPosition < (raceLength / 4)) {
            currentSpeed = currentSpeed  + (((getMaxSpeed() - currentSpeed) / 10) * random.nextDouble());
        }
        else {
            currentSpeed = currentSpeed * (0.5 + random.nextDouble());
        }

        if (currentSpeed > getMaxSpeed())
            currentSpeed = getMaxSpeed();

        if (currentSpeed < 5)
            currentSpeed = 5;

        if (currentPosition > (raceLength / 2) && currentSpeed < getMaxSpeed() / 2) {
            currentSpeed = getMaxSpeed() / 2;
        }
    }
}
