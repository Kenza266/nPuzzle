package com.example.demo2;

import com.nPuzzleHash.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindowInterface.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("8 puzzle");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("/Users/benx13/IdeaProjects/demo2/src/main/images/icon.png")));
        ((MainWindowController)loader.getController()).init(stage);
        stage.show();
    }

    public void run() {
        launch();
    }
}