package com.example.proiectdatabase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);//create the scene with the pref dimensions
        stage.setTitle("Star Wars");//title
        stage.setScene(scene);//set the scene
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}