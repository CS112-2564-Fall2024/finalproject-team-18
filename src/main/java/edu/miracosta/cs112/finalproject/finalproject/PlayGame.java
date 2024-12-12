package edu.miracosta.cs112.finalproject.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Core Class - Card class
// Abstract Class - Participant
// Inner Class - Card Counter inside deck class
// Inheritance/Polymorphism - Player and Dealer extends Participant
// Custom Exception -
// Javadoc Core Class -
// UML Design
// GUI Interface
// User Experience


public class PlayGame extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(PlayGame.class.getResource("BlackJackTableComplete-8.fxml"));
        primaryStage.setTitle("Blackjack Game");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}