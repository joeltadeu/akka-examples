package com.akka.examples.racesimulation;

import akka.actor.typed.ActorSystem;
import com.akka.examples.racesimulation.behavior.RaceManagerBehavior;
import com.akka.examples.racesimulation.command.RaceCommand;
import com.akka.examples.racesimulation.command.RaceStartCommand;

public class RacerMain {
    public static void main(String[] args) {
        ActorSystem<RaceCommand> raceController = ActorSystem.create(RaceManagerBehavior.create(), "RaceSimulation");
        raceController.tell(new RaceStartCommand());
    }
}
