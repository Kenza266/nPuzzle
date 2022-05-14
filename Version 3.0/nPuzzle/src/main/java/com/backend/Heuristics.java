package com.backend;

public class Heuristics {

    public int hHamming(State state, State goal) {
        int misplaced=0, temp1=state.getData(), temp2=goal.getData();
        for (int i=0; i<state.getSize() * state.getSize(); i++){
            if(temp1%10 != 0 && temp1%10 != temp2%10)
                misplaced++;
            temp1 = temp1/10;
            temp2 = temp2/10;
        }
        return misplaced;
    }

    public int getDifference(int source, int destination, int size) {
        if (source == destination)
            return 0;
        if (source % size != destination % size)
            if (source % size < destination % size)
                source = (source + 1);
            else
                source = (source - 1);
        else
            if (source < destination)
                source = source + size;
            else
                source = source - size;
        return 1 + getDifference(source, destination, size);
    }

    public int hManhattan(State state, State goal){
        int distance=0;
        for(int tile = 1; tile<state.getSize()*state.getSize(); tile++) {
            distance += getDifference(state.getIndex(tile), goal.getIndex(tile), state.getSize());
        }
        return distance;
    }

    public int hEuclid(State state, State goal) {
        int distance=0;
        for(int tile = 1; tile<state.getSize()*state.getSize(); tile++) {
            distance += Math.pow(getDifference(state.getIndex(tile), goal.getIndex(tile), state.getSize()), 2);
        }
        return (int) Math.sqrt(distance);
    }

}
