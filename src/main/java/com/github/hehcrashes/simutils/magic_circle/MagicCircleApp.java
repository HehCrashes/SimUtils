package com.github.hehcrashes.simutils.magic_circle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MagicCircleApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/hehcrashes/simutils/magic_circle/MainView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("魔法阵编辑界面");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}