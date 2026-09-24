package com.example.hellofx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {
    private static final String ORIGINAL_MESSAGE = "Welcome, Fact Bwalya!";

    @Override
    public void start(Stage stage) {
        Label message = new Label(ORIGINAL_MESSAGE);
        Button button = new Button("Start");
        Button resetButton = new Button("Reset");

        button.setOnAction(event -> message.setText("Great! You clicked the button."));
        resetButton.setOnAction(event -> message.setText(ORIGINAL_MESSAGE));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(message, button, resetButton);

        Scene scene = new Scene(layout, 500, 300);
        stage.setTitle("202500585");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}