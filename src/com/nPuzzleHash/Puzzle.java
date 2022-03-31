package com.nPuzzleHash;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

import static java.lang.System.exit;

public class Puzzle {
    private final int size;
    private final State start;
    private final State goal;
    private int solved;
    private final Map<Integer, String> movesDict = new HashMap<>() {{
                                                    put(1, " Left ");
                                                    put(-1, " Right ");
                                                    put(3, " Up ");
                                                    put(-3, " Down ");
    }};

    public static class Result {
        public int[] path;
        public int open;
        public long time;
        public int level;
        public int cost;

        public Result(int[] path, int open, long time, int level, int cost) {
            this.path = path;
            this.open = open;
            this.time = time;
            this.level = level;
            this.cost = cost;
        }
    }

    public Puzzle(int size, int start) {
        this.size = size;
        this.solved = 0;
        this.start = new State(start, this.size);
        this.goal = new State(123804765, this.size);
    }

    public void process() {
        if (solvable())
            System.out.println("The puzzle is solvable\n");
        else{
            System.out.println("The puzzle is not solvable\n");
            exit(0);
        }
    }

    public boolean after(State goal, int i, int j){
        return goal.getIndex(i) > goal.getIndex(j);
    }

    public boolean solvable() {
        int inversion=0, s = this.start.getData(), x, y;
        System.out.println("Initial state");
        this.start.display();
        System.out.println("Goal state");
        this.goal.display();
        for(int i=(int) Math.pow(10, this.size*this.size-1); i>0; i=i/10) {
            x = ((s - s % i) / i) % 10;
            if (x != 0)
                for(int j=i/10; j>0; j=j/10) {
                    y = ((s - s % j) / j) % 10;
                    if (y != 0 && after(this.goal, x, y)) {
                        inversion++;
                    }
                }
        }
        System.out.println("Number of inversions is "+inversion);
        return inversion%2 == 0;
    }

    public boolean isSolution(Node node) {
        return this.goal.getData() == node.getState().getData();
    }

    public String printPath(Node node) {
        int loc, locParent;
        String path = "\n";
        while(node.getParent() != null){
            path = node.getState().getData() + path;
            loc = node.getState().getIndex(0);
            locParent = node.getParent().getState().getIndex(0);
            path = movesDict.get(loc - locParent) + path;
            node = node.getParent();
        }
        path = start.getData() + path;
        path = "The path is " + path;
        return path;
    }

    public int[] getPath(Node node) {
        int loc, locParent, level=node.getLevel(), i=level-1;
        int[] path = new int[level];
        while(node.getParent() != null){
            loc = node.getState().getIndex(0);
            locParent = node.getParent().getState().getIndex(0);
            path[i] = loc - locParent;
            i --;
            node = node.getParent();
        }
        return path;
    }

    public int getBest(List<Node> list) {
        int min = 0;
        for (int i=1; i<list.size(); i++) {
            if(list.get(min).getCost() > list.get(i).getCost())
                min = i;
        }
        return min;
    }

    public static boolean notIn(List<State> list, State state) {
        for (State s : list) {
            if (state.getData() == s.getData())
                return false;
        }
        return true;
    }

