package com.akka.examples.racesimulation.command;

import akka.actor.typed.ActorRef;

public class RacePositionCommand implements RaceCommand {
    private static final long serialVersionUID = 1L;
    private ActorRef<RaceCommand> controller;

    public RacePositionCommand(ActorRef<RaceCommand> controller) {
        this.controller = controller;
    }

    public ActorRef<RaceCommand> getController() {
        return controller;
    }
}