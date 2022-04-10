package com.backend;
import java.lang.reflect.InvocationTargetException;

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
        Puzzle puzzle = new Puzzle(3, 281043765);
        int s = puzzle.process();
        //System.out.println(state);
        System.out.println(s);
        //Result result;

        /*result = puzzle.solveHashDFS(3*s+1);
        if (result != null)
            System.out.println(result.report);*/

    }
}
