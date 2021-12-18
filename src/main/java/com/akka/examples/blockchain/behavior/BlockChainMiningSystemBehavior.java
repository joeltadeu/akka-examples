package com.akka.examples.blockchain.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.PoolRouter;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;
import com.akka.examples.blockchain.command.ManagerCommand;

public class BlockChainMiningSystemBehavior extends AbstractBehavior<ManagerCommand> {

    private PoolRouter<ManagerCommand> managerPoolRouter;
    private ActorRef<ManagerCommand> managers;

    private BlockChainMiningSystemBehavior(ActorContext<ManagerCommand> context) {
        super(context);
        managerPoolRouter = Routers.pool(3,
            Behaviors.supervise(BlockChainManagerBehavior.create()).onFailure(SupervisorStrategy.restart())
            );
        managers = getContext().spawn(managerPoolRouter, "managerPool");
    }

    public static Behavior<ManagerCommand> create() {
        return Behaviors.setup(BlockChainMiningSystemBehavior::new);
    }

    @Override
    public Receive<ManagerCommand> createReceive() {
        return newReceiveBuilder()
            .onAnyMessage(message -> {
                managers.tell(message);
                return Behaviors.same();
            })
            .build();
    }
}
