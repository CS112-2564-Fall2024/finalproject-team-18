package edu.miracosta.cs112.finalproject.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BlackjackTableDesign3CSFinalProj2024.fxml"));
            Parent root = loader.load();

            // Set up the scene
            Scene scene = new Scene(root);

            // Configure the stage
            primaryStage.setTitle("Blackjack Table");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }
}







/*
package edu.miracosta.cs112.finalproject.finalproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PlayGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a simple scene with a label
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Welcome to PlayGame!"));

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("PlayGame Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
*/



/*package edu.miracosta.cs112.finalproject.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class PlayGame {
//extends Application
//@Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayGame.class.getResource("C:\\Users\\mrodr\\IdeaProjects\\finalproject-team-18\\src\\main\\resources\\edu\\miracosta\\cs112\\finalproject\\finalproject\\BlackjackTableDesign3CSFinalProj2024.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

    }

}
*/
/*
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<?import javafx.scene.control.Button?>
<VBox alignment="CENTER" spacing="20.0" xmlns:fx="http://javafx.com/fxml"
fx:controller ="edu.miracosta.cs112.finalproject.finalproject.HelloController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>

    <Label fx:id="welcomeText"/>>
    <Button text="Hello!" onAction="#onHelloButtonClick"/>
</VBox>
*/