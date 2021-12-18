package com.akka.examples.racesimulation.command;

public class RaceWorkerStartCommand implements RaceCommand {
    private static final long serialVersionUID = 1L;
    private int raceLength;

    public RaceWorkerStartCommand(int raceLength) {
        this.raceLength = raceLength;
    }

    public int getRaceLength() {
        return raceLength;
    }
}