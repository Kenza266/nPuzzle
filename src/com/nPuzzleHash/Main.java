package com.nPuzzleHash;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int Hard = 471203586;
        int Easy = 103824765;
        int Medium = 813724605;
        int Test = 182043756;
        int Test1 = 283164705;
        int Test2 = 540618732;
        int Easy1 = 134862705;
        int Medium1 = 281043765;
        int Hard1 = 567408321;
        int Easy2 = 134862705;
        int Medium2 = 281043765;
        int Hard2 = 567408321;
        int x = 281043765;
        Puzzle puzzle = new Puzzle(3, Easy1);
        puzzle.process();
        Puzzle.Result result;

        result = puzzle.solveBFS();
        System.out.println(result.report);

        result = puzzle.solveDFS(3);
        System.out.println(result.report);

        result = puzzle.solveA("hMisplaced");
        System.out.println(result.report);

        result = puzzle.solveA("hManhattan");
        System.out.println(result.report);

        System.out.println("With hashing\n");

        result = puzzle.solveHashBFS();
        System.out.println(result.report);

        result = puzzle.solveHashDFS(3);
        System.out.println(result.report);

        result = puzzle.solveHashA("hMisplaced");
        System.out.println(result.report);

        result = puzzle.solveHashA("hManhattan");
        System.out.println(result.report);

    }
}
