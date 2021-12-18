package com.akka.examples.blockchain.command;

import com.akka.examples.blockchain.model.HashResult;

import java.util.Objects;

public class HashResultCommand implements ManagerCommand {
    private static final long serialVersionUID = 1l;
    private HashResult hashResult;

    public HashResultCommand(HashResult hashResult) {
        this.hashResult = hashResult;
    }

    public HashResult getHashResult() {
        return hashResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashResultCommand that = (HashResultCommand) o;
        return Objects.equals(hashResult, that.hashResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashResult);
    }
}
