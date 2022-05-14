package com.backend;


import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class representing a particle.
 */
public class Particle {

    private static final Map<Integer, String> movesDict = new HashMap<>() {{
        put(1, "00");
        put(-1, "01");
        put(3, "10");
        put(-3, "11");
    }};
    private static final Map<String, Integer> movesDictReversed = new HashMap<>() {{
        put("00", 1);
        put("01", -1);
        put("10", 3);
        put("11", -3);
    }};


    State currentState;
    List<Integer> position; //The position vector of this particle
    int fitness; //The fitness of this particle
    int velocity; //The velocity vector of this particle
    List<Integer> personalBest; //Personal best of the particle

    public Particle(List<Integer> position, int velocity) {
        this.position = position;
        this.velocity = velocity;
    }


    public void improve() {
        int i, change = 1;
        List<Integer> path = this.position;
        //System.out.println("In improve");
        //this.position.forEach(e-> System.out.println(e.toString()));
        while(change == 1) {
            change = 0;
            i = 0;
            while (i < path.size() - 1) {
                if (path.get(i) == -path.get(i + 1)) {
                    change = 1;
                    path.remove(i);
                    path.remove(i);
                }
                else
                    i += 1;
            }
        }
        //this.position.forEach(e-> System.out.println(e.toString()));
        this.position = path;
    }


    public static Long encode(List<Integer> path){
        StringBuilder biPath = new StringBuilder();
        path.forEach(p -> biPath.append(movesDict.get(p)));
        if(biPath.toString().length() != 0)
            return Long.parseLong(biPath.toString(), 2);
        else
            return 0L;
    }

    public static List<Integer> decode(long intPath) {
        List<Integer> path = new ArrayList<>();
        String biPath = Long.toBinaryString(intPath);
        if(biPath.length() % 2 == 1)
            biPath = "0" + biPath;
        List<String> pathString = new ArrayList<>();
        pathString = Arrays.asList(biPath.split("(?<=\\G.{2})"));
        for (String str:pathString) {
            path.add(movesDictReversed.get(str));
        }
        return path;
    }
}
