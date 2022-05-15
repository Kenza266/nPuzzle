package com.nuzzle.frontend;

import com.nuzzle.backend.Puzzle;
import com.nuzzle.backend.Result;
import com.nuzzle.backend.geneticSolver;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.jar.JarInputStream;

public class Gui extends JFrame{//inheriting JFrame

    private Puzzle puzzle;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    String algs[] = { "BFS", "DFS", "Manhattan", "Euclid", "Hamming", "GA", "PSO" };

    JLabel labelEnterState;
    JTextField inputState;
    JLabel labelSelect;
    JComboBox selectAlgo;
    JButton runButton;
    JPanel panel;
    JLabel nodes;
    JLabel depth;
    JLabel maxNode;
    JLabel runtime;
    public Gui(){

        labelEnterState = new JLabel("Enter Input Sequence");
        labelEnterState.setBounds(115, 0, 150, 40);
        add(labelEnterState);

        inputState = new JFormattedTextField("281043765");
        inputState.setBounds(115,40, 150, 40);
        add(inputState);


        labelSelect = new JLabel("Select an algorithm:");
        labelSelect.setBounds(115, 80, 150, 40);
        add(labelSelect);


        selectAlgo = new JComboBox(algs);
        selectAlgo.setBounds(105,120,150,40);
        add(selectAlgo);

        runButton = new JButton("Run");
        runButton.setBounds(130,160,100, 40);

        runButton.addActionListener(e -> {
            if(validStateString(inputState.getText())){
                puzzle = new Puzzle(3, Integer.parseInt(inputState.getText()),123804765);
                int inversions = puzzle.process();
                if(inversions%2 != 0)
                    inputState.setText("The state is not solvable");
                else {
                    Result result;

                    try {
                        result = runAlgorithm(selectAlgo.getSelectedItem().toString(), inversions);
                        printReport(result);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else
                inputState.setText("Enter a valid state");
        });
        add(runButton);



        panel = new JPanel();
        panel.setBounds(50, 200, 300, 150);
        panel.setBorder(BorderFactory.createTitledBorder("Report"));
        panel.setLayout(null);
        add(panel);


        nodes = new JLabel("Open nodes: ");
        nodes.setBounds(30,25, 300, 25);
        panel.add(nodes);
        depth = new JLabel("Path size: ");
        depth.setBounds(30,50, 300, 25);
        panel.add(depth);
        maxNode = new JLabel("Max nodes: ");
        maxNode.setBounds(30,75, 300, 25);
        panel.add(maxNode);
        runtime = new JLabel("Runtime: ");
        runtime.setBounds(30,100, 300, 25);
        panel.add(runtime);





        setSize(400,500);
        setLayout(null);
        setVisible(true);

    }

    private Result runAlgorithm(String algorithm, int inversions) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Result result = null;
        switch (algorithm){
            case "BFS":
                result = puzzle.solveHashBFS();
                break;
            case "DFS":
                result = puzzle.solveHashDFS(inversions*3 + 1);
                break;
            case "Manhattan":
                result = puzzle.solveHashA("hManhattan");
                break;
            case "Euclid":
                result = puzzle.solveHashA("hEuclid");
                break;
            case "Hamming":
                result = puzzle.solveHashA("hHamming");
                break;
            case "GA":
                geneticSolver solver = new geneticSolver(puzzle, 5000, 0.7, 0.3, 0.5);
                result = puzzle.solveGenetic(solver, 200);
                break;
            case "PSO":
                result = puzzle.solvePSO();
                break;
        }
        if(result != null) {
            Collections.reverse(result.path);
            result.path.forEach(e -> {
                if (e.getState().length() == 8) e.setState("0" + e.getState());
            });
            return result;
        }
        return null;
    }

    private Boolean validStateString(String s){
        return s.matches("^(?!.*(.).*\\1)[0-8]{9}$");
    }


    private void printReport(Result result) {
        nodes.setText("Open nodes: "+ result.open);
        depth.setText("Path size: "+ result.level);
        maxNode.setText("Max nodes: "+ result.maxNodes);
        runtime.setText("Runtime: "+ (
                (result.time>100000) ?
                        (result.time/1000000>1000) ? df.format((float) result.time/1000000000) + "s" :
                                df.format((float) result.time/1000000) + "ms" :
                        df.format((float) result.time) + "ns"));

    }
}