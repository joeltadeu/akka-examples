package com.akka.examples.racesimulation.command;

import akka.actor.typed.ActorRef;

public class RaceUpdateCommand implements RaceCommand {
    private static final long serialVersionUID = 1L;
    private ActorRef<RaceCommand> racer;
    private int position;

    public RaceUpdateCommand(ActorRef<RaceCommand> racer, int position) {
        this.racer = racer;
        this.position = position;
    }

    public ActorRef<RaceCommand> getRacer() {
        return racer;
    }

    public int getPosition() {
        return position;
    }
}