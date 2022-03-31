package com.example.demo2;


import com.nPuzzleHash.*;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class MainWindowController {
    @FXML private Pane titlePane;

    @FXML private TextField input;
    @FXML private TextField target;

    @FXML private Button run;
    @FXML private Button play;

    @FXML private Label nodes;
    @FXML private Label depth;
    @FXML private Label cost;
    @FXML private Label runtime;

    @FXML private ChoiceBox<String> chbox;
    @FXML private Pane btn1;
    @FXML private Pane btn2;
    @FXML private Pane btn3;
    @FXML private Pane btn4;
    @FXML private Pane btn5;
    @FXML private Pane btn6;
    @FXML private Pane btn7;
    @FXML private Pane btn8;
    @FXML private Pane btn9;


    private Puzzle puzzle;


    private static List<String > algorithms = new ArrayList<>(Arrays.asList("BFS", "DFS", "manhattan", "euclidean", "hamming"));
    private static List<Pane> buttons = new ArrayList<>();//Arrays.asList(btn1, btn2, btn3, btn4, btn0, btn5, btn6, btn7, btn8);
    private static List<String> inputList;
    private static Map<Pane, ButtonLayout> buttonsPositions = new HashMap<>();
    private static int zeroIndex = 0;



    public void init(Stage stage) {
        buttons = Arrays.asList(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9);
        buttons.forEach(e -> buttonsPositions.put(e, new ButtonLayout(e.getLayoutX(), e.getLayoutY())));
        input.setText("281043765");
        target.setText("123804765");
        System.out.println(buttons);
        initCheckBox();
        initRunButton();
        play.setDisable(true);

    }


    private void initCheckBox() {
        chbox.setValue("BFS");
        chbox.getItems().addAll(algorithms);
    }


    private void initRunButton() {
        run.setOnAction(e -> {
            if(valditeStateString(input.getText()) && valditeStateString(target.getText())){
                nodes.setText("running...");
                depth.setText("running...");
                cost.setText("running...");
                runtime.setText("running...");
                ArrayList<Transformation> path = runAlgorithm(chbox.getValue(), input.getText(), target.getText());

                initGrid(input.getText());
                play.setDisable(false);
                play.setOnAction(event->playAnimation(path));
                btn1.setLayoutX(0);
                btn1.setLayoutY(0);
            } else {
                input.setText("enter a valid state");
                target.setText("enter a valid satate");
            }

        });

    }

    private ArrayList<Transformation> runAlgorithm(String algorithm, String input, String target) {
        puzzle = new Puzzle(3, Integer.parseInt(input));
        int inversions = puzzle.process();
        Puzzle.Result result = null;

        switch (algorithm){
            case "BFS":
                result = puzzle.solveHashBFS();
                break;
            case "DFS":
                result = puzzle.solveHashDFS(inversions*3);
                break;
            case "misplaced":
                //result = puzzle.solveHashA("hMisplaced");
                break;
            case "manhattan":
                //result = puzzle.solveHashA("hManhattan");
                break;
            case "eclidean":
                result = null;
                break;
            case "hamming":
                //result = puzzle.solveHashA("hHamming");
                break;
        }



        updateReport(result);

        Collections.reverse(result.path);
        result.path.forEach(e ->{if (e.getState().length() == 8)e.setState("0" + e.getState());});
        return result.path;
    }

    private void updateReport(Puzzle.Result result) {
        nodes.setText("Open nodes: "+ String.valueOf(result.open));
        depth.setText("Level: "+ String.valueOf(result.level));
        cost.setText("Cost: "+ String.valueOf(result.cost));
        runtime.setText("Runtime: "+ String.valueOf(result.time) + "ms");
    }

    private void initGrid(String text) {
        inputList = Arrays.asList(text.split(""));
        for (int i = 0; i < 9; i++) {
            if (inputList.get(i).equals("0")) {
                ((Label) buttons.get(i).getChildren().get(0)).setText(inputList.get(i));
                buttons.get(i).setOpacity(0);
            } else {
                ((Label) buttons.get(i).getChildren().get(0)).setText(inputList.get(i));
                buttons.get(i).setOpacity(1);
            }
        }
    }


    private Boolean valditeStateString(String s){
        return s.matches("^(?!.*(.).*\\1)[0-8]{9}$");
    }



    private void playAnimation(ArrayList<Transformation> path) {
        SequentialTransition sec = new SequentialTransition();
        inputList = Arrays.asList(target.getText().split(""));
        zeroIndex = inputList.indexOf("0");
        path.forEach(e -> sec.getChildren().add(move(e.getTransformation(), e.getState())));
        sec.play();
        play.setDisable(true);

    }

    private TranslateTransition move(String i, String state) {
        TranslateTransition transition = new TranslateTransition();
        zeroIndex = inputList.indexOf("0");

        switch (i){
            case "-3":
                transition = tileDown(zeroIndex-3);
                break;
            case "3":
                System.out.println(zeroIndex);
                transition = tileUp(zeroIndex+3);
                break;
            case "-1":
                transition = tileRight(zeroIndex-1);
                break;
            case "1":
                transition = tileLeft(zeroIndex+1);
                break;

        }
        inputList = Arrays.asList(state.split(""));
        return transition;

    }


    private TranslateTransition tileDown(int i){
        //the one above 0 goes down
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(inputList.get(i)));
        transition.setDuration(Duration.millis(500));
        transition.setByY(82);
        return transition;
    }

    private TranslateTransition tileUp(int i){
        //the one below 0 goes up

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(inputList.get(i)));
        transition.setDuration(Duration.millis(500));
        transition.setByY(-82);
        return transition;
    }
    private TranslateTransition tileRight(int i){
        //the one left 0 goes right
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(inputList.get(i)));
        transition.setDuration(Duration.millis(500));
        transition.setByX(82);
        return transition;

    }
    private TranslateTransition tileLeft(int i){
        //the one right 0 goes left
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(inputList.get(i)));
        transition.setDuration(Duration.millis(500));
        transition.setByX(-82);
        return transition;
    }

    private Pane getButton(String s){
        for (int i = 0; i < 9; i++) {
            if (((Label)buttons.get(i).getChildren().get(0)).getText().equals(s)){
                return buttons.get(i);
            }
        }
        return null;

    }

}