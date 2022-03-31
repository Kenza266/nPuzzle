package com.nPuzzleHash;

public class Transformation {
    private String state;
    private String transformation;

    public Transformation(String state, String transformation) {
        this.state = state;
        this.transformation = transformation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }
}
