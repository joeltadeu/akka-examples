package com.akka.examples.blockchain.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.StashBuffer;
import com.akka.examples.blockchain.command.HashResultCommand;
import com.akka.examples.blockchain.command.ManagerCommand;
import com.akka.examples.blockchain.command.MineBlockCommand;
import com.akka.examples.blockchain.command.WorkerCommand;
import com.akka.examples.blockchain.model.Block;
import com.akka.examples.blockchain.model.HashResult;

public class BlockChainManagerBehavior extends AbstractBehavior<ManagerCommand> {

    private ActorRef<HashResult> sender;
    private Block block;
    private int difficulty;
    private int currentNonce = 0;
    private boolean currentlyMining;
    private StashBuffer<ManagerCommand> stashBuffer;

    private BlockChainManagerBehavior(ActorContext<ManagerCommand> context, StashBuffer<ManagerCommand> stashBuffer) {
        super(context);
        this.stashBuffer = stashBuffer;
    }

    public static Behavior<ManagerCommand> create() {
        return Behaviors.withStash(10,
            stash -> Behaviors.setup(context -> new BlockChainManagerBehavior(context, stash)));
    }

    @Override
    public Receive<ManagerCommand> createReceive() {
        return idleMessageHandler();
    }

    public Receive<ManagerCommand> idleMessageHandler() {
        return newReceiveBuilder()
            .onSignal(Terminated.class, handler -> Behaviors.same())
            .onMessage(MineBlockCommand.class, message -> {
                this.sender = message.getSender();
                this.block = message.getBlock();
                this.difficulty = message.getDifficulty();
                this.currentlyMining = true;
                for (int i = 0; i < 10; i++) {
                    startNextWorker();
                }
                return activeMessageHandler();
            })
            .build();
    }

    public Receive<ManagerCommand> activeMessageHandler() {
        return newReceiveBuilder()
            .onSignal(Terminated.class, handler -> {
                startNextWorker();
                return Behaviors.same();
            })
            .onMessage(HashResultCommand.class, message -> {
                for (ActorRef<Void> child : getContext().getChildren()) {
                    getContext().stop(child);
                }
                this.currentlyMining = false;
                sender.tell(message.getHashResult());
                return stashBuffer.unstashAll(idleMessageHandler());
            })
            .onMessage(MineBlockCommand.class, message -> {
                System.out.println("Delaying a mining request");
                //getContext().getSelf().tell(message);
                if (!stashBuffer.isFull()) {
                    stashBuffer.stash(message);
                }
                return Behaviors.same();
            })
            .build();
    }

    private void startNextWorker() {
        if (currentlyMining) {
            Behavior<WorkerCommand> workerBehavior =
                Behaviors.supervise(BlockChainWorkerBehavior.create()).onFailure(SupervisorStrategy.resume());

            ActorRef<WorkerCommand> worker = getContext().spawn(workerBehavior, "worker" + currentNonce);
            getContext().watch(worker);
            worker.tell(new WorkerCommand(block, currentNonce * 1000, difficulty, getContext().getSelf()));
            currentNonce++;
        }
    }
}
