package com.nPuzzleHash;

import java.util.ArrayList;

public class Result {
    public ArrayList<Transformation> path;
    public int open;
    public long time;
    public int level;
    public int maxNodes;
    public String report;

    public Result(ArrayList<Transformation> path, int open, long time, int level, int maxNodes, String report) {
        this.path = path;
        this.open = open;
        this.time = time;
        this.level = level;
        this.maxNodes = maxNodes;
        this.report = report;
    }
}
