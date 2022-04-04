package com.nPuzzleHash;
import java.lang.reflect.InvocationTargetException;

public class Main {

    private static void run(int state) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Puzzle puzzle = new Puzzle(3, state);
        System.out.println(puzzle.process());
        Result result;

        result = puzzle.solveA("hMisplaced");
        System.out.println(result.report);

        result = puzzle.solveA("hManhattan");
        System.out.println(result.report);

        System.out.println("Hash\n");

        result = puzzle.solveHashA("hMisplaced");
        System.out.println(result.report);

        result = puzzle.solveHashA("hManhattan");
        System.out.println(result.report);
    }

    private static void run1(int state)  {
        Puzzle puzzle = new Puzzle(3, state);
        int s = puzzle.process();
        System.out.println(s);
        Result result;

        result = puzzle.solveBFS();
        System.out.println(result.report);

        result = puzzle.solveDFS(4*s+1);
        System.out.println(result.report);

        System.out.println("Hash\n");

        result = puzzle.solveHashBFS();
        System.out.println(result.report);

        result = puzzle.solveHashDFS(4*s+1);
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

        /*System.out.println("Easy");
        run(Easy);
        System.out.println("Hard");
        run(Hard);
        System.out.println("Hard1");
        run(Hard1);
        System.out.println("Hard2");
        run(Hard2);
        System.out.println("Test");
        run(Test);
        System.out.println("Test1");
        run(Test1);
        System.out.println("Medium");
        run(Medium);
        System.out.println("x");
        run(x);*/

        Puzzle puzzle = new Puzzle(3, Hard);
        int s = puzzle.process();
        System.out.println(s);
        Result result;

        result = puzzle.solveHashA("hEuclid");
        System.out.println(result.report);

        result = puzzle.solveHashA("hHamming");
        System.out.println(result.report);

        result = puzzle.solveHashA("hManhattan");
        System.out.println(result.report);

        result = puzzle.solveHashBFS();
        System.out.println(result.report);

        /*result = puzzle.solveDFS(4*s+1);
        System.out.println(result.report);*/

        /*result = puzzle.solveA("hMisplaced");
        System.out.println(result.report);

        result = puzzle.solveA("hManhattan");
        System.out.println(result.report);

        result = puzzle.solveA("hHamming");
        System.out.println(result.report);

        result = puzzle.solveA("hEuclid");
        System.out.println(result.report);

        System.out.println("With hashing\n");*/

        /*result = puzzle.solveHashBFS();
        System.out.println(result.report);

        result = puzzle.solveHashDFS(4*s+1);
        System.out.println(result.report);*/

        /*result = puzzle.solveHashA("hHamming");
        System.out.println(result.report);

        result = puzzle.solveHashA("hEuclid");
        System.out.println(result.report);*/

    }
}
