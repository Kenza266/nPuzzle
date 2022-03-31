package com.nPuzzleHash;
import java.util.*;
import java.util.List;
import java.lang.Math;

public class State{
    private int data;
    private final int size; //3

    public State(int data, int size) {
        this.data = data;
        this.size = size;
    }

    public int getData() {
        return this.data;
    }

    public int getSize() {
        return size;
    }

    public void display() {
        int x, line=0;
        for(int i=(int) Math.pow(10, this.size*this.size-1); i>0; i=i/10) {
            x = ((this.data - this.data % i) / i) % 10;
            System.out.print(x+"\t");
            line++;
            if (line%this.size == 0)
                System.out.print("\n");
        }
        System.out.println();
    }

    public int getIndex(int value) {
        int temp = this.data, x;
        for (int i=0; i<this.size * this.size; i++) {
            x = temp%10;
            if (x == value)
                return (this.size * this.size - 1) - i;
            temp = temp/10;
        }
        return -1;
    }

    public int getValue(int index) {
        index = this.size * this.size - index;
        int temp = this.data, x=-1;
        for (int i=0; i<index; i++) {
            x = temp%10;
            temp = temp/10;
        }
        return x;
    }

    public void setValue(int index, int value) {
        int temp = this.data, x=0;
        for (int i=0; i<this.size * this.size; i++) {
            if (this.size * this.size - 1 - index == i)
                x += value * (int)Math.pow(10, i);
            else
                x += temp%10 * (int)Math.pow(10, i);
            temp = temp/10;
        }
        this.data = x;
    }

    public State getChild(State parent, int source, int destination) {
        State childState = new State(parent.getData(), parent.getSize());
        int sourceValue, destinationValue;
        sourceValue = parent.getValue(source);
        destinationValue = parent.getValue(destination);
        childState.setValue(source, destinationValue);
        childState.setValue(destination, sourceValue);
        return childState;
    }

    public List<State> generateChildren() {
        int source = this.getIndex(0);
        State childState;
        List<State> children = new ArrayList<>();

        if (source - this.size > -1) { // Can go up
            childState = getChild(this, source,source - this.size );
            children.add(childState);
        }
        if (source + this.size < this.size * this.size) { // Can go down
            childState = getChild(this, source, source + this.size);
            children.add(childState);
        }
        if (source%this.size != 0) { // Can go right
            childState = getChild(this, source, source - 1);
            children.add(childState);
        }
        if ((source+1)%this.size != 0){ // Can go left
            childState = getChild(this, source, source + 1);
            children.add(childState);
        }


        return children;
    }

}
