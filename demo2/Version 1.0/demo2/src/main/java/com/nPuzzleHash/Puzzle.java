package com.nPuzzleHash;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class Puzzle {
    private final int size;
    private final State start;
    private final State goal;
    private Node childNode;
    private int solved;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final Map<Integer, String> movesDict = new HashMap<>() {{
        put(1, " Left ");
        put(-1, " Right ");
        put(3, " Up ");
        put(-3, " Down ");
    }};
    private int maxMem;

    public Puzzle(int size, int start) {
        this(size, start, 123804765);
    }

    public Puzzle(int size, int start, int goal) {
        this.size = size;
        this.solved = 0;
        this.start = new State(start, this.size);
        this.goal = new State(goal, this.size);
    }

    public int process() {
        int s = getInversions();
        return s;
    }

    public boolean after(State goal, int i, int j){
        return goal.getIndex(i) > goal.getIndex(j);
    }

    public int getInversions() {
        int inversion=0, s = this.start.getData(), x, y;
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
        return inversion;
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

    public ArrayList<Transformation> getPath(Node node) {
        int loc, locParent, level=node.getLevel();
        ArrayList<Transformation> path = new ArrayList<>();
        while(node.getParent() != null){
            loc = node.getState().getIndex(0);
            locParent = node.getParent().getState().getIndex(0);
            path.add(new Transformation(String.valueOf(node.getState().getData()), String.valueOf(loc - locParent)));
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
        maxMem = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        queue.add(initialNode);
        long startTime = System.nanoTime();
        while (!queue.isEmpty() && this.solved == 0) {
            if (queue.size()+visited.size() > maxMem)
                maxMem = queue.size()+visited.size();
            node = queue.poll();
            visited.add(node.getState());
            count ++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                childNode = new Node(c, node, 0, node.getLevel()+1);
                if (isSolution(childNode)) {
                    node = childNode;
                    this.solved = 1;
                    break;
                }
                if (notIn(visited, c)) {
                    queue.add(childNode);
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("BFS Not found\n");
            return null;
        }
        else {
            report = "Solved with BFS\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
    }

    public Result solveDFS(int maxDepth) {

        List<State> visited = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        List<State> children;
        Node node=null;
        int count = 0;
        String report;
        this.solved = 0;
        maxMem = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        stack.add(initialNode);
        long startTime = System.nanoTime();
        while (!stack.isEmpty() && this.solved == 0) {
            if (stack.size()+visited.size() > maxMem)
                maxMem = stack.size()+visited.size();
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
                    childNode = new Node(c, node, 0, node.getLevel()+1);
                    if (isSolution(childNode)) {
                        node = childNode;
                        this.solved = 1;
                        break;
                    }
                    if (notIn(visited, c)) {
                        stack.add(childNode);
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("DFS Not found\n");
            return null;
        }
        else {
            report = "Solved with DFS, max depth = " + maxDepth + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
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
        maxMem = 0;

        Node initialNode = new Node(this.start, null, (int) h.invoke(heuristic, this.start, this.goal), 0);

        list.add(initialNode);
        long startTime = System.nanoTime();
        while (!list.isEmpty() && this.solved == 0) {
            if (list.size()+visited.size() > maxMem)
                maxMem = list.size()+visited.size();
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
                hVal = (int) h.invoke(heuristic, c, this.goal);
                childNode = new Node(c, node, node.getLevel()+1+hVal, node.getLevel()+1);
                if (isSolution(childNode)) {
                    node = childNode;
                    this.solved = 1;
                    break;
                }
                if (notIn(visited, c)) {
                    list.add(childNode);
                }
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("A* Not found\n");
            return null;
        }
        else {
            report = "Solved with A* using " + H + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
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
        maxMem = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        queue.add(initialNode);
        long startTime = System.nanoTime();
        while (!queue.isEmpty() && this.solved == 0) {
            if (queue.size()+visited.getSize() > maxMem)
                maxMem = queue.size()+visited.getSize();
            node = queue.poll();
            visited.add(node.getState().getData(), node.getCost());
            count ++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                childNode = new Node(c, node, 0, node.getLevel()+1);
                if (isSolution(childNode)) {
                    node = childNode;
                    this.solved = 1;
                    break;
                }
                if (visited.notIn(c.getData())) {
                    queue.add(childNode);
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("BFS Not found\n");
            return null;
        }
        else {
            report = "Solved with BFS\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
    }

    public Result solveHashDFS(int maxDepth) {

        HashTable visited = new HashTable();
        Stack<Node> stack = new Stack<>();
        List<State> children;
        Node node=null;
        int count = 0;
        String report;
        this.solved = 0;
        maxMem = 0;

        Node initialNode = new Node(this.start, null, 0, 0);

        stack.add(initialNode);
        long startTime = System.nanoTime();
        while (!stack.isEmpty() && this.solved == 0) {
            if (stack.size()+visited.getSize() > maxMem)
                maxMem = stack.size()+visited.getSize();
            node = stack.pop();
            if (node.getLevel() <= maxDepth) {
                visited.add(node.getState().getData(), node.getCost());
                count++;
                if (isSolution(node)) {
                    this.solved = 1;
                    break;
                }
                children = node.getState().generateChildren();
                for (State c : children) {
                    childNode = new Node(c, node, 0, node.getLevel()+1);
                    if (isSolution(childNode)) {
                        node = childNode;
                        this.solved = 1;
                        break;
                    }
                    if (visited.notIn(c.getData())) {
                        stack.add(childNode);
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("DFS Not found\n");
            return null;
        }
        else {
            report = "Solved with DFS, max depth = " + maxDepth + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
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
        maxMem = 0;

        Node initialNode = new Node(this.start, null, (int) h.invoke(heuristic, this.start, this.goal), 0);

        list.add(initialNode);
        long startTime = System.nanoTime();
        while (!list.isEmpty() && this.solved == 0) {
            if (list.size()+visited.getSize() > maxMem)
                maxMem = list.size()+visited.getSize();
            best = getBest(list);
            node = list.get(best);

            list.remove(best);
            visited.add(node.getState().getData(), node.getCost());

            count++;
            if (isSolution(node)) {
                this.solved = 1;
                break;
            }
            children = node.getState().generateChildren();
            for (State c : children) {
                hVal = (int) h.invoke(heuristic, c, this.goal);
                childNode = new Node(c, node, node.getLevel()+1+hVal, node.getLevel()+1);
                if (isSolution(childNode)) {
                    node = childNode;
                    this.solved = 1;
                    break;
                }
                if (visited.notIn(c.getData(), childNode.getCost())) {
                    list.add(childNode);
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime); //divide by 1000000 to get milliseconds.
        if(this.solved == 0){
            System.out.println("A* Not found\n");
            return null;
        }
        else {
            report = "Solved with A* using " + H + "\n" +
                    "Report:\nNumber of opened nodes: " + count +
                    "\nExecution time: " + df.format((float) duration/1000000) + "ms" +
                    "\nSolution found at level: " + node.getLevel() +
                    "\nMax nodes in memory: " + maxMem + "\n";
            return new Result(getPath(node), count, duration, node.getLevel(), maxMem, report);
        }
    }

}