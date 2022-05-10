package com.backend;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    private static void run1(int state) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Puzzle puzzle = new Puzzle(3, state);
        System.out.println(state);
        System.out.println(puzzle.process());
        Result result;

        result = puzzle.solveA("hEuclid");
        System.out.println(result.report);

        result = puzzle.solveA("hManhattan");
        System.out.println(result.report);

        result = puzzle.solveA("hHamming");
        System.out.println(result.report);

        System.out.println("Hash\n");

        result = puzzle.solveHashA("hEuclid");
        System.out.println(result.report);

        result = puzzle.solveHashA("hManhattan");
        System.out.println(result.report);

        result = puzzle.solveHashA("hHamming");
        System.out.println(result.report);
    }

    private static void run(int state)  {
        Puzzle puzzle = new Puzzle(3, state);
        int s = puzzle.process();
        System.out.println(s);
        Result result;

        result = puzzle.solveBFS();
        System.out.println(result.report);

        result = puzzle.solveDFS(2*s+1);
        if(result != null)
            System.out.println(result.report);

        System.out.println("Hash\n");

        result = puzzle.solveHashBFS();
        System.out.println(result.report);

        result = puzzle.solveHashDFS(2*s+1);
        if(result != null)
            System.out.println(result.report);
    }

    private static void runAll(int state) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Puzzle puzzle = new Puzzle(3, state);
        int s = puzzle.process();
        System.out.println(state);
        System.out.println(s);
        Result result;

        result = puzzle.solveHashBFS();
        System.out.println(result.report);

        result = puzzle.solveHashDFS(3*s+1);
        if (result != null)
            System.out.println(result.report);

        result = puzzle.solveHashA("hManhattan");
        System.out.println(result.report);

        result = puzzle.solveHashA("hHamming");
        System.out.println(result.report);

        result = puzzle.solveHashA("hEuclid");
        System.out.println(result.report);
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int Hard = 471203586;
        int Easy = 103824765;
        int Medium = 813724605;
        int Test = 182043756;
        int Test1 = 283164705;
        int Easy1 = 134862705;
        int Medium1 = 281043765;
        int Hard1 = 567408321;
        int Easy2 = 134862705;
        int Medium2 = 281043765;
        int Hard2 = 567408321;
        int s1 = 153720468;
        int s2 = 315720846;
        int s3 = 684250713;
        int s4 = 461250873;
        int state = 283164705;
        int newS = 125647830;
        int herState = 813724065;
        
        Puzzle puzzle = new Puzzle(3, herState);

        int s = puzzle.process();
        if(s%2 == 1)
            return;
        puzzle.getStart().display();

        geneticSolver solver = new geneticSolver(puzzle, 5000, 0.7, 0.5, 0.4);
        String report = puzzle.solveGenetic(solver, 1000);
        System.out.println(report);

        /*MUTATION_CHANCE = 0.9
        CROSS_OVER_RATE = 0.3
        POPULATION_LEN = 1000
        MAX_ITERATION = 1000*/

        /*for(Chromosome c : solver.population){
            System.out.println("Gene");
            for(int g : c.gene){
                System.out.print(g+"\t");
            }
            System.out.println();
        }
        Chromosome[] children = Chromosome.singleCrossOver(solver.population.get(0), solver.population.get(1));
        System.out.println("Child1");
        for(int g : children[0].gene){
            System.out.print(g+"\t");
        }
        System.out.println();

        System.out.println("Child2");
        for(int g : children[1].gene){
            System.out.print(g+"\t");
        }
        System.out.println();

        Chromosome chromosome = solver.population.get(0);
        System.out.println("Chromosome");
        for(int g : chromosome.gene){
            System.out.print(g+"\t");
        }
        System.out.println();
        chromosome.mutate();
        System.out.println("After mutation");
        for(int g : chromosome.gene){
            System.out.print(g+"\t");
        }
        System.out.println();
        State newState = chromosome.validateGene(puzzle.getStart());
        System.out.println();

        if(newState == null)
            System.out.println("Non valid");
        else
            newState.display();*/


    }
}
