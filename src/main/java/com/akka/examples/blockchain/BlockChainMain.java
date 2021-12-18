package com.akka.examples.blockchain;

public class BlockChainMain {

    public static void main(String[] args) {
        BlockChainMiner miner = new BlockChainMiner();
        miner.mineBlocks();
    }
}
