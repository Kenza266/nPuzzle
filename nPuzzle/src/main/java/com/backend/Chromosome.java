package com.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chromosome {

    private double error;
    private List<Integer> gene; //path
    private State state;
    List<Integer> possibilities = new ArrayList<Integer>(Arrays. asList(1, -1, 3, -3));

    public Chromosome(State state, List<Integer> gene) {
        this.state = state;
        this.gene = gene;
        this.error = 100000;
    }

    public Chromosome(State state) {
        this(state, new ArrayList<>());
    }

    public Chromosome(ArrayList<Integer> gene) {
        this.gene = gene;
        this.error = 100000;
    }

    public double getError() {
        return error;
    }

    public List<Integer> getGene() {
        return gene;
    }

    public State getState() {
        return state;
    }

    public double computeCost(State goal) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Heuristics heuristic = new Heuristics();
        Method h1 = Heuristics.class.getMethod("hManhattan", State.class, State.class);
        Method h2 = Heuristics.class.getMethod("hHamming", State.class, State.class);
        Method h3 = Heuristics.class.getMethod("hEuclid", State.class, State.class);
        double lenPenalty = this.gene.size() * 0.01;
        this.error = (float) ((int) h1.invoke(heuristic, this.state, goal))/10 + lenPenalty;
        //getInversions(this.state, goal)/10
        return this.error;
    }

    public void correctGene(State start) {
        Chromosome newChromosome = start.apply(this.gene);
        this.state = newChromosome.getState();
        this.gene = newChromosome.getGene();
        //return this.state;
    }

    public static Chromosome[] singlePointCrossOver(Chromosome a, Chromosome b) {
        int point, lenA = a.gene.size(), lenB = b.gene.size();
        if (lenA < lenB)
            point = (int) (Math.random() * lenA);
        else
            point = (int) (Math.random() * lenB);

        List<Integer> child1 = Stream.of(a.gene.subList(0, point), b.gene.subList(point, lenB))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Integer> child2 = Stream.of(b.gene.subList(0, point), a.gene.subList(point, lenA))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new Chromosome[] {new Chromosome((ArrayList<Integer>) child1),
                new Chromosome((ArrayList<Integer>) child2)};
    }

    public static Chromosome[] crossOver(Chromosome a, Chromosome b) {
        if (a.gene.size() < b.gene.size())
            return crossOver(b, a);

        int lenA = a.gene.size(), lenB = b.gene.size();

        List<Integer> child1 = new ArrayList<>(), child2 = new ArrayList<>();

        for(int i=0; i<lenB; i++) {
            if(Math.random() < 0.5){
                child1.add(a.gene.get(i));
                child2.add(b.gene.get(i));
            }
            else{
                child1.add(b.gene.get(i));
                child2.add(a.gene.get(i));
            }
            if(lenB != lenA){
                for(int j=0; j<lenA-lenB; j++)
                    child1.add(a.gene.get(lenB+j));
            }
        }

        return new Chromosome[] {new Chromosome((ArrayList<Integer>) child1),
                new Chromosome((ArrayList<Integer>) child2)};
    }

    public void mutate(double chance) {
        double addRate = 0.5 + chance;
        int choice;
        if(this.gene.size() == 0)
            addRate = 1;
        if (Math.random() < addRate) {
            choice = (int)(Math.random() * possibilities.size());
            this.gene.add(possibilities.get(choice));
        }
        else{
            int n = (int) (Math.random() * gene.size());
            int[] index = new int[n];
            for(int i = 0; i<n; i++)
                index[i] = (int) (Math.random() * gene.size());
            for(int j :index) {
                choice = (int)(Math.random() * possibilities.size());
                this.gene.set(j, possibilities.get(choice));
            }
        }
    }

    public void improve() {
        int i, change = 1;
        while(change == 1) {
            change = 0;
            i = 0;
            while (i < this.gene.size() - 1) {
                if (this.gene.get(i) == -this.gene.get(i + 1)) {
                    change = 1;
                    this.gene.remove(i);
                    this.gene.remove(i);
                }
                else
                    i += 1;
            }
        }
    }
}
