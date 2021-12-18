package com.akka.examples.racesimulation.command;

import akka.actor.typed.ActorRef;

public class RaceFinishedCommand implements RaceCommand {
    private static final long serialVersionUID = 1L;
    private ActorRef<RaceCommand> racer;

    public RaceFinishedCommand(ActorRef<RaceCommand> racer) {
        this.racer = racer;
    }

    public ActorRef<RaceCommand> getRacer() {
        return racer;
    }
}