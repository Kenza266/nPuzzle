package com.nPuzzleHash;

public class Node {

    private final State state;
    private final Node parent;
    private final int cost;
    private final int level;

    public Node(State state, Node parent, int cost, int level) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.level = level;
    }

    public State getState() {
        return state;
    }
    public Node getParent() {
        return parent;
    }
    public int getCost() {
        return cost;
    }
    public int getLevel() {
        return level;
    }

}
