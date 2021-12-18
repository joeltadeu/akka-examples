package com.akka.examples.bigprime.command;

import java.math.BigInteger;

public class BigPrimeResultCommand implements BigPrimeManagerCommand {
    public final static long serialVersionUUID = 1L;
    private BigInteger prime;

    public BigPrimeResultCommand(BigInteger prime) {
        this.prime = prime;
    }

    public BigInteger getPrime() {
        return prime;
    }
}
