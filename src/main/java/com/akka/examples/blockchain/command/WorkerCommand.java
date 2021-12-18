package com.akka.examples.blockchain.command;

import akka.actor.typed.ActorRef;
import com.akka.examples.blockchain.model.Block;

public class WorkerCommand {
    private Block block;
    private int startNonce;
    private int difficulty;
    private ActorRef<ManagerCommand> controller;

    public WorkerCommand(Block block, int startNonce, int difficulty, ActorRef<ManagerCommand> controller) {
        this.block = block;
        this.startNonce = startNonce;
        this.difficulty = difficulty;
        this.controller = controller;
    }

    public Block getBlock() {
        return block;
    }
    public int getStartNonce() {
        return startNonce;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public ActorRef<ManagerCommand> getController() { return controller; }
}
