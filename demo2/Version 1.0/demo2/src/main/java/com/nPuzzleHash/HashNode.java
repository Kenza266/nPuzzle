package com.nPuzzleHash;

public class HashNode {
    final private int value;
    final private int cost;
    private final HashNode nextNode;

    public HashNode(int value, int cost, HashNode nextNode) {
        this.value = value;
        this.cost = cost;
        this.nextNode = nextNode;
    }
    public int getValue() {
        return this.value;
    }
    public int getCost() {
        return this.cost;
    }
    public HashNode getNextNode() {
        return this.nextNode;
    }
}
