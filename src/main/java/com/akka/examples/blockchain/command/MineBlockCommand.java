package com.akka.examples.blockchain.command;

import akka.actor.typed.ActorRef;
import com.akka.examples.blockchain.model.Block;
import com.akka.examples.blockchain.model.HashResult;

public class MineBlockCommand implements ManagerCommand {
    private static final long serialVersionUID = 1l;
    private Block block;
    private ActorRef<HashResult> sender;
    private int difficulty;

    public MineBlockCommand(Block block, ActorRef<HashResult> sender, int difficulty) {
        this.block = block;
        this.sender = sender;
        this.difficulty = difficulty;
    }

    public Block getBlock() {
        return block;
    }

    public ActorRef<HashResult> getSender() {
        return sender;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