    public Result solveBFS() {

        List<State> visited = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        List<State> children;
        Node node = null;
        int count = 0;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        queue.add(initialNode);
        long startTime = System.nanoTime();
        while (!queue.isEmpty()) {
            node = queue.poll();
            visited.add(node.getState());
            count ++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                if (notIn(visited, c)) {
                    queue.add(new Node(c, node, 0, node.getLevel()+1));
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "BFS Not found\n";
        else{
            report = "Solved with BFS\n"+
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration/1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

    public Result solveDFS(int maxDepth) {

        List<State> visited = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        List<State> children;
        Node node=null;
        int count = 0;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        stack.add(initialNode);
        long startTime = System.nanoTime();
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getLevel() <= maxDepth) {
                visited.add(node.getState());
                count++;
                if (isSolution(node)) {
                    this.solved = 1;
                    break;
                }
                children = node.getState().generateChildren();
                for (State c : children) {
                    if (notIn(visited, c)) {
                        stack.add(new Node(c, node, 0, node.getLevel()+1));
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "DFS, max depth = " + maxDepth + "\nNot found\n";
        else {
            report = "Solved with DFS, max depth = " + maxDepth + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration / 1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

    public Result solveA(String H) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Heuristics heuristic = new Heuristics();
        Method h = Heuristics.class.getMethod(H, State.class, State.class);

        List<State> visited = new ArrayList<>();
        List<Node> list = new ArrayList<>();
        List<State> children;
        Node node = null;
        int count = 0, best, hVal;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, (int) h.invoke(heuristic, this.start, this.goal), 0);

        list.add(initialNode);
        long startTime = System.nanoTime();
        while (!list.isEmpty()) {
            best = getBest(list);
            node = list.get(best);

            list.remove(best);
            visited.add(node.getState());

            count++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
               if (notIn(visited, c)) {
                    hVal = (int) h.invoke(heuristic, c, this.goal);
                    list.add(new Node(c, node, node.getLevel()+1+hVal, node.getLevel()+1));
                }
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "A* Not found\n";
        else {
            report = "Solved with A* using " + H + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration / 1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nCost of the solution: " + node.getCost() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

    // Hash
    public Result solveHashBFS() {

        HashTable visited = new HashTable();
        Queue<Node> queue = new LinkedList<>();
        List<State> children;
        Node node = null;
        int count = 0;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        queue.add(initialNode);
        long startTime = System.nanoTime();
        while (!queue.isEmpty()) {
            node = queue.poll();
            visited.add(node.getState().getData());
            count ++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                if (visited.notIn(c.getData())) {
                    queue.add(new Node(c, node, 0, node.getLevel()+1));
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "BFS Not found\n";
        else{
            report = "Solved with BFS\n"+
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration/1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

    public Result solveHashDFS(int maxDepth) {

        HashTable visited = new HashTable();
        Stack<Node> stack = new Stack<>();
        List<State> children;
        Node node=null;
        int count = 0;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        stack.add(initialNode);
        long startTime = System.nanoTime();
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getLevel() <= maxDepth) {
                visited.add(node.getState().getData());
                count++;
                if (isSolution(node)) {
                    this.solved = 1;
                    break;
                }
                children = node.getState().generateChildren();
                for (State c : children) {
                    if (visited.notIn(c.getData())) {
                        stack.add(new Node(c, node, 0, node.getLevel()+1));
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "DFS, max depth = " + maxDepth + "\nNot found\n";
        else {
            report = "Solved with DFS, max depth = " + maxDepth + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration / 1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

    public Result solveHashA(String H) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Heuristics heuristic = new Heuristics();
        Method h = Heuristics.class.getMethod(H, State.class, State.class);

        HashTable visited = new HashTable();
        List<Node> list = new ArrayList<>();
        List<State> children;
        Node node = null;
        int count = 0, best, hVal;
        String report;
        this.solved = 0;

        Node initialNode = new Node(this.start, null, (int) h.invoke(heuristic, this.start, this.goal), 0);

        list.add(initialNode);
        long startTime = System.nanoTime();
        while (!list.isEmpty()) {
            best = getBest(list);
            node = list.get(best);

            list.remove(best);
            visited.add(node.getState().getData());

            count++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                if (visited.notIn(c.getData())) {
                    hVal = (int) h.invoke(heuristic, c, this.goal);
                    list.add(new Node(c, node, node.getLevel()+1+hVal, node.getLevel()+1));
                }
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0)
            report = "A* Not found\n";
        else {
            report = "Solved with A* using " + H + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + duration / 1000000 + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nCost of the solution: " + node.getCost() + "\n";
        }
        System.out.println(report);
        return new Result(getPath(node), count, duration / 1000000, node.getLevel(), node.getCost());
    }

}
