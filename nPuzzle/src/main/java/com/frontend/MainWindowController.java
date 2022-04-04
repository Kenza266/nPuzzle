package com.frontend;

import com.backend.*;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Collections;

import java.util.*;

public class MainWindowController {

    @FXML private TextField input;
    @FXML private TextField target;

    @FXML private Button run;
    @FXML private Button play;
    @FXML private Button closeButton;

    @FXML private Label nodes;
    @FXML private Label depth;
    @FXML private Label maxNode;
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
    private final int duration = 500;
    private static final List<String > algorithms = new ArrayList<>(Arrays.asList("BFS", "DFS", "Manhattan", "Euclid", "Hamming"));
    private List<Pane> buttons = new ArrayList<>();
    private static List<String> textList;
    private static int zeroIndex = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void init(Stage stage) {
        buttons = Arrays.asList(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9);
        input.setText("281043765");
        target.setText("123804765");
        initGrid(target.getText());
        initCheckBox();
        initRunButton();
        closeButton.setOnAction(e -> stage.close());
        play.setDisable(true);
        run.setDisable(false);
    }

    private void initCheckBox() {
        chbox.setValue("BFS");
        chbox.getItems().addAll(algorithms);
    }

    private void initRunButton() {
        run.setOnAction(e -> {
            run.setDisable(true);
            if(validStateString(input.getText()) && validStateString(target.getText())){
                puzzle = new Puzzle(3, Integer.parseInt(input.getText()), Integer.parseInt(target.getText()));
                int inversions = puzzle.process();
                if(inversions%2 != 0)
                    input.setText("The state is not solvable");
                else {
                    textList = Arrays.asList(input.getText().split(""));
                    initGrid(input.getText());
                    Result result;
                    try {
                        result = runAlgorithm(chbox.getValue(), inversions);
                        if (result == null){ // Solution not found
                            play.setDisable(true);
                            input.setText("Solution not found");
                            initRunButton();
                        }
                        else {
                            updateReport(result);
                            play.setDisable(false);
                            play.setOnAction(event -> playAnimation(result.path));
                        }
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else if (!validStateString(input.getText()))
                    input.setText("Enter a valid state");
                else
                    target.setText("Enter a valid state");
            run.setDisable(false);
        });
    }

    private Result runAlgorithm(String algorithm, int inversions) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Result result = null;
        switch (algorithm){
            case "BFS":
                result = puzzle.solveHashBFS();
                break;
            case "DFS":
                result = puzzle.solveHashDFS(2);//inversions*3);
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

    private void updateReport(Result result) {
        nodes.setText("Open nodes: "+ String.valueOf(result.open));
        depth.setText("Path size: "+ String.valueOf(result.level));
        maxNode.setText("Max nodes: "+ String.valueOf(result.maxNodes));
        runtime.setText("Runtime: "+ (
                (result.time>100000) ?
                        (result.time/1000000>1000) ? String.valueOf(df.format((float) result.time/1000000000)) + "s" :
                                String.valueOf(df.format((float) result.time/1000000)) + "ms" :
                        String.valueOf(df.format((float) result.time)) + "ns")
        );
    }

    private void initGrid(String text) {
        textList = Arrays.asList(text.split(""));
        for (int i = 0; i < 9; i++) {
            if (textList.get(i).equals("0")) {
                ((Label) buttons.get(i).getChildren().get(0)).setText(textList.get(i));
                buttons.get(i).setOpacity(0);
            } else {
                ((Label) buttons.get(i).getChildren().get(0)).setText(textList.get(i));
                buttons.get(i).setOpacity(1);
            }
        }
    }

    private Boolean validStateString(String s){
        return s.matches("^(?!.*(.).*\\1)[0-8]{9}$");
    }

    private void playAnimation(ArrayList<Transformation> path) {
        play.setDisable(true);
        run.setDisable(true);
        SequentialTransition sec = new SequentialTransition();
        textList = Arrays.asList(input.getText().split(""));
        path.forEach(e -> sec.getChildren().add(move(e.getTransformation(), e.getState())));
        int x = Arrays.asList(input.getText().split("")).indexOf("0")-textList.indexOf("0");
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton("0"));
        transition.setDuration(Duration.millis(0.001));
        switch (x){
            case -3:
                transition.setByY(82);
                break;
            case 3:
                transition.setByY(-82);
                break;
            case -1:
                transition.setByX(83);
                break;
            case 1:
                transition.setByX(-83);
                break;
        }
        sec.getChildren().add(transition);
        sec.play();
        if(path.size() == 0)
            run.setDisable(false);
        sec.setOnFinished(actionEvent -> {
            sec.stop();
            run.setDisable(false);
            initRunButton();
        });
    }

    private TranslateTransition move(String i, String state) {
        TranslateTransition transition = new TranslateTransition();
        zeroIndex = textList.indexOf("0");
        switch (i){
            case "-3":
                transition = tileDown(zeroIndex-3);
                Collections.swap(buttons, buttons.indexOf(getButton("0")), buttons.indexOf(getButton(textList.get(zeroIndex-3))));
                break;
            case "3":
                transition = tileUp(zeroIndex+3);
                Collections.swap(buttons, buttons.indexOf(getButton("0")), buttons.indexOf(getButton(textList.get(zeroIndex+3))));
                break;
            case "-1":
                transition = tileRight(zeroIndex-1);
                Collections.swap(buttons, buttons.indexOf(getButton("0")), buttons.indexOf(getButton(textList.get(zeroIndex-1))));
                break;
            case "1":
                transition = tileLeft(zeroIndex+1);
                Collections.swap(buttons, buttons.indexOf(getButton("0")), buttons.indexOf(getButton(textList.get(zeroIndex+1))));
                break;
        }
        textList = Arrays.asList(state.split(""));
        return transition;
    }

    private TranslateTransition tileDown(int i){
        //the one above 0 goes down
        Pane b = getButton(textList.get(i));
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(b);
        transition.setDuration(Duration.millis(duration));
        transition.setByY(82);
        return transition;
    }

    private TranslateTransition tileUp(int i){
        //the one below 0 goes up
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(textList.get(i)));
        transition.setDuration(Duration.millis(duration));
        transition.setByY(-82);
        return transition;
    }

    private TranslateTransition tileRight(int i){
        //the one left 0 goes right
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(textList.get(i)));
        transition.setDuration(Duration.millis(duration));
        transition.setByX(83);
        return transition;

    }

    private TranslateTransition tileLeft(int i){
        //the one right 0 goes left
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(getButton(textList.get(i)));
        transition.setDuration(Duration.millis(duration));
        transition.setByX(-83);
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